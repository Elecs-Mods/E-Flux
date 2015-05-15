package elec332.eflux.init;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import elec332.core.main.ElecCore;
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
            new MultiMeter("Breaker"){  //DEV test item, on right click, breaks the clicked machine
                @Override
                public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
                    TileEntity tile = world.getTileEntity(z, y, z);
                    if (tile instanceof BreakableMachineTile)
                        ((BreakableMachineTile)tile).setBroken(true);
                    return false;
                }
            };
        multimeter = new MultiMeter("MultiMeter");
        wrench = new Wrench("Wrench");
        Components.init();
    }
}
