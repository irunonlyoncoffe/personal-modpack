// Keep HBM bedrock-fragment crafting outputs in HBM's material system.
// This does not reorder the ore dictionary, so Ender IO and AE2 machines
// remain free to produce their own powders and dusts.

val hbmGoldPowder = <hbm:powder_gold>;
val hbmIronPowder = <hbm:powder_iron>;
val hbmCoalPowder = <hbm:powder_coal>;
val hbmQuartzPowder = <hbm:powder_quartz>;

val ae2GoldDust = <appliedenergistics2:material:51>;
val enderIoIronPowder = <enderio:item_material:24>;
val enderIoCoalPowder = <enderio:item_material:23>;
val ae2QuartzDust = <appliedenergistics2:material:3>;

val goldFragment = <hbm:bedrock_ore_fragment:7900>;
val ironFragment = <hbm:bedrock_ore_fragment:2600>;
val coalFragment = <hbm:bedrock_ore_fragment:600>;
val quartzFragment = <hbm:bedrock_ore_fragment:1402>;

recipes.removeShaped(ae2GoldDust, [
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment]
]);
recipes.addShaped("hbm_gold_fragments_to_hbm_powder", hbmGoldPowder, [
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment]
]);

recipes.removeShaped(enderIoIronPowder, [
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment]
]);
recipes.addShaped("hbm_iron_fragments_to_hbm_powder", hbmIronPowder, [
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment]
]);

recipes.removeShaped(enderIoCoalPowder, [
    [coalFragment, coalFragment, coalFragment],
    [coalFragment, coalFragment, coalFragment],
    [coalFragment, coalFragment, coalFragment]
]);
recipes.addShaped("hbm_coal_fragments_to_hbm_powder", hbmCoalPowder, [
    [coalFragment, coalFragment, coalFragment],
    [coalFragment, coalFragment, coalFragment],
    [coalFragment, coalFragment, coalFragment]
]);

recipes.removeShaped(ae2QuartzDust, [
    [quartzFragment, quartzFragment, quartzFragment],
    [quartzFragment, quartzFragment, quartzFragment],
    [quartzFragment, quartzFragment, quartzFragment]
]);
recipes.addShaped("hbm_quartz_fragments_to_hbm_powder", hbmQuartzPowder, [
    [quartzFragment, quartzFragment, quartzFragment],
    [quartzFragment, quartzFragment, quartzFragment],
    [quartzFragment, quartzFragment, quartzFragment]
]);
