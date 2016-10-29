package elec332.eflux.multiblock;

import elec332.core.util.FluidTankWrapper;
import elec332.eflux.handler.FluidEnergyProviderHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class EFluxMultiBlockFluidGenerator extends EFluxMultiBlockGenerator {

    public EFluxMultiBlockFluidGenerator(int capacity){
        final FluidTank fluidT = new FluidTank(capacity);
        fluidTank = new FluidTankWrapper() {

            @Override
            protected IFluidTank getTank() {
                return fluidT;
            }

            @Override
            protected boolean canFillFluidType(FluidStack fluidStack) {
                return isFluidValid(fluidStack);
            }

            @Override
            protected boolean canDrainFluidType(FluidStack fluidStack) {
                return isFluidValid(fluidStack);
            }

        };
    }

    private FluidTankWrapper fluidTank;
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
        NBTTagCompound fluid = fluidTank.serializeNBT();
        tagCompound.setTag("fluidStorage", fluid);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        fluidTank.deserializeNBT(tagCompound.getCompoundTag("fluidStorage"));
    }

    /**
     * This method returns if the provided fluidStack is valid for this machine or not.
     *
     * @param stack The fluidStack trying to enter the machine
     * @return If the fluid can be accepted;
     */
    protected abstract boolean isFluidValid(FluidStack stack);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ? (T) this : super.getCapability(capability, facing, pos);
    }

}
