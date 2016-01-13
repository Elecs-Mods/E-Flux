package elec332.eflux.init;

import elec332.eflux.recipes.IEFluxFurnaceRecipe;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class RecipeRegister {

    public static void registerRecipes(){
        registerCraftingRecipes();
        registerFurnaceRecipes();
        registerEFluxRecipes();
    }

    private static void registerCraftingRecipes(){

    }

    private static void registerFurnaceRecipes(){

    }

    private static void registerEFluxRecipes(){

    }

    private static class DustSmeltingRecipe implements IEFluxFurnaceRecipe {

        @Override
        public boolean accepts(ItemStack input) {
            return input.getItem() == ItemRegister.groundMesh;
        }

        @Override
        public ItemStack getOutput(ItemStack stack) {
            return null;
        }

        @Override
        public float getExperience(ItemStack input) {
            return 0.3f;
        }

    }

}
