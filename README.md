# Techy Modpack managed files

Files in this repository are downloaded by File Director for Techy Modpack.

- `hbm_material_output_fixes.zs` removes the competing AE2, Ender IO, and Rustic 3x3 bedrock-fragment recipes, adds one HBM recipe per fragment, and puts HBM powder first in the relevant ore-dictionary lists.
- `hbmShredder.json` and the HBM-first ore-dictionary order make HBM's shredder output HBM gold, iron, lapis, coal, and quartz powders for every matching ore. Ender IO's own machine recipes remain unchanged.
- `rustic.cfg` disables Rustic's Extra Armor HUD and Armor Toughness HUD so HBM owns the armor/power display.
- `gnetum.json` disables Gnetum caching for modded post/unknown HUD elements (`gnetum_unknown`) so VoxelMap does not make the rest of the HUD translucent.
- `VoxelMapFixes-1.12.2-1.1.4.jar` (CurseForge file 9067816) and `naughthirium-3.0.0.jar` (CurseForge file 9146950) are installed via Mod Director's CurseForge provider and supersede `VoxelMapFixes-1.12.2-1.1.3.jar` and `naughthirium-2.3.0.jar`.
- `techy.bundle.json` tells File Director where managed files belong and pins their SHA-256 checksums.
- `techy.remote.json` should point at `.../refs/heads/main/techy.bundle.json` (not `/main/`), because GitHub's `/main/` raw CDN can lag and serve a stale bundle.
- `ntm-ae2-compat-1.1.4.jar` lets NTM-CE machines take AE2 processing patterns and choose the recipe fluid themselves. It supersedes `ntm-ae2-compat-1.1.3.jar`, which File Director disables by renaming it to `ntm-ae2-compat-1.1.3.jar.disabled-by-mod-director`.
- `hbm-de-nv-compat-1.0.1.jar` lets HBM night-vision goggles work on Draconic Evolution armor and registers Wyvern and Draconic helmets for HBM polluted-air and gas protection.
- `RedstoneFlux-1.12-2.1.1.1-universal.jar`, `BrandonsCore-1.12.2-2.4.20.162-universal.jar`, and `Draconic-Evolution-1.12.2-2.3.28.354-universal.jar` are installed into `mods`. Brandon's Core and Redstone Flux are required by Draconic Evolution.
- `!cleanroom-relauncher-1.2.1.jar` is installed into `mods` and supersedes `!cleanroom-relauncher-1.1.5.jar`. `!mixinbooter-11.18.jar` is installed from CleanroomMC GitHub releases. `+Fugue-0.25.4.jar` (CurseForge file 9202433) supersedes `+Fugue-0.25.3.jar`.
- Had Enough Items 4.36.1 is downloaded from the official CleanroomMC GitHub release and supersedes 4.35.1.
- `codechickenlib-3.3.8-universal.jar` is installed from Modrinth CodeChicken Lib CRE.
- `Essential_1-5-0-1_forge_1-12-2.jar` is installed into `mods` with a Cleanroom/Java 25 patched embedded `stage1` loader (stock Essential overwrites `essential/loader/.../stage1.jar` from that embedded copy on launch).
- The `essential/` loader bootstrap (patched `stage1.jar` plus `libraries/forge_1.12.2`) is also installed into the instance `essential/` folder for first-run state.
- Flan's Mod 5.10.0 and the obsolete Had Enough Items 4.35.0 jar are deleted from `mods`.

Existing installations fetch the current bundle at startup. File Director downloads managed files when their checksum differs and applies the removal rules before Minecraft loads.

## Essential + Cleanroom (Java 25) crash

If the log shows `Re-launching to load the newer versions` then
`AppClassLoader cannot be cast to URLClassLoader`, Essential is crashing
**before** Mod Director can install fixes.

### Tell affected players (works immediately)

1. Modrinth → their instance → **Settings → Java** → extra arguments, add:

```
-Dessential.loader.relaunch=false
```

2. Launch once. Mod Director can then finish updating files.

Optional: in `config/relauncher.json`, ensure `args` contains
`-Dessential.loader.relaunch=false` (your working Techy profile already does).

### Pack / File Director

- `!techy-essential-norelaunch-1.0.0.jar` sets that property before Essential loads.
  **Ship this jar inside the Modrinth pack `mods/` folder** — Mod Director cannot
  install it for players who already crash on Essential.
- `relauncher.json` in this repo is a distribution template with the same JVM flag
  and `autoSetup: true` (no machine-specific `javaPath`). Put it in pack
  `config/relauncher.json` for new installs.
- `techy.remote.json` must use `.../refs/heads/main/techy.bundle.json` (not `/main/`).
