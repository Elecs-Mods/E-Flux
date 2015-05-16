package elec332.eflux.inventory.slot;

import elec332.eflux.tileentity.TileEntityProcessingMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

/**
 * Created by Elec332 on 5-5-2015.
 */
public class SlotFurnaceInput extends SlotInput{

    public SlotFurnaceInput (TileEntityProcessingMachine machine, int index, int x, int z) {
        super(machine, index, x, z);
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