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
        slib =registerFluid("Slib");
        oil = registerFluid("Oil");
        crudeOil = registerFluid("CrudeOil");
        lubicrant = registerFluid("Lubicrant");
        fuel = registerFluid("Fuel");
        diesel = registerFluid("Diesel");
        petrol = registerFluid("Petrol");
        gas = registerFluid("Gas");
        brine = registerFluid("Brine");
    }

    public static Fluid slib, oil, crudeOil, brine; //Pre-Refinery
    public static Fluid lubicrant, fuel, diesel, petrol, gas; //Post-refinery

    private static Fluid registerFluid(String name){
        String s = name.substring(0, 1).toLowerCase() + name.substring(1);
        Fluid ret = new Fluid(name, new EFluxResourceLocation(s), new EFluxResourceLocation(s));
        FluidRegistry.registerFluid(ret);
        return ret;
    }

    public void init(){
        //FluidRegistry.registerFluid(slib);
       // FluidRegistry.registerFluid(oil);
        //FluidRegistry.registerFluid(crudeOil);
    }

}
