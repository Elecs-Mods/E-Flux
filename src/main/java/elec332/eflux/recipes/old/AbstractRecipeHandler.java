package elec332.eflux.recipes.old;

import com.google.common.collect.Lists;
import elec332.core.inventory.slot.SlotOutput;
import elec332.core.util.BasicInventory;
import elec332.eflux.util.RecipeItemStack;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 17-5-2015.
 */
public abstract class AbstractRecipeHandler implements IRecipeHandler{

    protected AbstractRecipeHandler(){
        this.multipleRecipes = new HashMap<AbstractList<RecipeItemStack>, ItemStack[]>();
    }

    private HashMap<AbstractList<RecipeItemStack>, ItemStack[]> multipleRecipes;

    protected void addRecipe(AbstractList<RecipeItemStack> input, ItemStack[] output){
        if (input.contains(null))
            throw new IllegalArgumentException("Why would a recipe contain a null ItemStack?!?");
        if (multipleRecipes.keySet().contains(input))
            throw new RuntimeException("Cannot register this recipe, another recipe with the same input has already been registered");
        multipleRecipes.put(input, output);
    }

    @Override
    public boolean hasOutput(List<Slot> slots) {
        return handle(false, slots);
    }

    @Override
    public void processRecipe(List<Slot> slots) {
        handle(true, slots);
    }

    public boolean handle(boolean b, List<Slot> slots){
        List<SlotOutput> outputSlots= Lists.newArrayList();
        List<Slot> inputSlots = Lists.newArrayList();
        for (Slot slot : slots){
            if (slot instanceof SlotOutput)
                outputSlots.add((SlotOutput) slot);
            else inputSlots.add(slot);
        }
        ///////////////////////////////////////////
        return canProcess(outputSlots, inputSlots, b);
    }

    private boolean canProcess(List<SlotOutput> outputSlots, List<Slot> inputSlots, boolean consume){
        boolean doConsume = false;
        if (consume)
            doConsume = canProcess(outputSlots, inputSlots, false);
        AbstractList<RecipeItemStack> stacks = Lists.newArrayList();
        List<Slot> usedInputSlots = Lists.newArrayList();
        for (Slot slot : inputSlots){
            if (slot.getStack() != null) {
                stacks.add(new RecipeItemStack(slot.getStack()));
                usedInputSlots.add(slot);
            }
        }
        Map.Entry<AbstractList<RecipeItemStack>, ItemStack[]> entry = getEntry(stacks);
        //Yep, some excessive null-checking below
        if (entry != null && entry.getKey() != null && entry.getKey().size() > 0 && entry.getValue() != null && entry.getValue().length > 0 && outputSlots != null && outputSlots.size() > 0 && inputSlots != null && inputSlots.size() > 0){
            for (int i = 0; i < entry.getValue().length; i++) {
                ItemStack stack = entry.getValue()[i];
                SlotOutput slotOutput = outputSlots.get(i);
                if (((BasicInventory)slotOutput.inventory).canAddItemStackFully(stack, slotOutput.getSlotIndex(), true)){
                    if (doConsume) {
                        if (slotOutput.getStack() == null)
                            slotOutput.putStack(stack.copy());
                        else slotOutput.getStack().stackSize += stack.stackSize;
                    }
                } else return false;
            }
            if (doConsume) {
                for (int i = 0; i < entry.getKey().size(); i++) {
                    RecipeItemStack recipeItemStack = entry.getKey().get(i);
                    Slot inputSlot = usedInputSlots.get(i);
                    inputSlot.getStack().stackSize -= recipeItemStack.getStackSize();
                    if (inputSlot.getStack().stackSize <= 0)
                        inputSlot.putStack(null);
                }
            }
            return true;
        } else return false;
    }

    private Map.Entry<AbstractList<RecipeItemStack>, ItemStack[]> getEntry(AbstractList<RecipeItemStack> recipe){
        try {
            return Lists.newArrayList(multipleRecipes.entrySet()).get(Lists.newArrayList(multipleRecipes.keySet()).indexOf(recipe));
        } catch (Exception e){
            return null;
        }
    }
}
