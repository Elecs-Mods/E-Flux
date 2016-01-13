package elec332.eflux.recipes;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 13-1-2016.
 */
public interface IEFluxFurnaceRecipe {

    public boolean accepts(ItemStack input);

    public ItemStack getOutput(ItemStack stack);

    public float getExperience(ItemStack input);

}
