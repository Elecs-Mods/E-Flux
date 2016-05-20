package elec332.eflux.inventory;

import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.eflux.inventory.slot.SlotScrollableItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class ContainerEnderInventory extends BaseContainer implements WidgetButton.IButtonEvent {

    public ContainerEnderInventory(EntityPlayer player, IItemHandler itemHandler) {
        super(player);
        this.scrollableSlots = new SlotScrollableItemHandler[27];
        this.itemHandler = itemHandler;
        this.addPlayerInventoryToContainer();
        int offset = -67;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int index = j + i * 9;
                this.scrollableSlots[index] = ((SlotScrollableItemHandler) addSlotToContainer(new SlotScrollableItemHandler(itemHandler, index, 8 + j * 18, (84 + offset) + i * 18)));
            }
        }
        this.up = addWidget(new WidgetButtonArrow(7, 4, WidgetButtonArrow.Direction.UP)).addButtonEvent(this);
        this.down = addWidget(new WidgetButtonArrow(7, 70, WidgetButtonArrow.Direction.DOWN)).addButtonEvent(this);
        this.up.setActive(false);
        this.down.setActive(itemHandler.getSlots() > 27);
    }

    private IItemHandler itemHandler;
    private final SlotScrollableItemHandler[] scrollableSlots;
    private final WidgetButton up, down;
    private int idx;

    @Override
    public void onButtonClicked(WidgetButton widgetButton) {
        if (widgetButton == up){
            idx--;
            if (27 + idx * 9 < itemHandler.getSlots()){
                down.setActive(true);
            }
            if (idx <= 0){
                up.setActive(false);
            }
            checkSlots();
        } else if (widgetButton == down){
            if (!up.isActive()){
                up.setActive(true);
            }
            idx++;
            if (27 + idx * 9 >= itemHandler.getSlots()){
                down.setActive(false);
            }
            checkSlots();
        }
    }

    private void checkSlots(){
        for (int i = 0; i < scrollableSlots.length; i++) {
            scrollableSlots[i].setIndex(i + idx * 9);
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack ret = super.slotClick(slotId, dragType, clickTypeIn, player);
        this.detectAndSendChanges();
        return ret;
    }

}
