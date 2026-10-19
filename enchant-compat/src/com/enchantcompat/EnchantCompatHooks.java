package com.enchantcompat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Shared enchantability checks. HBM unbreakable tools (bismuth, volcanic, etc.)
 * use ToolMaterial maxUses=0 so {@code isDamageable()} is false, which blocks
 * vanilla {@code isEnchantable()} and EnderCore XP Boost's custom type predicate.
 */
public final class EnchantCompatHooks {

    private EnchantCompatHooks() {
    }

    public static boolean canTakeEnchants(ItemStack stack) {
        if (stack == null || stack.func_190926_b()) {
            return false;
        }
        Item item = stack.func_77973_b();
        if (item == null) {
            return false;
        }
        // Normal tools / armor with durability
        if (item.func_77645_m()) {
            return true;
        }
        // Unbreakable but still enchantable (HBM bismuth maxUses=0, DE energy tools, …)
        return item.getItemEnchantability(stack) > 0;
    }
}
