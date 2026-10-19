package com.enchantcompat;

import java.lang.reflect.Method;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Bridges OpenBlocks Last Stand (spend XP to survive) so it can win over
 * Draconic Evolution's RF last-stand on {@code LivingDeathEvent}.
 */
public final class LastStandCompat {

    private static boolean resolved;
    private static Method countLevels;
    private static Method getPlayerXP;
    private static Method addPlayerXP;

    private LastStandCompat() {
    }

    /**
     * @return true if the player was saved (HP set to 1, XP spent, caller should cancel death)
     */
    public static boolean trySaveWithXp(EntityPlayer player) {
        if (player == null || !resolve()) {
            return false;
        }
        try {
            int levels = ((Integer) countLevels.invoke(null, (EntityLivingBase) player)).intValue();
            if (levels <= 0) {
                return false;
            }
            int xp = ((Integer) getPlayerXP.invoke(null, player)).intValue();
            // Default OpenBlocks formula for an exactly-lethal hit: max(1, 50*(1-0)/ench)
            float cost = Math.max(1.0F, 50.0F / levels);
            if (xp < cost) {
                return false;
            }
            player.func_70606_j(1.0F);
            addPlayerXP.invoke(null, player, Integer.valueOf(-((int) cost)));
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private static boolean resolve() {
        if (resolved) {
            return countLevels != null;
        }
        resolved = true;
        try {
            Class<?> handler = Class.forName("openblocks.enchantments.LastStandEnchantmentsHandler");
            Class<?> utils = Class.forName("openmods.utils.EnchantmentUtils");
            countLevels = handler.getMethod("countLastStandEnchantmentLevels", EntityLivingBase.class);
            getPlayerXP = utils.getMethod("getPlayerXP", EntityPlayer.class);
            addPlayerXP = utils.getMethod("addPlayerXP", EntityPlayer.class, int.class);
            return true;
        } catch (Throwable t) {
            countLevels = null;
            getPlayerXP = null;
            addPlayerXP = null;
            return false;
        }
    }
}
