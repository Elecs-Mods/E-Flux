package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.eflux.items.Components;
import elec332.eflux.items.MultiMeter;
import elec332.eflux.items.Wrench;
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
        new MultiMeter("MultiMeter");
        wrench = new Wrench("Wrench");
        Components.init();
    }
}
