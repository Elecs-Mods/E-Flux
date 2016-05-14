package elec332.eflux.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class SlotScrollable extends Slot {

    public SlotScrollable(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, -1, xPosition, yPosition);
        this.index = index;
    }

    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getSlotIndex() {
        return index;
    }

    @Override
    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.index);
    }

    @Override
    public void putStack(ItemStack stack) {
        this.inventory.setInventorySlotContents(this.index, stack);
        this.onSlotChanged();
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return this.inventory.decrStackSize(this.index, amount);
    }

    @Override
    public boolean isHere(IInventory inv, int slotIn) {
        return inv == this.inventory && slotIn == this.index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return inventory.isItemValidForSlot(index, stack);
    }


}
