package elec332.eflux.handler;

import com.google.common.collect.Maps;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;

/**
 * Created by Elec332 on 13-9-2015.
 */
public class FluidEnergyProviderHandler {

    public static final FluidEnergyProviderHandler instance = new FluidEnergyProviderHandler();
    private FluidEnergyProviderHandler(){
        burnDataMap = Maps.newHashMap();
    }

    private Map<String, FluidBurnData> burnDataMap;

    public FluidBurnData getPowerFromFluid(FluidStack stack){
        if (stack == null)
            return null;
        return getPowerFromFluid(stack.getFluid());
    }

    public FluidBurnData getPowerFromFluid(Fluid fluid){
        if (fluid == null)
            return null;
        return burnDataMap.get(fluid.getName());
    }

    public void registerFuelData(String fluid, int fuelCost, int powerProvided){
        registerFuelData(fluid, fuelCost, 20, powerProvided);
    }

    public void registerFuelData(String fluid, int fuelCost, int burnTime, int powerProvided){
        if (burnDataMap.get(fluid) != null)
            throw new IllegalArgumentException("FuelData for \""+fluid+"\" is already registered!");
        burnDataMap.put(fluid, new FluidBurnData(fuelCost, burnTime, powerProvided));
    }

    public static class FluidBurnData{

        public FluidBurnData(int fuelCost, int burnTime, int powerProvided){
            this.fuelCost = fuelCost;
            this.burnTime = burnTime;
            this.powerProvided = powerProvided;
        }

        public final int fuelCost, burnTime, powerProvided;

    }

}
