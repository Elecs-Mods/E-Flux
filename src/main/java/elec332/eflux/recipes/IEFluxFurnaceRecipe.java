package elec332.eflux.recipes;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-1-2016.
 */
public interface IEFluxFurnaceRecipe {

    public boolean accepts(@Nonnull ItemStack input);

    public ItemStack getOutput(@Nonnull ItemStack input);

    public float getExperience(@Nonnull ItemStack input);

}
