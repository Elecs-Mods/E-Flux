package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
import elec332.eflux.items.Breaker;
import elec332.eflux.items.Components;
import elec332.eflux.items.MultiMeter;
import elec332.eflux.items.Wrench;
import elec332.eflux.tileentity.BreakableMachineTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ItemRegister {
    public static final ItemRegister instance = new ItemRegister();
    private ItemRegister(){
    }

    public static Item wrench, multimeter;

    public void init(FMLInitializationEvent event){
        if (ElecCore.developmentEnvironment)
            new Breaker();
        multimeter = new MultiMeter("MultiMeter");
        wrench = new Wrench("Wrench");
        Components.init();
    }
}
