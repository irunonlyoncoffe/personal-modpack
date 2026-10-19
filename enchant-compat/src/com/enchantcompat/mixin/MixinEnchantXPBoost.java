package com.enchantcompat.mixin;

import com.enchantcompat.EnchantCompatHooks;
import com.enderio.core.common.enchant.EnchantXPBoost;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * EnderCore XP Boost uses a custom EnumEnchantmentType that requires
 * {@code Item.isDamageable()}. HBM bismuth/volcanic tools use maxUses=0
 * (unbreakable), so the type predicate rejects them even though they are
 * otherwise enchantable. Force-allow when the item reports enchantability.
 */
@Mixin(value = EnchantXPBoost.class, remap = false)
public abstract class MixinEnchantXPBoost {

    @Inject(method = "func_92089_a", at = @At("HEAD"), cancellable = true)
    private void enchantcompat$canApply(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (EnchantCompatHooks.canTakeEnchants(stack)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true)
    private void enchantcompat$canApplyAtTable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (EnchantCompatHooks.canTakeEnchants(stack)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }
}
