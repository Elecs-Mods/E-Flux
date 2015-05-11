package elec332.eflux.inventory.slot;

import elec332.eflux.recipes.RecipeRegistry;
import elec332.eflux.tileentity.TileEntityProcessingMachine;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 9-5-2015.
 */
public class SlotMachine extends SlotInput {
    public SlotMachine(TileEntityProcessingMachine inventory, int index, int x, int z) {
        super(inventory, index, x, z);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return RecipeRegistry.instance.hasOutput(tile, itemStack);
    }

    @Override
    public ItemStack getOutput() {
        return RecipeRegistry.instance.getOutput(tile, getStack())[0];
    }

    @Override
    public void consumeOnProcess() {
        getStack().stackSize--;
        if (getStack().stackSize <= 0){
            putStack(null);
        }
    }
}
