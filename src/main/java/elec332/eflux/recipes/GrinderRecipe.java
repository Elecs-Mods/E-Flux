package elec332.eflux.recipes;

import elec332.eflux.util.EnumMachines;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 13-4-2015.
 */
public class GrinderRecipe implements IEFluxRecipe {

    public GrinderRecipe(ItemStack input, ItemStack output){
        this.input = input;
        this.output = output;
    }

    private ItemStack input;
    private ItemStack output;

    @Override
    public int getEnergyCost() {
        return 2300;
    }

    @Override
    public ItemStack[] getIngredients() {
        return new ItemStack[]{this.input};
    }

    @Override
    public boolean isShapeless() {
        return true;
    }

    @Override
    public EnumMachines getMachine() {
        return EnumMachines.GRINDER;
    }

    @Override
    public ItemStack[] getResult() {
        return new ItemStack[]{this.output};
    }

    @Override
    public boolean multipleInput() {
        return false;
    }
}
