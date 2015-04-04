package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BaseBlockWithSidedFacing;
import elec332.eflux.blocks.DirectionBlock;
import net.minecraft.block.material.Material;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new DirectionBlock().setCreativeTab(EFlux.CreativeTab);
            new BaseBlockWithSidedFacing(Material.rock, "test").setCreativeTab(EFlux.CreativeTab);
        }
    }
}
