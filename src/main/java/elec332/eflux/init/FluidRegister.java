package elec332.eflux.init;

import elec332.eflux.client.EFluxResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * Created by Elec332 on 13-9-2015.
 */
public final class FluidRegister {

    public static final FluidRegister instance = new FluidRegister();
    private FluidRegister(){
        slib = new Fluid("Slib", new EFluxResourceLocation("slibFluid"), new EFluxResourceLocation("slibFluid"));
        oil = new Fluid("Oil", new EFluxResourceLocation("oilStill"), new EFluxResourceLocation("oilFlowing"));
        crudeOil = new Fluid("CrudeOil", new EFluxResourceLocation("crudeOil"), new EFluxResourceLocation("crudeOil"));
    }

    public static Fluid slib, oil, crudeOil;

    public void init(){
        FluidRegistry.registerFluid(slib);
        FluidRegistry.registerFluid(oil);
        FluidRegistry.registerFluid(crudeOil);
    }

}
