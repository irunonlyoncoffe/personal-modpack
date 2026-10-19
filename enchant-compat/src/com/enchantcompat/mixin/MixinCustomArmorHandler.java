package com.enchantcompat.mixin;

import com.brandon3055.draconicevolution.handlers.CustomArmorHandler;
import com.enchantcompat.LastStandCompat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * DE's {@code onPlayerDeath} runs at {@code EventPriority.HIGHEST} and spends RF
 * for its built-in last-stand before OpenBlocks Last Stand (XP) can matter.
 * Prefer the OpenBlocks enchant when present and affordable.
 */
@Mixin(value = CustomArmorHandler.class, remap = false)
public abstract class MixinCustomArmorHandler {

    @Inject(method = "onPlayerDeath", at = @At("HEAD"), cancellable = true)
    private void enchantcompat$preferOpenBlocksLastStand(LivingDeathEvent event, CallbackInfo ci) {
        if (event == null || event.isCanceled()) {
            return;
        }
        if (!(event.getEntityLiving() instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) event.getEntityLiving();
        if (LastStandCompat.trySaveWithXp(player)) {
            event.setCanceled(true);
            ci.cancel();
        }
    }
}
