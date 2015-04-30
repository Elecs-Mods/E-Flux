package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.DirectionBlock;
import elec332.eflux.util.EnumMachines;

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
        }

        for (EnumMachines machine : EnumMachines.values()){
            machine.init();
        }
    }
}
