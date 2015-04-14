package elec332.eflux.recipes;

import elec332.eflux.util.EnumMachines;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 13-4-2015.
 */
public interface IEFluxRecipe {

    public int getEnergyCost();

    public ItemStack[] getIngredients();

    public boolean isShapeless();

    public EnumMachines getMachine();

    public ItemStack[] getResult();

    public boolean multipleInput();
}
