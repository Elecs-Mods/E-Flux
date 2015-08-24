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
        addSlots(player.inventory);
    }

    private IInventory bmh;

    private void addSlots(InventoryPlayer inventoryPlayer){

        addSlotToContainer(new Slot(bmh, 0, 66, 53));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
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
