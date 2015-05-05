package elec332.eflux.api.circuit;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 4-5-2015.
 */
public interface IElectricComponent {

    public ItemStack getBroken(ItemStack stack);

    public boolean isBroken();

}
