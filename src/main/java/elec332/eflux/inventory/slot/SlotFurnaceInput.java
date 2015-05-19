package elec332.eflux.inventory.slot;

import elec332.eflux.tileentity.TileEntityProcessingMachine;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by Elec332 on 19-5-2015.
 */
public class SlotFurnaceInput extends Slot{

    public SlotFurnaceInput (TileEntityProcessingMachine machine, int index, int x, int z) {
        super(machine.getInventory(), index, x, z);
    }

    public boolean isItemValid(ItemStack itemStack) {
        return FurnaceRecipes.smelting().getSmeltingResult(itemStack) != null;
    }

    public ItemStack getOutput() {
        return FurnaceRecipes.smelting().getSmeltingResult(getStack());
    }

    public void consumeOnProcess() {
        getStack().stackSize--;
        if (getStack().stackSize <= 0){
            putStack(null);
        }
    }
}
