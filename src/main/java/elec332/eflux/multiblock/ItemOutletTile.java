package elec332.eflux.multiblock;

import elec332.core.multiblock.AbstractMultiBlockTile;
import elec332.core.world.WorldHelper;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 28-7-2015.
 */
public class ItemOutletTile extends AbstractMultiBlockTile {

    public void dropStack(ItemStack stack){
        WorldHelper.dropStack(worldObj, myLocation().atSide(getFacing()), stack);
    }

}
