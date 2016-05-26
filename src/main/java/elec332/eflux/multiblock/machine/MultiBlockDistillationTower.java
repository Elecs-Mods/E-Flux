package elec332.eflux.multiblock.machine;

import com.google.common.collect.Lists;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.heat.IHeatReceiver;
import elec332.eflux.api.util.CapabilityHelper;
import elec332.eflux.util.Config;
import elec332.eflux.util.IEFluxFluidHandler;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

import static elec332.eflux.init.FluidRegister.*;

/**
 * Created by Elec332 on 1-4-2016.
 */
public class MultiBlockDistillationTower extends AbstractMultiBlock implements IHeatReceiver {

    @CapabilityInject(IEFluxFluidHandler.class)
    private static Capability<IEFluxFluidHandler> CAPABILITY;

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {
        oilTank = CapabilityHelper.forCapacity(6000);   // Input Tank
        setPositions();
        lubeTank = CapabilityHelper.forCapacity(500);  // 2%
        fuelTank = CapabilityHelper.forCapacity(5000);  // 49%
        dieselTank = CapabilityHelper.forCapacity(4000);// 30%
        petrolTank = CapabilityHelper.forCapacity(2000);// 12%
        gasTank = CapabilityHelper.forCapacity(1000);   // 7%
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

    private CapabilityHelper.FluidHandlerHelper oilTank;
    private CapabilityHelper.FluidHandlerHelper lubeTank, fuelTank, dieselTank, petrolTank, gasTank;
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
        } else {
            heat = 0;
        }
        if (getWorldObj().getWorldTime() % 20 == 0 && heat > Config.MultiBlocks.DistillationTower.requiredheat){
            FluidStack stack = oilTank.drain(100, false);
            if (stack == null || stack.amount != 100){
                return;
            }
            heat -= Config.MultiBlocks.DistillationTower.requiredheat;
            oilTank.drain(100, true);
            attemptFill(lubeTank, 2, lubicrant);
            attemptFill(fuelTank, 49, fuel);
            attemptFill(dieselTank, 30, diesel);
            attemptFill(petrolTank, 12, petrol);
            attemptFill(gasTank, 7, gas);
        }
        backup = heat;
    }

    private void attemptFill(CapabilityHelper.FluidHandlerHelper tank, int fill, Fluid fluid){
        int i = tank.fill(new FluidStack(fluid, fill), true);
        if (i != fill){
            BlockPos boomPos = getBlockLocAtTranslatedPos(1, 2, 5);
            WorldHelper.spawnExplosion(getWorldObj(), boomPos.getX(), boomPos.getY(), boomPos.getZ(), 5f);
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
        if (capability == CAPABILITY){
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
    public List<String> getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag != null){
            if (tag.hasKey("h")){
                currentTip.add("Heat: "+tag.getInteger("h"));
            }
            if (tag.hasKey("o")){
                currentTip.add("Oil: "+tag.getInteger("o")+"mB");
            }
        }
        return super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public NBTTagCompound getWailaTag(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {
        if (tag != null){
            tag.setInteger("h", backup);
            if (heatInputs.contains(pos)){
                FluidStack fluid = oilTank.getTankInfo()[0].fluid;
                tag.setInteger("o", fluid == null ? 0 : fluid.amount);
            }
        }
        return super.getWailaTag(player, te, tag, world, pos);
    }

}
