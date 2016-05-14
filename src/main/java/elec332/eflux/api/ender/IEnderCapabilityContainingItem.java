package elec332.eflux.api.ender;

import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 5-5-2016.
 */
public interface IEnderCapabilityContainingItem {

    public IEnderCapabilityFactory getCapabilityFactory(ItemStack stack);

}
