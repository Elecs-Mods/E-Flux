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
        if (stack == null) {
            return null;
        }
        FluidBurnData ret = getPowerFromFluid(stack.getFluid());
        if (ret == null){
            return null;
        }
        if (ret.fuelCost > stack.amount){
            float diff = ((float) stack.amount) / ((float) ret.fuelCost);
            return ret.modify(diff, diff, diff);
        }
        return ret;
    }

    private FluidBurnData getPowerFromFluid(Fluid fluid){
        if (fluid == null) {
            return null;
        }
        return burnDataMap.get(fluid.getName().toLowerCase());
    }

    public void registerFuelData(String fluid, int fuelCost, int powerProvided){
        registerFuelData(fluid, fuelCost, 20, powerProvided);
    }

    public void registerFuelData(String fluid, int fuelCost, int burnTime, int powerProvided){
        if (burnDataMap.get(fluid) != null) {
            throw new IllegalArgumentException("FuelData for \"" + fluid + "\" is already registered!");
        }
        fluid = fluid.toLowerCase();
        burnDataMap.put(fluid, new FluidBurnData(fluid, fuelCost, burnTime, powerProvided));
    }

    public static class FluidBurnData {

        public FluidBurnData(String fluid, int fuelCost, int burnTime, int powerProvided){
            this.fuelCost = fuelCost;
            this.burnTime = burnTime;
            this.powerPerTick = powerProvided;
            this.fluid = fluid;
        }

        public final int fuelCost, burnTime, powerPerTick;
        public final String fluid;

        public FluidBurnData modify(float fuel, float burn, float power){
            return new FluidBurnData(fluid, (int) (fuelCost * fuel), (int) (burnTime * burn), (int) (powerPerTick * power));
        }

    }

}
