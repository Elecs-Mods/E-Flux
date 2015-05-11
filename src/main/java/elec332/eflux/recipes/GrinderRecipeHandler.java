package elec332.eflux.recipes;

import elec332.core.helper.OredictHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

/**
 * Created by Elec332 on 10-5-2015.
 */
public class GrinderRecipeHandler implements IRecipeHandler {

    protected GrinderRecipeHandler(){
        this.recipes = new HashMap<String, ItemStack>();
        init();
    }

    private HashMap<String, ItemStack> recipes;

    @Override
    public boolean hasOutput(ItemStack... itemStack) {
        return getOutput(itemStack) != null;
    }

    @Override
    public ItemStack[] getOutput(ItemStack... itemStack) {
        if (itemStack != null && itemStack.length == 1){
            ItemStack ret = recipes.get(OredictHelper.getOreName(itemStack[0]));
            if (ret != null)
                return new ItemStack[]{ret};
        }
        return null;
    }

    @Override
    public void registerRecipe(Object input, ItemStack... output) {
        if (input instanceof String && output != null && output.length == 1){
            if (recipes.containsKey(input))
                throw new RuntimeException("Recipe for type is already registered");
            recipes.put((String)input, output[0]);
        } else throw new RuntimeException("Error registering recipe!");
    }

    private void init(){
        //TestRecipe
        registerRecipe("plankWood", new ItemStack(Blocks.planks, 1, 3));
        //registerRecipe("oreIron", 'ref-to-dust-itemstack');
    }
}
