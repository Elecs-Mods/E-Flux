package elec332.eflux.recipes;

import elec332.eflux.util.EnumMachines;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elec332 on 13-4-2015.
 */
public class RecipeRegistry {

    public static final RecipeRegistry instance = new RecipeRegistry();
    private RecipeRegistry(){
        recipes = new HashMap<EnumMachines, List<IEFluxRecipe>>();
    }

    private HashMap<EnumMachines, List<IEFluxRecipe>> recipes;

    public void registerRecipe(IEFluxRecipe recipe){
        if (recipes.get(recipe.getMachine()) == null)
            recipes.put(recipe.getMachine(), new ArrayList<IEFluxRecipe>());
        recipes.get(recipe.getMachine()).add(recipe);
    }

    public boolean hasResult(InventoryCrafting stack, EnumMachines machine){
        return getOutput(stack, machine) != null;
    }

    public ItemStack[] getOutput(InventoryCrafting inventoryCrafting, EnumMachines machine){
        int numSlots = inventoryCrafting.getSizeInventory();
        List<ItemStack> tempList = new ArrayList<ItemStack>();
        for (int i = 0; i < numSlots; i++){
            tempList.add(inventoryCrafting.getStackInSlot(i));
        }
        ItemStack[] itemStacks = tempList.toArray(new ItemStack[tempList.size()]);
        for (IEFluxRecipe recipe : this.recipes.get(machine)){
            if (!recipe.multipleInput() && inventoryCrafting.getSizeInventory() == 1){
                if (isStackValidForCrafting(recipe.getIngredients()[0], itemStacks[0]))
                    return recipe.getResult();
            } else if (recipe.isShapeless()){
                for (ItemStack recipeStack : recipe.getIngredients()){
                    boolean flag = false;
                    for (ItemStack stackInSlot : tempList){
                        if (isStackValidForCrafting(recipeStack, stackInSlot)) {
                            flag = true;
                            tempList.remove(stackInSlot);
                            break;
                        }
                    }
                    if (!flag) {
                        return null;
                    }
                }
                return recipe.getResult();
            } else {
                for (int i = 0; i < recipe.getIngredients().length; i++){
                    ItemStack recipeStack = recipe.getIngredients()[i];
                    ItemStack stackInSlot = itemStacks[i];
                    if (!isStackValidForCrafting(recipeStack, stackInSlot))
                        return null;
                }
                return recipe.getResult();
            }
        }
        return null;
    }

    private boolean isStackValidForCrafting(ItemStack recipeStack, ItemStack toCheck){
        return recipeStack != null && toCheck != null && recipeStack.getItem() == toCheck.getItem() && toCheck.stackSize >= recipeStack.stackSize &&(toCheck.getItemDamage() == Short.MAX_VALUE || recipeStack.getItemDamage() == toCheck.getItemDamage()) || recipeStack == null && toCheck == null;
    }
}
