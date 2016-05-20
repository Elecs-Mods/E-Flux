package elec332.eflux.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Created by Elec332 on 15-5-2016.
 */
public class SafeWrappedInventory implements IItemHandlerModifiable {

    public static SafeWrappedInventory of(IItemHandlerModifiable i){
        return new SafeWrappedInventory(i);
    }

    private SafeWrappedInventory(IItemHandlerModifiable itemHandler){
        this.itemHandler = itemHandler;
    }

    private IItemHandlerModifiable itemHandler;

    public void clear(){
        itemHandler = null;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (itemHandler == null){
            return;
        }
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return itemHandler == null ? 0 : itemHandler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return itemHandler == null ? null : itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return itemHandler == null ? stack : itemHandler.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return itemHandler == null ? null : itemHandler.extractItem(slot, amount, simulate);
    }

}
