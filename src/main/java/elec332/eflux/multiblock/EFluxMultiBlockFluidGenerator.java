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
            FluidEnergyProviderHandler.FluidBurnData burnData = FluidEnergyProviderHandler.instance.getPowerFromFluid(fluidTank.getFluid());
            if (burnData == null){
                countDownTimer = 20;
                return;
            }
            float f = (float)fluidTank.getFluidAmount()/burnData.fuelCost;
            if (f < 1){
                fluidTank.drain((int) (f * burnData.fuelCost), true);
                countDownTimer = (int) (f * burnData.burnTime);
                generatePower((int) (f * burnData.powerProvided));
                return;
            }
            fluidTank.drain(burnData.fuelCost, true);
            countDownTimer = burnData.burnTime;
            generatePower(burnData.powerProvided);
        }
    }

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
        if (fluidTank.getFluid() == null && !isFluidValid(resource))
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
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == CAPABILITY || super.hasCapability(capability, facing, pos);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == CAPABILITY ? (T) this : super.getCapability(capability, facing, pos);
    }

}
