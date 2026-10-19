package com.enchantcompat.mixin;

import com.enchantcompat.EnchantCompatHooks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes enchantments combinable with each other and applicable to any item that
 * reports enchantability (covers HBM tools, DE energy tools, QuantumThings, etc.).
 */
@Mixin(Enchantment.class)
public abstract class MixinEnchantment {

    @Inject(method = "isCompatibleWith", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$allCompatible(Enchantment other, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(other != (Object) this);
    }

    /** canApply — SRG name in production 1.12.2 */
    @Inject(method = "func_92089_a", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$canApply(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (EnchantCompatHooks.canTakeEnchants(stack)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }

    /** Forge method — not obfuscated */
    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$canApplyAtTable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (EnchantCompatHooks.canTakeEnchants(stack)) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }
}
