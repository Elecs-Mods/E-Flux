package elec332.eflux.recipes.old;

import elec332.eflux.util.IEFluxMachine;
import net.minecraft.inventory.Slot;

import java.util.List;

/**
 * Created by Elec332 on 13-4-2015.
 */
public class RecipeRegistry {

    public static final RecipeRegistry instance = new RecipeRegistry();
    private RecipeRegistry(){
        recipeHandlers = new IRecipeHandler[32];
    }

    private IRecipeHandler[] recipeHandlers;

    public void registerHandler(IRecipeHandler handler, EnumRecipeMachine machine){
        if (recipeHandlers[machine.ordinal()] == null)
            recipeHandlers[machine.ordinal()] = handler;
        else throw new RuntimeException("There is already a handler for EnumMachine "+machine.toString());
    }

    public boolean hasOutput(IEFluxMachine machine, List<Slot> slots){
        return hasOutput(machine.getMachine(), slots);
    }

    public boolean hasOutput(EnumRecipeMachine machine, List<Slot> slots){
        return getRecipeHandler(machine).hasOutput(slots);
    }

    public void handleOutput(IEFluxMachine machine, List<Slot> slots){
        handleOutput(machine.getMachine(), slots);
    }

    public void handleOutput(EnumRecipeMachine machine, List<Slot> slots){
        getRecipeHandler(machine).processRecipe(slots);
    }

    public void registerRecipe(EnumRecipeMachine machine, Object input, Object output){
        getRecipeHandler(machine).registerRecipe(input, output);
    }

    public IRecipeHandler getRecipeHandler(EnumRecipeMachine machine){
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
        return recipeItemStackHelper.isStackValid(stack) && toCheck != null && recipeStack.getItem() == toCheck.getItem() && toCheck.stackSize >= recipeStack.stackSize &&(toCheck.getItemDamage() == Short.MAX_VALUE || recipeStack.getItemDamage() == toCheck.getItemDamage()) || recipe!ItemStackHelper.isStackValid(stack) && toCheck == null;
    }*/

    static {
        instance.registerHandler(new BasicRecipeHandler(2, 1), EnumRecipeMachine.COMPRESSOR);
        instance.registerHandler(new BasicRecipeHandler(2, 1), EnumRecipeMachine.ETCHINGMACHINE);
    }
}
