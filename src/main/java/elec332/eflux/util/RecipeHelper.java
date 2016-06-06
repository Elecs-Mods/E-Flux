package elec332.eflux.util;

import com.google.common.collect.Maps;
import elec332.core.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.Map;
import java.util.function.Function;


/**
 * Created by Elec332 on 6-6-2016.
 */
public class RecipeHelper {

    public static <T extends IRecipe> T registerRecipe(Function<Data, T> f, ItemStack stack, Object... recipeComponents){
        T ret = f.apply(decipher(stack, recipeComponents));
        RegistryHelper.getCraftingManager().addRecipe(ret);
        return ret;
    }

    public static Data decipher(ItemStack stack, Object... recipeComponents){
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[]) recipeComponents[i++];

            for (String s2 : astring) {
                ++k;
                j = s2.length();
                s = s + s2;
            }
        } else {
            while (recipeComponents[i] instanceof String) {
                String s1 = (String)recipeComponents[i++];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }

        Map<Character, ItemStack> map;

        for (map = Maps.<Character, ItemStack>newHashMap(); i < recipeComponents.length; i += 2) {
            Character character = (Character)recipeComponents[i];
            ItemStack itemstack = null;
            if (recipeComponents[i + 1] instanceof Item) {
                itemstack = new ItemStack((Item)recipeComponents[i + 1]);
            } else if (recipeComponents[i + 1] instanceof Block) {
                itemstack = new ItemStack((Block)recipeComponents[i + 1], 1, 32767);
            } else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack = (ItemStack)recipeComponents[i + 1];
            }
            map.put(character, itemstack);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);
            if (map.containsKey(c0)) {
                aitemstack[i1] = map.get(c0).copy();
            } else {
                aitemstack[i1] = null;
            }
        }
        return new Data(j, k, aitemstack, stack);
    }

    public static class Data {

        private Data(int width, int height, ItemStack[] ingredients, ItemStack output) {
            this.width = width;
            this.height = height;
            this.ingredients = ingredients;
            this.output = output;
        }

        public final int width, height;
        public final ItemStack[] ingredients;
        public final ItemStack output;

    }

    public static final Function<Data, ShapedRecipes> SHAPED_RECIPE_FUNCTION, SHAPED_RECIPE_WITH_NBT_CHECK;

    static {
        SHAPED_RECIPE_FUNCTION = (Data data) -> new ShapedRecipes(data.width, data.height, data.ingredients, data.output);
        SHAPED_RECIPE_WITH_NBT_CHECK = (Data data) -> new ShapedRecipes(data.width, data.height, data.ingredients, data.output){

            //TODO

        };
    }

}
