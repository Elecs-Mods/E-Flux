package elec332.eflux.recipes.old;

import net.minecraft.inventory.Slot;

import java.util.List;

/**
 * Created by Elec332 on 18-5-2015.
 */
public interface IRecipeHandler {

    public boolean hasOutput(List<Slot> slots);

    public void processRecipe(List<Slot> slots);

    public void registerRecipe(Object input, Object output);

}
