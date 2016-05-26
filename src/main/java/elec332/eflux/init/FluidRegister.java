package elec332.eflux.init;

import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockFluid;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.items.ItemBucket;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.awt.*;

/**
 * Created by Elec332 on 13-9-2015.
 */
public final class FluidRegister {

    public static final FluidRegister instance = new FluidRegister();
    private FluidRegister(){
        slib =registerFluid("slib");
        oil = registerFluid("oil", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), Color.BLACK.getRGB());
        crudeOil = registerFluid("crudeOil");
        lubicrant = registerFluid("lubicrant");
        fuel = registerFluid("fuel");
        diesel = registerFluid("diesel");
        petrol = registerFluid("petrol");
        gas = registerFluid("lpg");
        brine = registerFluid("brine");
    }

    public static Fluid slib, oil, crudeOil, brine; //Pre-Refinery
    public static Fluid lubicrant, fuel, diesel, petrol, gas; //Post-refinery

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
        final Block block = GameRegistry.register(new BlockFluid(ret).setCreativeTab(EFlux.creativeTab));
        ret.setBlock(block);
        GameRegistry.register(new ItemBucket((BlockFluid) block).setCreativeTab(EFlux.creativeTab));
        return ret;
    }

    public void init(){
        //((BlockFluidClassic)oil.getBlock()).setRenderLayer(BlockRenderLayer.SOLID);
        //FluidRegistry.registerFluid(slib);
       // FluidRegistry.registerFluid(oil);
        //FluidRegistry.registerFluid(crudeOil);
    }

}
