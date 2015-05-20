package elec332.eflux.api.circuit;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 4-5-2015.
 */
public interface ICircuit {

    public int boardSize(ItemStack stack);

    public ItemStack getRequiredComponent(int i, ItemStack stack);

    public void breakRandomComponent(ItemStack stack);

    public boolean isValid(ItemStack stack);

    public EnumCircuit getDifficulty();

    public boolean isCircuit(ItemStack stack);

}
