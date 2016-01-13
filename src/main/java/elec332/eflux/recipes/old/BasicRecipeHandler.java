package elec332.eflux.recipes.old;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import elec332.eflux.recipes.old.AbstractRecipeHandler;
import elec332.eflux.util.RecipeItemStack;
import net.minecraft.item.ItemStack;

import java.util.AbstractList;

/**
 * Created by Elec332 on 18-5-2015.
 */
public class BasicRecipeHandler extends AbstractRecipeHandler {

    protected BasicRecipeHandler(int i, int o){
        if (i < 1 || o < 1)
            throw new IllegalArgumentException("Machine cannot have 0 input or output slots");
        this.i = i;
        this.o = o;
    }

    private int i;
    private int o;

    @Override
    public void registerRecipe(Object input, Object output) {
        if (!(output instanceof ItemStack || output instanceof ItemStack[]))
            throw new IllegalArgumentException("The output of a recipe must be an ItemStack!!!");
        if (output instanceof ItemStack[] && ((ItemStack[]) output).length > o)
            throw new IllegalArgumentException("Output amount cannot be larger that the amount of output slots");
        ItemStack[] recipeResult;
        AbstractList<RecipeItemStack> recipeItemStacks = Lists.newArrayList();
        if (output instanceof ItemStack[]){
            if (((ItemStack[])output).length > o)
                throw new IllegalArgumentException("Output amount cannot be larger that the amount of output slots");
            recipeResult = (ItemStack[]) output;
        }else recipeResult = new ItemStack[]{(ItemStack)output};
        checkSize(input);
        if (input instanceof String && !Strings.isNullOrEmpty((String) input))
            recipeItemStacks.add(new RecipeItemStack((String) input));
        else if (input instanceof ItemStack)
            recipeItemStacks.add(new RecipeItemStack((ItemStack)input));
        else if (input instanceof String[]){
            for (String s : (String[])input){
                if (!Strings.isNullOrEmpty(s))
                    recipeItemStacks.add(new RecipeItemStack(s));
            }
        } else if (input instanceof ItemStack[]){
            for (ItemStack stack : (ItemStack[])input){
                recipeItemStacks.add(new RecipeItemStack(stack));
            }
        } else if (input instanceof RecipeItemStack)
            recipeItemStacks.add((RecipeItemStack)input);
        else if (input instanceof RecipeItemStack[]){
            recipeItemStacks = Lists.newArrayList((RecipeItemStack[])input);
        } else if (input instanceof AbstractList && ((AbstractList)input).size() > 0 && ((AbstractList)input).get(0) instanceof RecipeItemStack){
            if (((AbstractList)input).size() > i)
                throw new IllegalArgumentException("Input amount cannot be larger that the amount of input slots");
            @SuppressWarnings("unchecked")
            AbstractList<RecipeItemStack> recipeItemStacks1 = Lists.newArrayList((AbstractList) input);
            recipeItemStacks.addAll(recipeItemStacks1);
        }
        if (recipeItemStacks.size() > 0 && recipeResult.length > 0)
            addRecipe(recipeItemStacks, recipeResult);
    }

    private <A> void checkSize(A possArray){
        if (possArray.getClass().isArray())
            checkSize((Object[])possArray);
    }

    private <A> void checkSize(A[] array){
        if (array.length > i)
            throw new IllegalArgumentException("Input amount cannot be larger that the amount of input slots");
        if (array.length == 0)
            throw new IllegalArgumentException("Input cannot be 0 ItemStacks");
    }
}
