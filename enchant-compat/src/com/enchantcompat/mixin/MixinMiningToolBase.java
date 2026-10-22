package com.enchantcompat.mixin;

import com.brandon3055.draconicevolution.items.tools.MiningToolBase;
import com.brandon3055.draconicevolution.items.tools.ToolBase;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * DE mining tools only accept DIGGER/ALL enchant types at the table, which blocks
 * EnderCore XP Boost (custom type), Reaper, etc. Allow any enchant that isn't
 * explicitly disabled on the tool.
 *
 * Do not @Shadow getDisabledEnchants here: it lives as a concrete method on ToolBase.
 * An abstract @Shadow previously redeclared it on MiningToolBase and crashed DE HUD
 * (Chaotic Staff, etc.) with AbstractMethodError.
 */
@Mixin(value = MiningToolBase.class, remap = false)
public abstract class MixinMiningToolBase {

    @Inject(method = "canApplyAtEnchantingTable", at = @At("HEAD"), cancellable = true, remap = false)
    private void enchantcompat$allowAnyEnchant(ItemStack stack, Enchantment enchantment,
            CallbackInfoReturnable<Boolean> cir) {
        if (enchantment == null || stack == null || stack.func_190926_b()) {
            cir.setReturnValue(Boolean.FALSE);
            return;
        }
        Map<Enchantment, Integer> disabled = ((ToolBase) (Object) this).getDisabledEnchants(stack);
        cir.setReturnValue(disabled == null || !disabled.containsKey(enchantment));
    }
}
