package elec332.eflux.inventory;

import elec332.core.inventory.BaseContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class ContainerSingleSlot extends BaseContainer {  //Deprecated???

    public ContainerSingleSlot(IInventory handler, EntityPlayer player){
        super(player);
        this.bmh = handler;
        addSlots();
    }

    private IInventory bmh;

    private void addSlots(){

        addSlotToContainer(new Slot(bmh, 0, 66, 53));

        addPlayerInventoryToContainer();
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        bmh.closeInventory();
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
