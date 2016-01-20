package elec332.eflux.util;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class GrinderRecipes{

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
            return new OreDictStack(name, amount + stack.amount);
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

    public void addRecipe(IGrinderRecipe recipe){
        recipes.add(recipe);
    }

    public OreDictStack[] getGrindResult(ItemStack stack){
        List<OreDictStack> parts = Lists.newArrayList();
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
            }
        }
        if (parts.isEmpty()){
            parts.add(new OreDictStack(scrap, max));
        }
        return parts.toArray(new OreDictStack[parts.size()]);
    }

    /*private GrinderRecipes(){
        recipes = Lists.newArrayList();
        recipeIdentifiers = Lists.newArrayList();
    }

    private List<OreGrindingRecipe> recipes;
    private List<String> recipeIdentifiers;

    public static final String scrap = "scrap";
    public static final String stoneDust = "stoneDust";

    public void addRecipe(OreGrindingRecipe recipe){
        if (!recipeIdentifiers.contains(recipe.mainOreOutput)) {
            recipes.add(recipe);
            recipeIdentifiers.add(recipe.mainOreOutput);
        }
    }

    public DustPile.DustPart[] getGrindResult(ItemStack stack){
        List<String> oredict = OredictHelper.getOreNames(stack);
        if (!oredict.isEmpty() && (JavaHelper.doesListContainPartially(oredict, "ore")) || JavaHelper.doesListContainPartially(oredict, "ingot")){
            if (stack.getItem() instanceof ItemBlock){
                int i = EFlux.random.nextInt(6) + 1;
                return getFromOre(oredict, i);
            } else {
                List<String> s = JavaHelper.replaceAll(oredict, "ingot", "");
                return new DustPile.DustPart[]{new DustPile.DustPart(s).addNuggets(9)};
            }
        }
        int n = 9;
        if (stack.getItem() instanceof ItemBlock){
            n *= 2;
        }
        return new DustPile.DustPart[]{new DustPile.DustPart(Lists.newArrayList(scrap)).addNuggets(n)};
    }

    private DustPile.DustPart[] getFromOre(List<String> oredict, int stone){
        List<DustPile.DustPart> ret = Lists.newArrayList();
        ret.add(new DustPile.DustPart(Lists.newArrayList(stoneDust)).addNuggets(stone));
        List<String> s = JavaHelper.replaceAll(oredict, "ore", "");
        OreGrindingRecipe recipe = getRecipe(s);
        if (recipe != null){
            if (recipe.secondaryOreOutput.length > 0 && EFlux.random.nextInt(10*stone) < 2){
                ret.add(new DustPile.DustPart(Lists.newArrayList(recipe.secondaryOreOutput[EFlux.random.nextInt(recipe.secondaryOreOutput.length)])).addNuggets(1));
                stone++;
            }
            ret.add(new DustPile.DustPart(Lists.newArrayList(recipe.mainOreOutput)).addNuggets(18-stone));
        } else {
            ret.add(new DustPile.DustPart(Lists.newArrayList(s)).addNuggets(18-stone));
        }

        return ret.toArray(new DustPile.DustPart[ret.size()]);
    }


    private OreGrindingRecipe getRecipe(List<String> ore){
        for (OreGrindingRecipe recipe : recipes)
            if (JavaHelper.hasAtLeastOneMatch(ore, Lists.newArrayList(recipe.ore)))
                return recipe;
        return null;
    }

    public static class OreGrindingRecipe implements Serializable{

        /**
         * For Json
         *
        @SuppressWarnings("unused")
        public OreGrindingRecipe(){
        }

        public OreGrindingRecipe(List<String> ore, String mainOreOutput){
            this.ore = ore;
            this.mainOreOutput = mainOreOutput;
        }

        private List<String> ore;
        private String mainOreOutput;
        private String[] secondaryOreOutput = new String[0];

        public OreGrindingRecipe setSecondaryOutput(String... s){
            secondaryOreOutput = s;
            return this;
        }

    }

    static {
        OredictHelper.initLists();
        instance.addRecipe(new OreGrindingRecipe(Lists.newArrayList("Iron"), "dustIron").setSecondaryOutput("dustTin"));
        for (String s : OredictHelper.getAllOres()){
            addDefaultRecipe(OredictHelper.concatOreName(s));
        }
        for (String s : OredictHelper.getAllIngots()){
            addDefaultRecipe(OredictHelper.concatIngotName(s));
        }
    }

    private static void addDefaultRecipe(String s){
        instance.addRecipe(new OreGrindingRecipe(Lists.newArrayList(s), "dust"+s));
    }

    private static class IdentifierRegistry{

        private static final Map<ItemStack, String> registry = Maps.newHashMap();



    */

}