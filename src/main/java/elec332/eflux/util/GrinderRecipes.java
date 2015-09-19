package elec332.eflux.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import elec332.core.helper.OredictHelper;
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
        String oredict = OredictHelper.getOreName(stack);
        if (!Strings.isNullOrEmpty(oredict) && (oredict.contains("ore") || oredict.contains("ingot"))){
            if (stack.getItem() instanceof ItemBlock){
                int i = EFlux.random.nextInt(6) + 1;
                return getFromOre(oredict, i);
            } else {
                String s = oredict.replace("ingot", "");
                return new DustPile.DustPart[]{new DustPile.DustPart(s).addNuggets(9)};
            }
        }
        int n = 9;
        if (stack.getItem() instanceof ItemBlock){
            n *= 2;
        }
        return new DustPile.DustPart[]{new DustPile.DustPart(scrap).addNuggets(n)};
    }

    private DustPile.DustPart[] getFromOre(String oredict, int stone){
        List<DustPile.DustPart> ret = Lists.newArrayList();
        ret.add(new DustPile.DustPart(stoneDust).addNuggets(stone));
        String s = oredict.replace("ore", "");
        OreGrindingRecipe recipe = getRecipe(s);
        if (recipe != null){
            if (recipe.secondaryOreOutput.length > 0 && EFlux.random.nextInt(10*stone) < 2){
                ret.add(new DustPile.DustPart(recipe.secondaryOreOutput[EFlux.random.nextInt(recipe.secondaryOreOutput.length)]).addNuggets(1));
                stone++;
            }
            ret.add(new DustPile.DustPart(recipe.mainOreOutput).addNuggets(18-stone));
        } else {
            ret.add(new DustPile.DustPart(scrap).addNuggets(18-stone));
        }

        return ret.toArray(new DustPile.DustPart[ret.size()]);
    }


    private OreGrindingRecipe getRecipe(String ore){
        for (OreGrindingRecipe recipe : recipes)
            if (recipe.ore.equalsIgnoreCase(ore))
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

        public OreGrindingRecipe(String ore, String mainOreOutput){
            this.ore = ore;
            this.mainOreOutput = mainOreOutput;
        }

        private String ore, mainOreOutput;
        private String[] secondaryOreOutput = new String[0];

        public OreGrindingRecipe setSecondaryOutput(String... s){
            secondaryOreOutput = s;
            return this;
        }

    }

    static {
        OredictHelper.initLists();
        instance.addRecipe(new OreGrindingRecipe("Iron", "dustIron").setSecondaryOutput("dustTin"));
        for (String s : OredictHelper.getAllOres()){
            addDefaultRecipe(OredictHelper.concatOreName(s));
        }
        for (String s : OredictHelper.getAllIngots()){
            addDefaultRecipe(OredictHelper.concatIngotName(s));
        }
    }

    private static void addDefaultRecipe(String s){
        instance.addRecipe(new OreGrindingRecipe(s, "dust"+s));
    }

}