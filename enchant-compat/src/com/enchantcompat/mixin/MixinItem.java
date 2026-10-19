package com.enchantcompat.mixin;

import com.enchantcompat.EnchantCompatHooks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Default Item.canApplyAtEnchantingTable only checks EnumEnchantmentType, which
 * rejects custom types (XP Boost) and WEAPON/DIGGER checks on DE energy items
 * that are not ItemSword/ItemTool subclasses.
 * <p>
 * Item.isEnchantable requires isDamageable(), which fails for HBM unbreakable
 * tools (bismuth/volcanic maxUses=0) even though they have high enchantability.
 */
@Mixin(Item.class)
public abstract class MixinItem {

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$canApplyAtTable(ItemStack stack, Enchantment enchantment,
            CallbackInfoReturnable<Boolean> cir) {
        if (enchantment == null) {
            return;
        }
        if (EnchantCompatHooks.canTakeEnchants(stack)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }

    /** isEnchantable — SRG func_77616_k; enchanting table gate */
    @Inject(method = "func_77616_k", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$isEnchantable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (EnchantCompatHooks.canTakeEnchants(stack)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }
}
