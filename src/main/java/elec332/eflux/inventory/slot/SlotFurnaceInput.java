package elec332.eflux.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by Elec332 on 19-5-2015.
 */
public class SlotFurnaceInput extends Slot{

    public SlotFurnaceInput (IInventory inventory, int index, int x, int z) {
        super(inventory, index, x, z);
    }

    public boolean isItemValid(ItemStack itemStack) {
        return FurnaceRecipes.instance().getSmeltingResult(itemStack) != null;
    }

    public ItemStack getOutput() {
        return FurnaceRecipes.instance().getSmeltingResult(getStack());
    }

    public void consumeOnProcess() {
        getStack().stackSize--;
        if (getStack().stackSize <= 0){
            putStack(null);
        }
    }
}
