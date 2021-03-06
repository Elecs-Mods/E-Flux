package elec332.eflux.multiblock.machine;

import com.google.common.collect.Lists;
import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInformation;
import elec332.core.main.ElecCore;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.util.FluidTankWrapper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.heat.IHeatReceiver;
import elec332.eflux.util.Config;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import java.util.List;

import static elec332.eflux.init.FluidRegister.*;

/**
 * Created by Elec332 on 1-4-2016.
 */
public class MultiBlockDistillationTower extends AbstractMultiBlock implements IHeatReceiver {

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {
        oilTank = FluidTankWrapper.withCapacity(6000);   // Input Tank
        setPositions();
        lubeTank = FluidTankWrapper.withCapacity(500);  // 2%
        fuelTank = FluidTankWrapper.withCapacity(5000);  // 49%
        dieselTank = FluidTankWrapper.withCapacity(4000);// 30%
        petrolTank = FluidTankWrapper.withCapacity(2000);// 12%
        gasTank = FluidTankWrapper.withCapacity(1000);   // 7%
    }

    private void setPositions(){
        heatInputs = Lists.newArrayList();
        oilIn1 = getBlockLocAtTranslatedPos(0, 0, 2);
        oilIn2 = getBlockLocAtTranslatedPos(2, 0, 2);
        lubeOut = getBlockLocAtTranslatedPos(1, 2, 0);
        gasOut = getBlockLocAtTranslatedPos(1, 2, 6);
        BlockPos lmb = getBlockLocAtTranslatedPos(1, 3, 0);
        petrolOut = lmb.up(5);
        dieselOut = lmb.up(3);
        fuelOut = lmb.up();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                heatInputs.add(getBlockLocAtTranslatedPos(i, 0, j));
            }
        }
    }

    private FluidTankWrapper oilTank;
    private FluidTankWrapper lubeTank, fuelTank, dieselTank, petrolTank, gasTank;
    private int heat, backup;
    private BlockPos oilIn1, oilIn2, lubeOut, gasOut, petrolOut, fuelOut, dieselOut;
    private List<BlockPos> heatInputs;

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setTag("oil", oilTank.serializeNBT());
        tagCompound.setTag("lube", lubeTank.serializeNBT());
        tagCompound.setTag("fuel", fuelTank.serializeNBT());
        tagCompound.setTag("diesel", dieselTank.serializeNBT());
        tagCompound.setTag("petrol", petrolTank.serializeNBT());
        tagCompound.setTag("gas", gasTank.serializeNBT());
        tagCompound.setInteger("heat", heat);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        oilTank.deserializeNBT(tagCompound.getCompoundTag("oil"));
        lubeTank.deserializeNBT(tagCompound.getCompoundTag("lube"));
        fuelTank.deserializeNBT(tagCompound.getCompoundTag("fuel"));
        dieselTank.deserializeNBT(tagCompound.getCompoundTag("diesel"));
        petrolTank.deserializeNBT(tagCompound.getCompoundTag("petrol"));
        gasTank.deserializeNBT(tagCompound.getCompoundTag("gas"));
        heat = tagCompound.getInteger("heat");
    }

    /**
     * This gets run server-side only
     */
    @Override
    public void onTick() {
        if (heat >= Config.MultiBlocks.DistillationTower.heatDispersion) { //Heat dispersion
            heat -= Config.MultiBlocks.DistillationTower.heatDispersion;
        } else if (heat != 0){
            heat = 0;
        }
        if (getWorldObj().getWorldTime() % 60 == 0 && heat > Config.MultiBlocks.DistillationTower.minHeat){
            FluidStack stack = oilTank.drain(100, false);
            if (stack == null || stack.amount != 100){
                return;
            }
            heat -= Config.MultiBlocks.DistillationTower.requiredHeat;
            oilTank.drain(100, true);
            attemptFill(lubeTank, 2, lubicrant);
            attemptFill(fuelTank, 49, fuel);
            attemptFill(dieselTank, 30, diesel);
            attemptFill(petrolTank, 12, petrol);
            attemptFill(gasTank, 7, gas);
        }
        backup = heat;
    }

    private void attemptFill(FluidTankWrapper tank, int fill, Fluid fluid){
        int i = tank.fill(new FluidStack(fluid, fill), true);
        if (i != fill && !getWorldObj().isRemote){
            BlockPos boomPos = getBlockLocAtTranslatedPos(1, 2, 5);
            ElecCore.tickHandler.registerCall(() -> WorldHelper.spawnExplosion(getWorldObj(), boomPos.getX(), boomPos.getY(), boomPos.getZ(), 5f), getWorldObj());
        }
    }

    /**
     * Invalidate your multiblock here
     */
    @Override
    public void invalidate() {

    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == EFluxAPI.HEAT_CAPABILITY && heatInputs.contains(pos) || super.hasCapability(capability, facing, pos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        if (capability == EFluxAPI.HEAT_CAPABILITY && heatInputs.contains(pos)){
            return (T) this;
        }
        return super.getCapability(capability, facing, pos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            if (pos.equals(oilIn1) || pos.equals(oilIn2)){
                return (T) oilTank;
            } else if (pos.equals(fuelOut)){
                return (T) fuelTank;
            } else if (pos.equals(dieselOut)){
                return (T) dieselTank;
            } else if (pos.equals(petrolOut)){
                return (T) petrolTank;
            } else if (pos.equals(gasOut)){
                return (T) gasTank;
            } else if (pos.equals(lubeOut)){
                return (T) lubeTank;
            }
            return null;
        }
        return super.getSpecialCapability(capability, facing, pos);
    }

    @Override
    public int getHeat() {
        return this.heat;
    }

    @Override
    public void addHeat(int heat) {
        this.heat += heat;
    }

    @Override
    public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
        NBTTagCompound tag = hitData.getData();
        if (tag.hasKey("h")){
            information.addInformation("Heat: "+tag.getInteger("h"));
        }
        if (tag.hasKey("o")){
            information.addInformation("Oil: "+tag.getInteger("o")+"mB");
        }
        super.addInformation(information, hitData);
    }

    @Nonnull
    @Override
    public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
        tag.setInteger("h", backup);
        if (heatInputs.contains(hitData.getPos())){
            FluidStack fluid = oilTank.getTankProperties()[0].getContents();
            tag.setInteger("o", fluid == null ? 0 : fluid.amount);
        }
        return super.getInfoNBTData(tag, tile, player, hitData);
    }

}
