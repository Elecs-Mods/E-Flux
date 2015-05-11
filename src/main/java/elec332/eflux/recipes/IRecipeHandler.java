package elec332.eflux.recipes;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 10-5-2015.
 */
public interface IRecipeHandler {

    public boolean hasOutput(ItemStack... itemStack);

    public ItemStack[] getOutput(ItemStack... itemStack);

    public void registerRecipe(Object input, ItemStack... output);

}
