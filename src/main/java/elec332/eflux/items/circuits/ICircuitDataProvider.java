package elec332.eflux.items.circuits;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 4-5-2015.
 */
public interface ICircuitDataProvider {

    public ItemStack[] getComponents();

    public String getName();

}
