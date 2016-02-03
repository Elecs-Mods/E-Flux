package elec332.eflux.client;

import elec332.eflux.multiblock.machine.MultiBlockFurnace;
import elec332.eflux.tileentity.multiblock.TileMultiBlockTile;
import net.minecraft.inventory.IInventory;

/**
 * Created by Elec332 on 25-1-2016.
 */
public class FurnaceRenderTile extends TileMultiBlockTile {

    public IInventory getInv(){
        return ((MultiBlockFurnace)getMultiBlock()).getInventory();
    }

}
