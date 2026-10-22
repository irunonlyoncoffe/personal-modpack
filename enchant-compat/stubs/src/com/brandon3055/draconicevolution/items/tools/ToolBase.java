package com.brandon3055.draconicevolution.items.tools;

import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public abstract class ToolBase {
    public Map<Enchantment, Integer> getDisabledEnchants(ItemStack stack) {
        return null;
    }
}
