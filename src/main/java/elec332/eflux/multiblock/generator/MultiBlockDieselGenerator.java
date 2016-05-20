package elec332.eflux.multiblock.generator;

import elec332.eflux.handler.FluidEnergyProviderHandler;
import elec332.eflux.multiblock.EFluxMultiBlockFluidGenerator;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Elec332 on 13-9-2015.
 */
public class MultiBlockDieselGenerator extends EFluxMultiBlockFluidGenerator {

    public MultiBlockDieselGenerator() {
        super(12000);
    }

    @Override
    protected FluidEnergyProviderHandler.FluidBurnData modifyBurnData(FluidEnergyProviderHandler.FluidBurnData burnData) {
        return burnData;
    }

    /**
     * This method returns if the provided fluidStack is valid for this machine or not.
     *
     * @param stack The fluidStack trying to enter the machine
     * @return If the fluid can be accepted;
     */
    @Override
    protected boolean isFluidValid(FluidStack stack) {
        return true;
    }

    /**
     * This returns the maximum amount of power that can be drawn from 1 request,
     * remember, this not the amount of EF or RP, but RP * EF.
     *
     * @return The maximum amount of power allowed to be drawn.
     */
    @Override
    public int getMaxProvidedPower() {
        return 2000;
    }

    /**
     * This returns the maximum amount of power that can be stored, but remember:
     * This value should be low, only a buffer for a couple of ticks of generated power.
     *
     * @return the maximum amount of power that can be stored.
     */
    @Override
    public int maxStoredPower() {
        return 10000;
    }

}
