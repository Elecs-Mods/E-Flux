package elec332.eflux.inventory;

import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.core.inventory.widget.slot.WidgetScrollableSlot;
import elec332.core.inventory.window.Window;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 3-12-2016.
 */
public class WindowEnderInventory extends Window implements WidgetButton.IButtonEventListener {

    public WindowEnderInventory(IItemHandler itemHandler) {
        super();
        this.scrollableSlots = new WidgetScrollableSlot[27];
        this.itemHandler = itemHandler;
    }

    private IItemHandler itemHandler;
    private final WidgetScrollableSlot[] scrollableSlots;
    private WidgetButton up, down;
    private int idx;

    @Override
    protected void initWindow() {
        this.addPlayerInventoryToContainer();
        int offset = -67;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                int index = j + i * 9;
                this.scrollableSlots[index] = addWidget(new WidgetScrollableSlot(itemHandler, index, 8 + j * 18, (84 + offset) + i * 18));
            }
        }
        this.up = addWidget(new WidgetButtonArrow(7, 4, WidgetButtonArrow.Direction.UP)).addButtonEvent(this);
        this.down = addWidget(new WidgetButtonArrow(7, 70, WidgetButtonArrow.Direction.DOWN)).addButtonEvent(this);
        this.up.setActive(false);
        this.down.setActive(itemHandler.getSlots() > 27);
    }

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
            scrollableSlots[i].setSlotIndex(i + idx * 9);
        }
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack ret = super.slotClick(slotId, dragType, clickTypeIn, player);
        this.detectAndSendChanges();
        return ret;
    }

}
