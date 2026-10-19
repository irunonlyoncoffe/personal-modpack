$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$repo = Split-Path -Parent $root
$version = "1.1.6"
$baseJar = Join-Path $repo "ntm-ae2-compat-1.1.5.jar"
$outJar = Join-Path $repo "ntm-ae2-compat-$version.jar"
$hbm = "C:\Users\carol\AppData\Roaming\ModrinthApp\profiles\Techy Modpack\mods\NTM-CE-1.12.2-2.6.1.0.jar"
$ae2 = "C:\Users\carol\AppData\Roaming\ModrinthApp\profiles\Techy Modpack\mods\ae2-uel-v0.56.7.jar"

foreach ($p in @($baseJar, $hbm, $ae2)) {
  if (-not (Test-Path $p)) { throw "Missing dependency: $p" }
}

$build = Join-Path $root "build"
$classes = Join-Path $build "classes"
$stage = Join-Path $build "stage"
Remove-Item $build -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $classes, $stage | Out-Null

$stubClasses = Join-Path $build "stubs"
New-Item -ItemType Directory -Force -Path $stubClasses | Out-Null
& javac -encoding UTF-8 -source 1.8 -target 1.8 -d $stubClasses (Get-ChildItem (Join-Path $root "stubs") -Recurse -Filter "*.java" | ForEach-Object FullName)
if ($LASTEXITCODE -ne 0) { throw "Stub compile failed" }

$cp = @($baseJar, $hbm, $ae2, $stubClasses) -join ";"
$sources = Get-ChildItem (Join-Path $root "src") -Recurse -Filter "*.java" | ForEach-Object FullName
& javac -encoding UTF-8 -source 1.8 -target 1.8 -cp $cp -d $classes $sources
if ($LASTEXITCODE -ne 0) { throw "Mixin compile failed" }

Copy-Item $baseJar $outJar -Force
Push-Location $stage
try {
  & jar xf $outJar
  Copy-Item (Join-Path $classes "com\*") (Join-Path $stage "com") -Recurse -Force

  $mixinJson = @'
{
  "required": true,
  "package": "com.ntmae2.mixin",
  "compatibilityLevel": "JAVA_8",
  "minVersion": "0.8",
  "mixins": [
    "MixinCraftingHelper",
    "MixinProxyCombo",
    "MixinChemPlant",
    "MixinAssembly",
    "MixinPrecAss",
    "MixinRockMill",
    "MixinPurex",
    "MixinFusion",
    "MixinPlasma",
    "MixinChemFactory",
    "MixinAssemblyFactory",
    "MixinSoldering",
    "MixinWelder",
    "MixinRotary",
    "MixinCrucible",
    "MixinAcidizer",
    "MixinMixer",
    "MixinPyro",
    "MixinSolidifier",
    "MixinCoker",
    "MixinHydrotreater",
    "MixinReformer",
    "MixinVacuum",
    "MixinRefinery",
    "MixinElectrolyser",
    "MixinFractionTower",
    "MixinCatalyticCracker"
  ]
}
'@
  $utf8 = New-Object System.Text.UTF8Encoding $false
  [System.IO.File]::WriteAllText((Join-Path $stage "mixins.ntmae2compat.json"), $mixinJson, $utf8)

  $mcmod = @"
[
  {
    "modid": "ntmae2compat",
    "name": "NTM AE2 Compat",
    "description": "Lets NTM-CE machines take AE2 processing patterns and select the recipe fluid themselves. Built for NTM-CE 1.12.2-2.6.1.0.",
    "version": "$version",
    "mcversion": "1.12.2",
    "authorList": ["ntmae2compat"],
    "dependencies": ["hbm", "appliedenergistics2"]
  }
]
"@
  [System.IO.File]::WriteAllText((Join-Path $stage "mcmod.info"), $mcmod, $utf8)

  $manifest = Join-Path $build "MANIFEST.MF"
  Copy-Item (Join-Path $stage "META-INF\MANIFEST.MF") $manifest -Force
  Remove-Item (Join-Path $stage "META-INF") -Recurse -Force
  if (Test-Path $outJar) { Remove-Item $outJar -Force }
  & jar cfm $outJar $manifest -C $stage .
  if ($LASTEXITCODE -ne 0) { throw "jar pack failed" }
}
finally {
  Pop-Location
}

$hash = (Get-FileHash -LiteralPath $outJar -Algorithm SHA256).Hash.ToLowerInvariant()
Write-Host "Built $outJar"
Write-Host "SHA-256 $hash"
Write-Output $hash
