// Make HBM the canonical dust/powder for HBM's automatically generated
// shredder recipes, while leaving the other dusts registered for their own
// mods' machines and recipes.

val hbmGoldPowder = <hbm:powder_gold>;
val hbmIronPowder = <hbm:powder_iron>;
val hbmCoalPowder = <hbm:powder_coal>;
val hbmQuartzPowder = <hbm:powder_quartz>;

val ae2GoldDust = <appliedenergistics2:material:51>;
val ae2IronDust = <appliedenergistics2:material:49>;
val enderIoGoldDust = <enderio:item_material:25>;
val enderIoIronPowder = <enderio:item_material:24>;
val enderIoCoalPowder = <enderio:item_material:23>;
val enderIoLapisPowder = <enderio:item_material:32>;
val ae2QuartzDust = <appliedenergistics2:material:3>;
val rusticGoldDust = <rustic:dust_gold>;

val hbmLapisPowder = <hbm:powder_lapis>;

val dustGold = <ore:dustGold>;
val dustIron = <ore:dustIron>;
val dustLapis = <ore:dustLapis>;
val dustCoal = <ore:dustCoal>;
val dustQuartz = <ore:dustQuartz>;

// HBM's shredder chooses the first registered dust for oreGold/oreIron/etc.
// Remove and re-add the known dusts in a deterministic order with HBM first.
// These calls deliberately take one item each: this pack's CraftTweaker/ZenScript
// combination can crash when an ore-dictionary method receives several items.
dustGold.remove(hbmGoldPowder);
dustGold.remove(ae2GoldDust);
dustGold.remove(enderIoGoldDust);
dustGold.remove(rusticGoldDust);
dustGold.add(hbmGoldPowder);
dustGold.add(ae2GoldDust);
dustGold.add(enderIoGoldDust);
dustGold.add(rusticGoldDust);

dustIron.remove(hbmIronPowder);
dustIron.remove(ae2IronDust);
dustIron.remove(enderIoIronPowder);
dustIron.add(hbmIronPowder);
dustIron.add(ae2IronDust);
dustIron.add(enderIoIronPowder);

dustLapis.remove(hbmLapisPowder);
dustLapis.remove(enderIoLapisPowder);
dustLapis.add(hbmLapisPowder);
dustLapis.add(enderIoLapisPowder);

dustCoal.remove(hbmCoalPowder);
dustCoal.remove(enderIoCoalPowder);
dustCoal.add(hbmCoalPowder);
dustCoal.add(enderIoCoalPowder);

dustQuartz.remove(hbmQuartzPowder);
dustQuartz.remove(ae2QuartzDust);
dustQuartz.add(hbmQuartzPowder);
dustQuartz.add(ae2QuartzDust);

val goldFragment = <hbm:bedrock_ore_fragment:7900>;
val ironFragment = <hbm:bedrock_ore_fragment:2600>;
val coalFragment = <hbm:bedrock_ore_fragment:600>;
val quartzFragment = <hbm:bedrock_ore_fragment:1402>;

recipes.removeShaped(ae2GoldDust, [
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment]
]);
recipes.removeShaped(enderIoGoldDust, [
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment],
    [goldFragment, goldFragment, goldFragment]
]);
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

recipes.removeShaped(enderIoIronPowder, [
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment],
    [ironFragment, ironFragment, ironFragment]
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

print("Techy HBM output fixes loaded successfully");
