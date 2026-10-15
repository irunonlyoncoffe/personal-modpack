# Techy Modpack managed files

Files in this repository are downloaded by File Director for Techy Modpack.

- `hbm_material_output_fixes.zs` removes the competing AE2, Ender IO, and Rustic 3x3 bedrock-fragment recipes, adds one HBM recipe per fragment, and puts HBM powder first in the relevant ore-dictionary lists.
- `hbmShredder.json` and the HBM-first ore-dictionary order make HBM's shredder output HBM gold, iron, lapis, coal, and quartz powders for every matching ore. Ender IO's own machine recipes remain unchanged.
- `rustic.cfg` disables Rustic's Extra Armor HUD and Armor Toughness HUD so HBM owns the armor/power display.
- `gnetum.json` disables Gnetum caching for modded post/unknown HUD elements (`gnetum_unknown`) so VoxelMap does not make the rest of the HUD translucent.
- `VoxelMapFixes-1.12.2-1.1.4.jar` and `naughthirium-3.0.0.jar` are installed from CurseForge and replace `VoxelMapFixes-1.12.2-1.1.3.jar` and `naughthirium-2.3.0.jar`.
- `techy.bundle.json` tells File Director where managed files belong and pins their SHA-256 checksums.
- `ntm-ae2-compat-1.1.4.jar` lets NTM-CE machines take AE2 processing patterns and choose the recipe fluid themselves. It replaces `ntm-ae2-compat-1.1.3.jar`.
- `hbm-de-nv-compat-1.0.1.jar` lets HBM night-vision goggles work on Draconic Evolution armor and registers Wyvern and Draconic helmets for HBM polluted-air and gas protection.
- `RedstoneFlux-1.12-2.1.1.1-universal.jar`, `BrandonsCore-1.12.2-2.4.20.162-universal.jar`, and `Draconic-Evolution-1.12.2-2.3.28.354-universal.jar` are installed into `mods`. Brandon's Core and Redstone Flux are required by Draconic Evolution.
- `!cleanroom-relauncher-1.2.1.jar` is installed into `mods` and replaces `!cleanroom-relauncher-1.1.5.jar` and `!cleanroom-relauncher-1.1.4.jar`. `!mixinbooter-11.18.jar` and `+Fugue-0.25.3.jar` are installed from CleanroomMC GitHub releases and replace the previous Cleanroom loader jars.
- Had Enough Items 4.36.1 is downloaded from the official CleanroomMC GitHub release and replaces 4.35.1 and 4.35.0.
- `codechickenlib-3.3.8-universal.jar` is installed from Modrinth CodeChicken Lib CRE.
- `Essential_1-5-0-1_forge_1-12-2.jar` is installed into `mods`.
- `!techy-obsolete-mod-cleanup.jar` renames replaced jars to `*.disabled-by-mod-director` once the replacement is installed. File Director no longer renames those jars itself: Cleanroom already has them open, and on Windows that locked rename aborts launch. If a jar is still open, the cleanup waits until the game exits and renames it before the next start. That covers `ntm-ae2-compat-1.1.3.jar`, `naughthirium-2.3.0.jar`, Flan's Mod 5.10.0, and the older Had Enough Items, VoxelMapFixes, Cleanroom loader, MixinBooter, and Fugue jars.

Existing installations fetch the current bundle at startup. File Director downloads managed files when their checksum differs.
