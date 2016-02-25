package elec332.eflux.api.circuit;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 4-5-2015.
 */
public interface ICircuit {

    public int boardSize(ItemStack stack);

    public ItemStack getRequiredComponent(ItemStack stack, int slot);

    public void breakRandomComponent(ItemStack stack);

    public boolean isValid(ItemStack stack);

    public EnumCircuit getDifficulty(ItemStack stack);

    public boolean isCircuit(ItemStack stack);

}
