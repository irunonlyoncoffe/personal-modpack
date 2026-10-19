package net.minecraftforge.event.entity.living;

import net.minecraft.entity.EntityLivingBase;

public class LivingDeathEvent {
    public EntityLivingBase getEntityLiving() { return null; }
    public boolean isCanceled() { return false; }
    public void setCanceled(boolean cancel) {}
}
