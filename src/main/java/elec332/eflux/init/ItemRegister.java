package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ItemRegister {
    public static final ItemRegister instance = new ItemRegister();

    public void preInit(FMLPreInitializationEvent event){
        //Early item registering
    }

    public void init(FMLInitializationEvent event){
        //Normal item registering
    }
}
