package elec332.eflux.recipes;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class EFluxFurnaceRecipes extends FurnaceRecipes {

    @Nonnull
    public static EFluxFurnaceRecipes getInstance(){
        return (EFluxFurnaceRecipes) FurnaceRecipes.instance();
    }

    public EFluxFurnaceRecipes(){
        super();
        this.recipes = Lists.newArrayList();
    }

    private final List<IEFluxFurnaceRecipe> recipes;

    @Override
    public ItemStack getSmeltingResult(ItemStack stack) {
        /* When the vanilla smelting recipes are registered, my list is still null... :( */
        if (stack != null && recipes != null) {
            for (IEFluxFurnaceRecipe recipe : recipes) {
                if (recipe.accepts(stack)) {
                    return recipe.getOutput(stack);
                }
            }
        }
        return super.getSmeltingResult(stack);
    }

    @Override
    @SuppressWarnings("all")
    public float getSmeltingExperience(ItemStack stack) {
        if (stack != null) {
            for (IEFluxFurnaceRecipe recipe : recipes) {
                if (recipe.accepts(stack)) {
                    return recipe.getExperience(stack);
                }
            }
        }
        return super.getSmeltingExperience(stack);
    }

    public void registerRecipe(IEFluxFurnaceRecipe recipe){
        recipes.add(recipe);
    }

}
