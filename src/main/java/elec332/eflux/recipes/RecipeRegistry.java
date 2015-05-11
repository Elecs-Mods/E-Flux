package elec332.eflux.recipes;

import elec332.eflux.util.EnumMachines;
import elec332.eflux.util.IEFluxMachine;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 13-4-2015.
 */
public class RecipeRegistry {

    public static final RecipeRegistry instance = new RecipeRegistry();
    private RecipeRegistry(){
        recipeHandlers = new IRecipeHandler[32];
    }

    private IRecipeHandler[] recipeHandlers;

    public void registerHandler(IRecipeHandler handler, EnumMachines machine){
        if (recipeHandlers[machine.ordinal()] == null)
            recipeHandlers[machine.ordinal()] = handler;
        else throw new RuntimeException("There is already a handler for EnumMachine "+machine.toString());
    }

    public boolean hasOutput(IEFluxMachine machine, ItemStack... itemStack){
        return hasOutput(machine.getMachine(), itemStack);
    }

    public boolean hasOutput(EnumMachines machine, ItemStack... itemStack){
        return getRecipeHandler(machine).hasOutput(itemStack);
    }

    public ItemStack[] getOutput(IEFluxMachine machine, ItemStack... itemStack){
        return getOutput(machine.getMachine(), itemStack);
    }

    public ItemStack[] getOutput(EnumMachines machine, ItemStack... itemStack){
        return getRecipeHandler(machine).getOutput(itemStack);
    }

    public void registerRecipe(EnumMachines machine, Object input, ItemStack... itemStack){
        getRecipeHandler(machine).registerRecipe(input, itemStack);
    }

    public IRecipeHandler getRecipeHandler(EnumMachines machine){
        return recipeHandlers[machine.ordinal()];
    }


    /*public ItemStack[] getOutput(InventoryCrafting inventoryCrafting, EnumMachines machine){
        int numSlots = inventoryCrafting.getSizeInventory();
        List<ItemStack> tempList = new ArrayList<ItemStack>();
        for (int i = 0; i < numSlots; i++){
            tempList.add(inventoryCrafting.getStackInSlot(i));
        }
        ItemStack[] itemStacks = tempList.toArray(new ItemStack[tempList.size()]);
        for (IEFluxRecipe recipe : this.recipeHandlers.get(machine)){
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
    }*/

    static {
        instance.registerHandler(new GrinderRecipeHandler(), EnumMachines.GRINDER);
    }
}
