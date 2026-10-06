// Keep HBM:CE materials as the preferred outputs for HBM ore processing.
// Other mods' items remain valid ore-dictionary equivalents, but are
// moved behind HBM's entries so they no longer take precedence.

val hbmGoldPowder = <hbm:powder_gold>;
val rusticGoldDust = <rustic:dust_gold>;
val hbmIronPowder = <hbm:powder_iron>;
val ae2IronDust = <appliedenergistics2:material:49>;


val goldFragment = <hbm:bedrock_ore_fragment:7900>;
val ironFragment = <hbm:bedrock_ore_fragment:2600>;

<ore:dustGold>.remove(rusticGoldDust);
<ore:dustGold>.add(rusticGoldDust);
<ore:dustIron>.remove(ae2IronDust);
<ore:dustIron>.add(ae2IronDust);


// HBM generates these recipes from the first ore-dictionary entry, which was
// Rustic Gold Dust and AE2 Iron Dust in this pack. Replace those exact recipes.
recipes.removeShaped(rusticGoldDust, [
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment]
]);


recipes.addShaped("hbm_gold_fragments_to_hbm_powder", hbmGoldPowder, [
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment]
]);


recipes.removeShaped(ae2IronDust, [
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment]
]);


recipes.addShaped("hbm_iron_fragments_to_hbm_powder", hbmIronPowder, [
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment]
]);
