package elec332.eflux.util;

import elec332.core.util.OredictHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Elec332 on 17-5-2015.
 */
public final class RecipeItemStack {

    public RecipeItemStack(String s){
        this.oreDictName = s;
        this.oreDict = true;
        this.stackSize = 1;
    }

    public RecipeItemStack(ItemStack stack){
        this(stack.getItem(), stack.getItemDamage());
        setStackSize(stack.stackSize);
    }

    public RecipeItemStack(Item item, int meta){
        this(item);
        this.meta = meta;
    }

    public RecipeItemStack(Item item){
        this.item = item;
        setStackSize(1);
        this.meta = 0;
        this.oreDict = false;
    }

    public RecipeItemStack setStackSize(int stackSize) {
        this.stackSize = stackSize;
        return this;
    }

    public int getStackSize() {
        return stackSize;
    }

    private Item item;
    private int stackSize;
    private int meta;
    private boolean oreDict;
    private String oreDictName;

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof ItemStack && ((ItemStack)obj).getItem() == item && ((ItemStack)obj).stackSize <= stackSize && ((ItemStack)obj).getItemDamage() == meta) ||
                (obj instanceof RecipeItemStack && test((RecipeItemStack)obj));
               // oreDict ? (obj instanceof RecipeItemStack && ()):
        //(obj instanceof RecipeItemStack && ((RecipeItemStack)obj).item == item && ((RecipeItemStack)obj).stackSize <= stackSize && ((RecipeItemStack)obj).meta == meta);
    }

    boolean test(RecipeItemStack recipeItemStack){
        //if (recipeItemStack.oreDict)
         //   System.out.println(OreDictionary.getOres(recipeItemStack.oreDictName).size());
        //System.out.println(isRegisteredAsOre("ingotIron", new ItemStack(Items.iron_ingot, 1, 0)));
        return (recipeItemStack.item == item && recipeItemStack.stackSize <= stackSize && recipeItemStack.meta == meta) || (recipeItemStack.oreDict && recipeItemStack.stackSize <= stackSize && isRegisteredAsOre(recipeItemStack.oreDictName, new ItemStack(item, 1, meta)));//OreDictionary.getOres(recipeItemStack.oreDictName).contains(new ItemStack(item, 1, meta)));
    }

    private boolean isRegisteredAsOre(String s, ItemStack stack){
        for (ItemStack itemStack : OredictHelper.getOres(s, false)){
            if (itemStack.getItem() == stack.getItem()){
                if (itemStack.getItemDamage() == stack.getItemDamage())
                    return true;
                if (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
                    return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return meta;
    }
}
