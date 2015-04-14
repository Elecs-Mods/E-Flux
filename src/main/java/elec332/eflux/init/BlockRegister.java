package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BaseBlockWithSidedFacing;
import elec332.eflux.blocks.DirectionBlock;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.material.Material;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();
    private BlockRegister(){
    }

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new DirectionBlock().setCreativeTab(EFlux.CreativeTab);
            new BaseBlockWithSidedFacing(Material.rock, "test").setCreativeTab(EFlux.CreativeTab);
        }
        //new Grinder(Material.rock, "Grinder");
        for (EnumMachines machine : EnumMachines.values()){
            machine.init();
        }
    }
}
