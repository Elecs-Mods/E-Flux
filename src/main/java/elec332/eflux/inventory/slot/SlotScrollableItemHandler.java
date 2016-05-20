package elec332.eflux.inventory.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

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
        IItemHandler handler = getItemHandler();
        if (handler == null){
            return false;
        }
        ItemStack remainder = handler.insertItem(index, stack, true);
        return remainder == null || remainder.stackSize < stack.stackSize;
    }

    @Override
    public ItemStack getStack() {
        IItemHandler handler = getItemHandler();
        if (handler == null){
            return null;
        }
        return handler.getStackInSlot(index);
    }

    @Override
    public void putStack(ItemStack stack) {
        IItemHandler handler = getItemHandler();
        if (handler == null){
            return;
        }
        ((IItemHandlerModifiable) handler).setStackInSlot(index, stack);
        this.onSlotChanged();
    }

    @Override
    public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        IItemHandler handler = getItemHandler();
        if (handler == null){
            return 0;
        }
        ItemStack maxAdd = stack.copy();
        maxAdd.stackSize = maxAdd.getMaxStackSize();
        ItemStack currentStack = handler.getStackInSlot(index);
        ItemStack remainder = handler.insertItem(index, maxAdd, true);

        int current = currentStack == null ? 0 : currentStack.stackSize;
        int added = maxAdd.stackSize - (remainder != null ? remainder.stackSize : 0);
        return current + added;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        IItemHandler handler = getItemHandler();
        return handler != null && handler.extractItem(index, 1, true) != null;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        IItemHandler handler = getItemHandler();
        if (handler == null){
            return null;
        }
        return handler.extractItem(index, amount, false);
    }

    @Nullable
    public IItemHandler getItemHandler() {
        return itemHandler;
    }

}
