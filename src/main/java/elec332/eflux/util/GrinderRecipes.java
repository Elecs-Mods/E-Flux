package elec332.eflux.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import elec332.core.util.OredictHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class GrinderRecipes {

    public static final GrinderRecipes instance = new GrinderRecipes();
    private GrinderRecipes(){
        this.recipes = Lists.newArrayList();
    }

    private List<IGrinderRecipe> recipes;

    public static final String scrap = "EFluxScrap";
    public static final String stoneDust = "dustStone";

    public interface IGrinderRecipe {

        public boolean accepts(ItemStack stack);

        /**
         * This method should return an array of ItemStacks, combined stack-sizes
         * not exceeding the given parameter. The returned array is the processing
         * result.
         *
         * @param stack The input stack.
         * @param total The total output stack.
         * @return The output of this recipe.
         */
        public OreDictStack[] getOutput(ItemStack stack, int total);

    }


    public void addRecipe(String input, OreDictStack output){
        if (Strings.isNullOrEmpty(input) || output == null){
            throw new IllegalArgumentException();
        }
        addRecipe(new DefaultRecipe(input, output.copy()));
    }

    public void addRecipe(IGrinderRecipe recipe){
        recipes.add(recipe);
    }

    public NonNullList<OreDictStack> getGrindResult(ItemStack stack){
        NonNullList<OreDictStack> parts = NonNullList.create();
        int max = stack.getItem() instanceof ItemBlock ? 18 : 9;
        recipeLoop:
        for (IGrinderRecipe recipe : recipes){
            if (recipe.accepts(stack)){
                OreDictStack[] s = recipe.getOutput(stack, max);
                if (s == null || s.length == 0){
                    continue;
                }
                int i = 0;
                for (OreDictStack stack1 : s){
                    i += stack1.amount;
                    List l = OreDictionary.getOres(stack1.name);
                    if (l == null || l.isEmpty()){
                        parts.clear();
                        continue recipeLoop;
                    }
                    parts.add(new OreDictStack(stack1.name, stack1.amount));
                    if (i > max){
                        throw new RuntimeException(recipe.getClass().getName());
                    }
                }
                break;
            }
        }
        if (parts.isEmpty()){
            parts.add(new OreDictStack(scrap, max));
        }
        return parts;
    }

    private class DefaultRecipe implements IGrinderRecipe {

        public DefaultRecipe(String in, OreDictStack out){
            this.in = in;
            this.out = out;
        }

        private final String in;
        private final OreDictStack out;

        @Override
        public boolean accepts(ItemStack stack) {
            return OredictHelper.getOreNames(stack).contains(in);
        }

        @Override
        public OreDictStack[] getOutput(ItemStack stack, int total) {
            OreDictStack o = out.copy();
            return new OreDictStack[]{o};
        }

    }

    public static class OreDictStack {

        public OreDictStack(String name, int amount){
            this.name = name;
            this.amount = amount;
        }

        public final String name;
        public int amount;

        public boolean canMerge(OreDictStack stack){
            return stack.name.equals(name);
        }

        public OreDictStack merge(OreDictStack stack){
            if (!canMerge(stack)){
                throw new IllegalArgumentException();
            }
            amount += stack.amount;
            return this;
        }

        public void writeToNBT(NBTTagCompound tag){
            tag.setString("oreDictName", name);
            tag.setInteger("stackAmnt", amount);
        }

        public static OreDictStack readFromNBT(NBTTagCompound tag){
            if (!tag.hasKey("oreDictName") || !tag.hasKey("stackAmnt")){
                return null;
            }
            return new OreDictStack(tag.getString("oreDictName"), tag.getInteger("stackAmnt"));
        }

        public OreDictStack copy(){
            return new OreDictStack(name, amount);
        }

    }

}