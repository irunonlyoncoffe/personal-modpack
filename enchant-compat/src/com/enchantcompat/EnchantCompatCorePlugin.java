package com.enchantcompat;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.mixin.Mixins;

@IFMLLoadingPlugin.Name("Enchant Compat")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.SortingIndex(1105)
public final class EnchantCompatCorePlugin implements IFMLLoadingPlugin {
    private static boolean registered;

    public EnchantCompatCorePlugin() {
        if (!registered) {
            Mixins.addConfiguration("mixins.enchantcompat.json");
            registered = true;
            System.out.println("[Enchant Compat] Registered mixins.enchantcompat.json");
        }
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
    }

    public String getAccessTransformerClass() {
        return null;
    }
}
