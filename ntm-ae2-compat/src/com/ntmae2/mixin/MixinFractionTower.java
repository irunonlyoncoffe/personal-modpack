package com.ntmae2.mixin;

import appeng.api.implementations.tiles.ICraftingMachine;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import com.hbm.inventory.fluid.FluidType;
import com.hbm.inventory.fluid.tank.FluidTankNTM;
import com.hbm.inventory.recipes.FractionRecipes;
import com.hbm.tileentity.machine.oil.TileEntityMachineFractionTower;
import com.ntmae2.MachinePatterns;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityMachineFractionTower.class, remap = false)
public abstract class MixinFractionTower implements ICraftingMachine {
    @Shadow
    public FluidTankNTM[] tanks;

    @Override
    public boolean acceptsPlans() {
        return MachinePatterns.fluidAccepts(tanks[0]);
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails details, InventoryCrafting inv, EnumFacing face) {
        return MachinePatterns.pushMappedFluid(
                (TileEntity) (Object) this,
                tanks[0],
                0,
                null,
                details,
                inv,
                new MachinePatterns.FluidLookup() {
                    @Override
                    public boolean valid(FluidType type) {
                        return MachinePatterns.knownFluid(type, FractionRecipes.fractions);
                    }

                    @Override
                    public FluidType fromOutput(ItemStack stack) {
                        return null;
                    }
                });
    }
}
