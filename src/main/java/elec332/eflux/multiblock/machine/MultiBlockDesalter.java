package elec332.eflux.multiblock.machine;

import elec332.core.main.ElecCore;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.util.FluidTankWrapper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.heat.IHeatReceiver;
import elec332.eflux.util.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

import static elec332.eflux.init.FluidRegister.brine;
import static elec332.eflux.init.FluidRegister.crudeOil;

/**
 * Created by Elec332 on 15-4-2016.
 */
public class MultiBlockDesalter extends AbstractMultiBlock implements IHeatReceiver {

    @Override
    public void init() {
        setPositions();
        oilTank = FluidTankWrapper.withCapacity(2000);
        crudeOilTank = FluidTankWrapper.withCapacity(500);
        waterTank = FluidTankWrapper.withCapacity(1000);
        brineTank = FluidTankWrapper.withCapacity(600);
    }

    private void setPositions(){
        oilIn = getBlockLocAtTranslatedPos(1, 0, 0);
        oilOut = getBlockLocAtTranslatedPos(1, 1, 0);
        waterIn = getBlockLocAtTranslatedPos(0, 0, 1);
        brineOut = getBlockLocAtTranslatedPos(1, 1, 2);
    }

    private BlockPos oilIn, oilOut, waterIn, brineOut;
    private FluidTankWrapper oilTank, crudeOilTank, waterTank, brineTank;

    private int heat;

    @Override
    public void onTick() {
        if (heat >= Config.MultiBlocks.Desalter.heatDispersion) {
            heat -= Config.MultiBlocks.Desalter.heatDispersion;
        } else if (heat != 0){
            heat = 0;
        }
        if (getWorldObj().getWorldTime() % 80 == 0 && heat >= Config.MultiBlocks.Desalter.minHeat){
            FluidStack drained = oilTank.drain(20, false);
            if (drained == null || drained.amount != 20){
                return;
            }
            drained = waterTank.drain(20, false);
            if (drained == null || drained.amount != 20){
                return;
            }
            heat -= Config.MultiBlocks.Desalter.requiredHeat;
            oilTank.drain(20, true);
            waterTank.drain(25, true);
            attemptFill(crudeOilTank, 16, crudeOil);
            attemptFill(brineTank, 20, brine);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("oil", oilTank.serializeNBT());
        tagCompound.setTag("crudeOil", crudeOilTank.serializeNBT());
        tagCompound.setTag("water", waterTank.serializeNBT());
        tagCompound.setTag("brine", brineTank.serializeNBT());
        tagCompound.setInteger("heat", heat);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        oilTank.deserializeNBT(tagCompound.getCompoundTag("oil"));
        crudeOilTank.deserializeNBT(tagCompound.getCompoundTag("crudeOil"));
        waterTank.deserializeNBT(tagCompound.getCompoundTag("water"));
        brineTank.deserializeNBT(tagCompound.getCompoundTag("brine"));
        heat = tagCompound.getInteger("heat");
    }

    private void attemptFill(IFluidHandler tank, int fill, Fluid fluid){
        int i = tank.fill(new FluidStack(fluid, fill), true);
        if (i != fill){
            final BlockPos boomPos = getBlockLocAtTranslatedPos(1, 1, 3);
            ElecCore.tickHandler.registerCall(new Runnable() {
                @Override
                public void run() {
                    WorldHelper.spawnExplosion(getWorldObj(), boomPos.getX(), boomPos.getY(), boomPos.getZ(), 3f);
                }
            }, getWorldObj());
        }
    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == EFluxAPI.HEAT_CAPABILITY && pos.getY() == oilIn.getY() || super.hasCapability(capability, facing, pos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        if (capability == EFluxAPI.HEAT_CAPABILITY){
            if (pos.getY() == oilIn.getY()){
                return (T) this;
            }
        }
        return super.getCapability(capability, facing, pos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            if (pos.equals(oilIn)){
                return (T) oilTank;
            }
            if (pos.equals(oilOut)){
                return (T) crudeOilTank;
            }
            if (pos.equals(waterIn)){
                return (T) waterTank;
            }
            if (pos.equals(brineOut)){
                return (T) brineTank;
            }
        }
        return super.getSpecialCapability(capability, facing, pos);
    }

    @Override
    public int getHeat() {
        return heat;
    }

    @Override
    public void addHeat(int heat) {
        this.heat += heat;
    }

}
