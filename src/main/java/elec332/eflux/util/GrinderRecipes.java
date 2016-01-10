package elec332.eflux.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import elec332.core.java.JavaHelper;
import elec332.core.util.OredictHelper;
import elec332.eflux.EFlux;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class GrinderRecipes{

    public static final GrinderRecipes instance = new GrinderRecipes();
    private GrinderRecipes(){
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
         */
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

}