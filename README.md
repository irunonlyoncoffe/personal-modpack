# Techy Modpack managed files

Files in this repository are downloaded by File Director for Techy Modpack.

- `hbm_material_output_fixes.zs` contains the CraftTweaker material-output fixes.
- `techy.bundle.json` tells File Director where the script belongs and pins its SHA-256 checksum.

To publish a script update, replace the `.zs` file and update the SHA-256 value in the bundle. Existing installations fetch the current bundle at startup and only replace the local script when its checksum differs.
