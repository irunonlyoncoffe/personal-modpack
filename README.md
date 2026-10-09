# Techy Modpack managed files

Files in this repository are downloaded by File Director for Techy Modpack.

- `hbm_material_output_fixes.zs` contains the CraftTweaker material-output fixes.
- `rustic.cfg` disables Rustic's Extra Armor HUD and Armor Toughness HUD so HBM owns the armor/power display.
- `techy.bundle.json` tells File Director where managed files belong and pins their SHA-256 checksums.
- Had Enough Items 4.35.1 is downloaded from the official CleanroomMC GitHub release.
- Flan's Mod 5.10.0 and the obsolete Had Enough Items 4.35.0 jar are deleted from `mods`.

Existing installations fetch the current bundle at startup. File Director downloads managed files when their checksum differs and applies the removal rules before Minecraft loads.
