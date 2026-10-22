package com.brandon3055.draconicevolution.items.tools;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public abstract class MiningToolBase extends ToolBase {
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
}
