package elec332.eflux.init;

import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockFluid;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.items.ItemBucket;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.eventhandler.Event;
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
        petrol = registerFluid("Petrol");
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
