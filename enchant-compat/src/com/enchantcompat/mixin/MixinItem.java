package com.enchantcompat.mixin;

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
 */
@Mixin(Item.class)
public abstract class MixinItem {

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$canApplyAtTable(ItemStack stack, Enchantment enchantment,
            CallbackInfoReturnable<Boolean> cir) {
        if (enchantment == null || stack == null || stack.func_190926_b()) {
            return;
        }
        Item item = (Item) (Object) this;
        if (item.func_77645_m() || item.getItemEnchantability(stack) > 0) {
            cir.setReturnValue(Boolean.TRUE);
        }
    }
}
