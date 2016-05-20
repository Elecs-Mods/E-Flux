package elec332.eflux.multiblock;

import elec332.eflux.handler.FluidEnergyProviderHandler;
import elec332.eflux.util.IEFluxFluidHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class EFluxMultiBlockFluidGenerator extends EFluxMultiBlockGenerator implements IEFluxFluidHandler {

    public EFluxMultiBlockFluidGenerator(int capacity){
        fluidTank = new FluidTank(capacity);
    }

    @CapabilityInject(IEFluxFluidHandler.class)
    private static Capability<IEFluxFluidHandler> CAPABILITY;

    private FluidTank fluidTank;
    private int countDownTimer;
    private int powerTick;

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {

    }

    @Override
    public final void onTick() {
        countDownTimer--;
        if (countDownTimer <= 0){
            powerTick = 0;
            FluidEnergyProviderHandler.FluidBurnData burnData = modifyBurnData(FluidEnergyProviderHandler.instance.getPowerFromFluid(fluidTank.getFluid()));
            if (burnData == null){
                countDownTimer = 10;
                return;
            }
            float f = Math.min(((float)fluidTank.getFluidAmount()/burnData.fuelCost), 1);
            fluidTank.drain((int) (burnData.fuelCost * f), true);
            countDownTimer = (int) (burnData.burnTime * f);
            powerTick = (int) (burnData.powerPerTick * f);
        } else {
            generatePower(powerTick);
        }
    }

    protected abstract FluidEnergyProviderHandler.FluidBurnData modifyBurnData(FluidEnergyProviderHandler.FluidBurnData burnData);

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        NBTTagCompound fluid = new NBTTagCompound();
        fluidTank.writeToNBT(fluid);
        tagCompound.setTag("fluidStorage", fluid);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        fluidTank.readFromNBT(tagCompound.getCompoundTag("fluidStorage"));
    }

    /**
     * This method returns if the provided fluidStack is valid for this machine or not.
     *
     * @param stack The fluidStack trying to enter the machine
     * @return If the fluid can be accepted;
     */
    protected abstract boolean isFluidValid(FluidStack stack);

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (fluidTank.getFluid() == null || !isFluidValid(resource))
            return 0;
        return fluidTank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(Fluid fluid) {
        return true;
    }

    @Override
    public boolean canDrain(Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo() {
        return new FluidTankInfo[]{fluidTank.getInfo()};
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == CAPABILITY ? (T) this : super.getCapability(capability, facing, pos);
    }

}
