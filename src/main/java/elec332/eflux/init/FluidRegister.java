package elec332.eflux.init;

import elec332.core.util.RegistryHelper;
import elec332.eflux.blocks.BlockFluid;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.awt.*;

/**
 * Created by Elec332 on 13-9-2015.
 */
public final class FluidRegister {

    public static Fluid slib, oil, crudeOil, brine; //Pre-Refinery
    public static Fluid lubicrant, fuel, diesel, petrol, gas; //Post-refinery

    public static void init(){
        slib =registerFluid("slib", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), new Color(139, 69, 19).getRGB());
        oil = registerFluid("oil", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), Color.BLACK.getRGB());
        crudeOil = registerFluid("crudeOil", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), Color.BLACK.getRGB());
        lubicrant = registerFluid("lubicrant", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), Color.BLACK.getRGB());
        fuel = registerFluid("fuel", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), new Color(255, 140, 0).getRGB());
        diesel = registerFluid("diesel", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), new Color(255, 165, 0).getRGB());
        petrol = registerFluid("petrol", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), Color.BLACK.getRGB());
        gas = registerFluid("lpg", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), Color.WHITE.getRGB());
        brine = registerFluid("brine", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), new Color(205, 133, 63).getRGB());
    }

    private static Fluid registerFluid(String name){
        return registerFluid(name, new EFluxResourceLocation(name), new EFluxResourceLocation(name));
    }

    private static Fluid registerFluid(String name, ResourceLocation still, ResourceLocation flowing){
        return registerFluid(name, still, flowing, 0xFFFFFFFF);
    }

    private static Fluid registerFluid(String name, ResourceLocation still, ResourceLocation flowing, final int color){
        Fluid ret = new Fluid(name, still, flowing){
            @Override
            public int getColor() {
                return color;
            }
        };
        FluidRegistry.registerFluid(ret);
        final Block block = RegistryHelper.register(new BlockFluid(ret));
        ret.setBlock(block);
        FluidRegistry.addBucketForFluid(ret);
        //GameRegistry.register(new ItemBucket((BlockFluid) block).setCreativeTab(EFlux.creativeTab));
        return ret;
    }

}
