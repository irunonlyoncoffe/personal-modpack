package com.techymodpack;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Disables mod jars that a newer managed file has replaced.
 *
 * File Director's supersede rename runs after Cleanroom has already opened those jars.
 * On Windows that rename throws, and File Director treats the exception as fatal and exits.
 * This plugin renames the old jars itself. If the game still has them open, a detached
 * helper waits until this process exits and renames them then.
 */
@IFMLLoadingPlugin.Name("Techy obsolete mod cleanup")
public class ObsoleteModCleanup implements IFMLLoadingPlugin {
    static final String DISABLED_SUFFIX = ".disabled-by-mod-director";

    /**
     * Old filename, then the replacement that must already exist.
     * An empty replacement means the old file is always disabled.
     */
    static final String[][] OBSOLETE = {
            {"ntm-ae2-compat-1.1.3.jar", "ntm-ae2-compat-1.1.4.jar"},
            {"naughthirium-2.3.0.jar", "naughthirium-3.0.0.jar"},
            {"VoxelMapFixes-1.12.2-1.1.3.jar", "VoxelMapFixes-1.12.2-1.1.4.jar"},
            {"HadEnoughItems_1.12.2-4.35.1.jar", "HadEnoughItems_1.12.2-4.36.1.jar"},
            {"HadEnoughItems_1.12.2-4.35.0.jar", "HadEnoughItems_1.12.2-4.36.1.jar"},
            {"!cleanroom-relauncher-1.1.5.jar", "!cleanroom-relauncher-1.2.1.jar"},
            {"!cleanroom-relauncher-1.1.4.jar", "!cleanroom-relauncher-1.2.1.jar"},
            {"!mixinbooter-11.17.jar", "!mixinbooter-11.18.jar"},
            {"+Fugue-0.24.4.jar", "+Fugue-0.25.3.jar"},
            {"Flan's Mod-1.12.2-5.10.0.jar", ""}
    };

    private static final AtomicBoolean STARTED = new AtomicBoolean();

    public ObsoleteModCleanup() {
        run(null);
    }

    static void run(File minecraftHome) {
        if (!STARTED.compareAndSet(false, true)) {
            return;
        }
        try {
            File mods = findMods(minecraftHome);
            if (mods == null) {
                STARTED.set(false);
                return;
            }
            Map<File, File> locked = disableUnlocked(mods);
            if (!locked.isEmpty()) {
                spawnWatcher(ProcessHandle.current().pid(), locked);
            }
        } catch (Throwable thrown) {
            STARTED.set(false);
            thrown.printStackTrace();
        }
    }

    static File findMods(File minecraftHome) {
        if (minecraftHome != null) {
            File mods = new File(minecraftHome, "mods");
            if (mods.isDirectory()) {
                return mods;
            }
        }
        File mods = new File("mods");
        if (mods.isDirectory()) {
            return mods.getAbsoluteFile();
        }
        return null;
    }

    /** @return old file to disabled destination, for jars that could not be moved yet */
    static Map<File, File> disableUnlocked(File mods) {
        Map<File, File> locked = new LinkedHashMap<File, File>();
        for (String[] job : OBSOLETE) {
            File source = new File(mods, job[0]);
            if (!source.isFile()) {
                continue;
            }
            if (!job[1].isEmpty() && !new File(mods, job[1]).isFile()) {
                continue;
            }
            File destination = new File(mods, job[0] + DISABLED_SUFFIX);
            try {
                Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("[Techy] Disabled " + source.getName());
            } catch (IOException failure) {
                locked.put(source, destination);
                if (!(failure instanceof FileSystemException)) {
                    failure.printStackTrace();
                }
            }
        }
        return locked;
    }

