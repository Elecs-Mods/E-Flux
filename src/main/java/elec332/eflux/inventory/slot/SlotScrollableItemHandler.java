package elec332.eflux.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class SlotScrollableItemHandler extends Slot {

    public SlotScrollableItemHandler(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(new InventoryBasic("", false, 0), -1, xPosition, yPosition);
        this.itemHandler = inventoryIn;
        this.index = index;
    }

    private int index;
    private final IItemHandler itemHandler;

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getSlotIndex() {
        return index;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        if (stack == null)
            return false;
        ItemStack remainder = this.getItemHandler().insertItem(index, stack, true);
        return remainder == null || remainder.stackSize < stack.stackSize;
    }

    @Override
    public ItemStack getStack() {
        return this.getItemHandler().getStackInSlot(index);
    }

    @Override
    public void putStack(ItemStack stack) {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(index, stack);
        this.onSlotChanged();
    }

    @Override
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {

    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        maxAdd.stackSize = maxAdd.getMaxStackSize();
        ItemStack currentStack = this.getItemHandler().getStackInSlot(index);
        ItemStack remainder = this.getItemHandler().insertItem(index, maxAdd, true);

        int current = currentStack == null ? 0 : currentStack.stackSize;
        int added = maxAdd.stackSize - (remainder != null ? remainder.stackSize : 0);
        return current + added;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return this.getItemHandler().extractItem(index, 1, true) != null;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        return this.getItemHandler().extractItem(index, amount, false);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

}
