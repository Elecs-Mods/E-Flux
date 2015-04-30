package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.eflux.items.MultiMeter;
import elec332.eflux.items.Wrench;
//import elec332.eflux.grid.power.itemsTEST.Charger;
import net.minecraft.item.Item;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ItemRegister {
    public static final ItemRegister instance = new ItemRegister();
    private ItemRegister(){
    }

    public static Item wrench;

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment){
            new MultiMeter("mm");
            //new Charger();
        }
        wrench = new Wrench("Wrench");
    }
}
