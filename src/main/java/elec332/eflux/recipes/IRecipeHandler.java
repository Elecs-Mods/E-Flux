package elec332.eflux.recipes;

import elec332.eflux.recipes.old.EnumRecipeMachine;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 21-1-2016.
 */
public interface IRecipeHandler<T extends EnumRecipeMachine> {

    public T getType();

    public boolean canHandle(ItemStack stack);

    public ItemStack handle(ItemStack stack);

}
