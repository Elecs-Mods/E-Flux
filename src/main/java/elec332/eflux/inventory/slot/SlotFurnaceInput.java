package elec332.eflux.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by Elec332 on 5-5-2015.
 */
public class SlotFurnaceInput extends SlotInput{

    public SlotFurnaceInput(IInventory inventory, int index, int x, int z) {
        super(inventory, index, x, z);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null;
    }

    @Override
    public ItemStack getOutput() {
        return FurnaceRecipes.smelting().getSmeltingResult(getStack());
    }

    @Override
    public void consumeOnProcess() {
        getStack().stackSize--;
        if (getStack().stackSize <= 0){
            putStack(null);
        }
    }
}
