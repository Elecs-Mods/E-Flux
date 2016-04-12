package elec332.eflux.api.util;

import elec332.eflux.util.IEFluxFluidHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 5-4-2016.
 */
public class CapabilityHelper {

    public static FluidHandlerHelper fromFluidTank(@Nonnull FluidTank tank){
        return new FluidHandlerHelper(tank);
    }

    public static FluidHandlerHelper forCapacity(int capacity){
        return new FluidHandlerHelper(capacity);
    }

    public static class FluidHandlerHelper implements IEFluxFluidHandler, INBTSerializable<NBTTagCompound> {

        public FluidHandlerHelper(int capacity){
            this(new FluidTank(capacity));
        }

        public FluidHandlerHelper(FluidTank tank){
            this.tank = tank;
        }

        private final FluidTank tank;

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return tank.fill(resource, doFill);
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (resource == null || !resource.isFluidEqual(tank.getFluid())) {
                return null;
            }
            return tank.drain(resource.amount, doDrain);
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return tank.drain(maxDrain, doDrain);
        }

        @Override
        public boolean canFill(Fluid fluid) {
            return true;
        }

        @Override
        public boolean canDrain(Fluid fluid) {
            return true;
        }

        @Override
        public FluidTankInfo[] getTankInfo() {
            return new FluidTankInfo[]{tank.getInfo()};
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tank.writeToNBT(tag);
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            tank.readFromNBT(nbt);
        }

    }

}
