$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$repo = Split-Path -Parent $root
$version = "1.0.0"
$outJar = Join-Path $repo "enchant-compat-$version.jar"
$profileMods = "C:\Users\carol\AppData\Roaming\ModrinthApp\profiles\Techy Modpack\mods"
$deJar = Join-Path $profileMods "Draconic-Evolution-1.12.2-2.3.28.354-universal.jar"

if (-not (Test-Path $deJar)) { throw "Missing DE jar: $deJar" }

$build = Join-Path $root "build"
$classes = Join-Path $build "classes"
$stubClasses = Join-Path $build "stubs"
$stage = Join-Path $build "stage"
Remove-Item $build -Recurse -Force -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path $classes, $stubClasses, $stage | Out-Null

$stubSources = Get-ChildItem (Join-Path $root "stubs\src") -Recurse -Filter "*.java" | ForEach-Object FullName
& javac -encoding UTF-8 -source 1.8 -target 1.8 -d $stubClasses $stubSources
if ($LASTEXITCODE -ne 0) { throw "Stub compile failed" }

$cp = @($stubClasses, $deJar) -join ";"
$sources = Get-ChildItem (Join-Path $root "src") -Recurse -Filter "*.java" | ForEach-Object FullName
& javac -encoding UTF-8 -source 1.8 -target 1.8 -cp $cp -d $classes $sources
if ($LASTEXITCODE -ne 0) { throw "Source compile failed" }

Push-Location $stage
try {
  New-Item -ItemType Directory -Force -Path "META-INF" | Out-Null
  Copy-Item (Join-Path $root "resources\META-INF\MANIFEST.MF") "META-INF\MANIFEST.MF" -Force
  Copy-Item (Join-Path $root "resources\mixins.enchantcompat.json") . -Force
  Copy-Item (Join-Path $root "resources\mcmod.info") . -Force
  Copy-Item (Join-Path $classes "com") . -Recurse -Force
  & jar cfm $outJar "META-INF\MANIFEST.MF" mixins.enchantcompat.json mcmod.info com
  if ($LASTEXITCODE -ne 0) { throw "jar failed" }
} finally {
  Pop-Location
}

$profileOut = Join-Path $profileMods "enchant-compat-$version.jar"
Copy-Item $outJar $profileOut -Force
Write-Host "Built $outJar"
Write-Host "Installed $profileOut"
& jar tf $outJar
