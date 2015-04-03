package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();

    public void preInit(FMLPreInitializationEvent event){
        //Early block registering
    }

    public void init(FMLInitializationEvent event){
        //Normal block registering
    }
}
