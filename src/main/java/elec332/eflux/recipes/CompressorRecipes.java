package elec332.eflux.recipes;

import com.google.common.collect.Lists;
import elec332.core.util.InventoryHelper;
import elec332.core.util.ItemStackHelper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 21-1-2016.
 */
public class CompressorRecipes {

    private static final CompressorRecipes instance = new CompressorRecipes();
    private CompressorRecipes(){
        recipes = Lists.newArrayList();
    }

    private List<IRecipeHandler> recipes;

    public void registerRecipe(ItemStack in, ItemStack out, boolean nbt){
        if (!ItemStackHelper.isStackValid(in) || !ItemStackHelper.isStackValid(out)){
            return;
        }
        registerRecipe(new StackRecipe(in, out, nbt));
    }

    public void registerRecipe(IRecipeHandler recipeHandler){
        if (recipeHandler.getType() != EnumRecipeMachine.COMPRESSOR){
            throw new IllegalArgumentException();
        }
        recipes.add(recipeHandler);
    }

    public ItemStack getOutput(@Nonnull ItemStack in){
        for (IRecipeHandler recipe : recipes){
            ItemStack d = in.copy();
            if (recipe.canHandle(d)){
                return recipe.handle(d);
            }
        }
        return null;
    }

    public static CompressorRecipes getInstance(){
        return instance;
    }

    private class StackRecipe implements IRecipeHandler {

        public StackRecipe(ItemStack in, ItemStack out, boolean nbt){
            this.in = in;
            this.out = out;
            this.nbt = nbt;
        }

        private final ItemStack in, out;
        private final boolean nbt;

        @Override
        public EnumRecipeMachine getType() {
            return EnumRecipeMachine.COMPRESSOR;
        }

        @Override
        public boolean canHandle(ItemStack stack) {
            return stack.stackSize > 0 && (nbt ? InventoryHelper.areEqualNoSize(in, stack) : InventoryHelper.areEqualNoSizeNoNBT(in, stack));
        }

        @Override
        public ItemStack handle(ItemStack stack) {
            return out.copy();
        }

    }

}