    static void spawnWatcher(long pid, Map<File, File> locked) throws IOException {
        boolean windows = System.getProperty("os.name", "").toLowerCase().contains("win");
        File script = File.createTempFile("techy-disable-obsolete", windows ? ".bat" : ".sh");
        try (Writer writer = new OutputStreamWriter(Files.newOutputStream(script.toPath()), StandardCharsets.UTF_8)) {
            writer.write(windows ? windowsScript(pid, locked) : posixScript(pid, locked));
        }
        if (!windows && !script.setExecutable(true) && !script.canExecute()) {
            System.err.println("[Techy] Could not mark the cleanup script executable: " + script);
        }
        ProcessBuilder builder = windows
                ? new ProcessBuilder(
                "powershell.exe",
                "-NoProfile",
                "-WindowStyle", "Hidden",
                "-Command",
                "Start-Process -WindowStyle Hidden -FilePath " + powershellLiteral(script.getAbsolutePath()))
                : new ProcessBuilder("nohup", "sh", script.getAbsolutePath());
        builder.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        builder.redirectError(ProcessBuilder.Redirect.DISCARD);
        builder.start();
        System.out.println("[Techy] Old mod jars are still open. They will be disabled after this game exits.");
    }

    static String windowsScript(long pid, Map<File, File> locked) {
        StringBuilder script = new StringBuilder();
        script.append("@echo off\r\n");
        script.append("chcp 65001 >nul\r\n");
        script.append(":wait\r\n");
        script.append("tasklist /FI \"PID eq ").append(pid).append("\" /NH | findstr \"")
                .append(pid).append("\" >nul\r\n");
        script.append("if not errorlevel 1 (\r\n");
        script.append("  ping -n 6 127.0.0.1 >nul\r\n");
        script.append("  goto wait\r\n");
        script.append(")\r\n");
        script.append("set TRIES=0\r\n");
        script.append(":retry\r\n");
        for (Map.Entry<File, File> entry : locked.entrySet()) {
            script.append("if exist ").append(batQuote(entry.getKey())).append(" move /Y ")
                    .append(batQuote(entry.getKey())).append(' ').append(batQuote(entry.getValue()))
                    .append(" >nul 2>&1\r\n");
        }
        script.append("set PENDING=0\r\n");
        for (File source : locked.keySet()) {
            script.append("if exist ").append(batQuote(source)).append(" set PENDING=1\r\n");
        }
        script.append("if \"%PENDING%\"==\"0\" goto done\r\n");
        script.append("set /a TRIES+=1\r\n");
        script.append("if %TRIES% GEQ 30 goto done\r\n");
        script.append("ping -n 3 127.0.0.1 >nul\r\n");
        script.append("goto retry\r\n");
        script.append(":done\r\n");
        script.append("del \"%~f0\"\r\n");
        return script.toString();
    }

    static String posixScript(long pid, Map<File, File> locked) {
        StringBuilder script = new StringBuilder();
        script.append("#!/bin/sh\n");
        script.append("while kill -0 ").append(pid).append(" 2>/dev/null; do\n");
        script.append("  sleep 2\n");
        script.append("done\n");
        script.append("tries=0\n");
        script.append("while [ \"$tries\" -lt 30 ]; do\n");
        for (Map.Entry<File, File> entry : locked.entrySet()) {
            script.append("  if [ -f ").append(shQuote(entry.getKey().getAbsolutePath())).append(" ]; then\n");
            script.append("    mv -f ").append(shQuote(entry.getKey().getAbsolutePath())).append(' ')
                    .append(shQuote(entry.getValue().getAbsolutePath())).append(" 2>/dev/null\n");
            script.append("  fi\n");
        }
        script.append("  pending=0\n");
        for (File source : locked.keySet()) {
            script.append("  if [ -f ").append(shQuote(source.getAbsolutePath())).append(" ]; then pending=1; fi\n");
        }
        script.append("  if [ \"$pending\" -eq 0 ]; then break; fi\n");
        script.append("  tries=$((tries + 1))\n");
        script.append("  sleep 1\n");
        script.append("done\n");
        script.append("rm -f \"$0\"\n");
        return script.toString();
    }

    private static String batQuote(File file) {
        return "\"" + file.getAbsolutePath().replace("%", "%%").replace("\"", "") + "\"";
    }

    private static String shQuote(String value) {
        return "'" + value.replace("'", "'\\''") + "'";
    }

    private static String powershellLiteral(String value) {
        return "'" + value.replace("'", "''") + "'";
    }

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        Object location = data == null ? null : data.get("mcLocation");
        run(location instanceof File ? (File) location : null);
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
