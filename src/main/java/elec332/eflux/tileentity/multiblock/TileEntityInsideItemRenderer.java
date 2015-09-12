package elec332.eflux.tileentity.multiblock;

import elec332.eflux.util.RIWInventory;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 5-9-2015.
 */
public class TileEntityInsideItemRenderer extends TileMultiBlockTile{

    public TileEntityInsideItemRenderer(){
        this.inventory = new RIWInventory(0);
    }

    private RIWInventory inventory;

    @Override
    public void updateEntity() {
        if (timeCheck() && getMultiBlock() instanceof RIWInventory.IRIWInventoryContainer){
            syncInventoryContents(((RIWInventory.IRIWInventoryContainer)getMultiBlock()).getInventory());
        }
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    public RIWInventory getInventory() {
        return this.inventory;
    }

    public void syncInventoryContents(RIWInventory inventory){
        sendPacket(3, inventory.getPacketData());
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        if (id == 3) {
            inventory.readFromNBT(tag);
            return;
        }
        super.onDataPacket(id, tag);
    }

}
