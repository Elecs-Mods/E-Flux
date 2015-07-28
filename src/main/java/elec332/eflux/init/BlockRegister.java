package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockOres;
import elec332.eflux.blocks.DirectionBlock;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.Block;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();
    private BlockRegister(){
    }

    public static Block ores;

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new DirectionBlock().setCreativeTab(EFlux.creativeTab);
        }

        for (EnumMachines machine : EnumMachines.values()){
            machine.init();
        }
        ores = new BlockOres().register().setCreativeTab(EFlux.creativeTab);

    }
}
