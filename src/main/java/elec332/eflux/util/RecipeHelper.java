package elec332.eflux.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import elec332.core.util.InventoryHelper;
import elec332.core.util.ItemStackHelper;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.RecipeSorter;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


/**
 * Created by Elec332 on 6-6-2016.
 */
public class RecipeHelper {

 /*   public static <T extends IRecipe> T registerRecipe(Function<Data, T> f, ItemStack stack, Object... recipeComponents){
        T ret = f.apply(decipher(Preconditions.checkNotNull(stack), recipeComponents));
        elec332.core.util.recipes.RecipeHelper.getCraftingManager().registerRecipe(ret);
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
        public final NonNullList<Ingredient> ingredients;
        public final ItemStack output;

    }

    public static final Function<Data, ShapedRecipes> SHAPED_RECIPE_FUNCTION, SHAPED_RECIPE_WITH_NBT_CHECK;

    private static class ShapedNBTRecipe extends ShapedRecipes {

        public ShapedNBTRecipe(int width, int height, NonNullList<Ingredient> input, ItemStack output) {
            super(UUID.randomUUID().toString(), width, height, input, output);
        }

        @Override
        public boolean checkMatch(@Nonnull InventoryCrafting inventoryCrafting, int p_77573_2_, int p_77573_3_, boolean p_77573_4_) {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    int k = i - p_77573_2_;
                    int l = j - p_77573_3_;
                    ItemStack itemstack = null;

                    if (k >= 0 && l >= 0 && k < this.recipeWidth && l < this.recipeHeight) {
                        if (p_77573_4_) {
                            itemstack = this.recipeItems[this.recipeWidth - k - 1 + l * this.recipeWidth];
                        } else {
                            itemstack = this.recipeItems[k + l * this.recipeWidth];
                        }
                    }

                    ItemStack itemstack1 = inventoryCrafting.getStackInRowAndColumn(i, j);

                    if (ItemStackHelper.isStackValid(itemstack1) || ItemStackHelper.isStackValid(itemstack)) {
                        if (!InventoryHelper.areEqualNoSize(itemstack, itemstack1)){
                            return false;
                        }
                    }
                }
            }
            return true;
        }

    }

    static {
        RecipeSorter.register("eflux:shapedNBT", ShapedNBTRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        SHAPED_RECIPE_FUNCTION = (Data data) -> new ShapedRecipes(data.width, data.height, data.ingredients, data.output);
        SHAPED_RECIPE_WITH_NBT_CHECK = (Data data) -> new ShapedNBTRecipe(data.width, data.height, data.ingredients, data.output);
    }*/

}
