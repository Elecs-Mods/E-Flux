package elec332.eflux.util;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

/**
 * Created by Elec332 on 5-4-2016.
 *
 * This is here to make sure we don't interfere with Forge's
 * IFluidHandler Capability when that gets added.
 * (Interface mostly copied from Forge)
 */
public interface IEFluxFluidHandler {

    /**
     * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param resource
     *            FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param doFill
     *            If false, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    int fill(FluidStack resource, boolean doFill);

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * @param resource
     *            FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param doDrain
     *            If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     *         simulated) drained.
     */
    FluidStack drain(FluidStack resource, boolean doDrain);

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     *
     * This method is not Fluid-sensitive.
     *
     * @param maxDrain
     *            Maximum amount of fluid to drain.
     * @param doDrain
     *            If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     *         simulated) drained.
     */
    FluidStack drain(int maxDrain, boolean doDrain);

    /**
     * Returns true if the given fluid can be inserted into the given direction.
     *
     * More formally, this should return true if fluid is able to enter from the given direction.
     */
    boolean canFill(Fluid fluid);

    /**
     * Returns true if the given fluid can be extracted from the given direction.
     *
     * More formally, this should return true if fluid is able to leave from the given direction.
     */
    boolean canDrain(Fluid fluid);

    /**
     * Returns an array of objects which represent the internal tanks. These objects cannot be used
     * to manipulate the internal tanks. See {@link net.minecraftforge.fluids.FluidTankInfo}.
     *
     * @return Info for the relevant internal tanks.
     */
    FluidTankInfo[] getTankInfo();

}
