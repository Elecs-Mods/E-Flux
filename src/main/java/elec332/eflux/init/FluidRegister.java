package elec332.eflux.init;

import elec332.eflux.client.EFluxResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by Elec332 on 13-9-2015.
 */
public class FluidRegister {

    public static final FluidRegister instance = new FluidRegister();
    private FluidRegister(){
        slib = new Fluid("Slib", new EFluxResourceLocation("slibFluid"), new EFluxResourceLocation("slibFluid"));
    }

    public static Fluid slib;

    public void init(){
        FluidRegistry.registerFluid(slib);
    }

}
