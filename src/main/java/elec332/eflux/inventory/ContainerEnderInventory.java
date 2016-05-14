package elec332.eflux.inventory;

import elec332.core.inventory.BaseContainer;
import elec332.eflux.inventory.slot.SlotScrollableItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class ContainerEnderInventory extends BaseContainer {

    public ContainerEnderInventory(EntityPlayer player, IItemHandler itemHandler) {
        super(player);
        addPlayerInventoryToContainer();
        int offset = -75;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotScrollableItemHandler(itemHandler, j + i * 9 + 9, 8 + j * 18, (84 + offset) + i * 18));
            }
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack ret = super.slotClick(slotId, dragType, clickTypeIn, player);
        this.detectAndSendChanges();
        return ret;
    }

}
