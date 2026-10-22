# Techy Modpack managed files

Files in this repository are downloaded by File Director for Techy Modpack.

- `hbm_material_output_fixes.zs` removes the competing AE2, Ender IO, and Rustic 3x3 bedrock-fragment recipes, adds one HBM recipe per fragment, and puts HBM powder first in the relevant ore-dictionary lists.
- `hbmShredder.json` and the HBM-first ore-dictionary order make HBM's shredder output HBM gold, iron, lapis, coal, and quartz powders for every matching ore. Ender IO's own machine recipes remain unchanged.
- `rustic.cfg` disables Rustic's Extra Armor HUD and Armor Toughness HUD so HBM owns the armor/power display.
- `classicbar-0.6.1.jar` replaces the vanilla health/food/armor icons with Classic Bars. `hbm-classicbar-1.0.1.jar` keeps HBM's armor power/fuel/shield HUD when Classic Bars cancels the vanilla armor row. `classicbar.cfg` turns off Classic Bars' Rustic chat warning (Rustic's HUD is already disabled by `rustic.cfg`). Older `hbm-classicbar-1.0.0.jar` is deleted by File Director `modify` rules.
- `gnetum.json` disables Gnetum caching for modded post/unknown HUD elements (`gnetum_unknown`) so VoxelMap does not make the rest of the HUD translucent.
- `VoxelMapFixes`, `naughthirium`, Fugue, Had Enough Items, Cleanroom Relauncher, MixinBooter, and CodeChicken Lib are shipped by the Modrinth pack itself (not managed by File Director).
- `techy.bundle.json` tells File Director where managed files belong and pins their SHA-256 checksums.
- `techy.remote.json` should point at `.../refs/heads/main/techy.bundle.json` (not `/main/`), because GitHub's `/main/` raw CDN can lag and serve a stale bundle.
- `ntm-ae2-compat-1.1.9.jar` lets NTM-CE machines take AE2 processing patterns and choose the recipe fluid themselves, including the fractioning tower and catalytic cracking tower. It keeps the 1.1.5 machine path. The mixer refuses a push that does not fit entirely and AE retries it, so a large mixer job cannot mark inputs consumed when the tanks only took part of them. Older `ntm-ae2-compat-1.1.4.jar` / `1.1.5.jar` / `1.1.6.jar` / `1.1.7.jar` / `1.1.8.jar` are deleted by File Director `modify` rules (not renamed via `supersede`) so Windows file locks during launch do not abort Mod Director.
- `hbm-de-nv-compat-1.0.2.jar` lets HBM night-vision goggles work on Draconic Evolution armor, seals Wyvern/Draconic sets so NTM-Space PLSS can be installed and used on them, and gives them Remnants Power Armor radiation resistance. Older `hbm-de-nv-compat-1.0.1.jar` is deleted by File Director `modify` rules for the same reason.
- `enchant-compat-1.0.3.jar` lets mod enchants combine and apply to HBM/DE/Quantum tools, lets XP Boost hit HBM bismuth tools, and makes OpenBlocks Last Stand spend XP before Draconic armor's RF death save. 1.0.3 fixes `@Mixin` retention so CleanMix actually applies the mixins (1.0.2 loaded but did nothing). Older `enchant-compat-1.0.0.jar` / `1.0.1.jar` / `1.0.2.jar` are deleted by File Director `modify` rules (not `supersede`).
- `RedstoneFlux-1.12-2.1.1.1-universal.jar`, `BrandonsCore-1.12.2-2.4.20.162-universal.jar`, and `Draconic-Evolution-1.12.2-2.3.28.354-universal.jar` are installed into `mods`. Brandon's Core and Redstone Flux are required by Draconic Evolution.
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
