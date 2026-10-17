package com.brandon3055.draconicevolution.items.tools;

import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public abstract class MiningToolBase {
    public abstract Map<Enchantment, Integer> getDisabledEnchants(ItemStack stack);
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }
}
