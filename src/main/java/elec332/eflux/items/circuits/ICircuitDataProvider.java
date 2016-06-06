package elec332.eflux.items.circuits;

import elec332.eflux.api.circuit.EnumCircuit;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 4-5-2015.
 */
public abstract class ICircuitDataProvider extends IForgeRegistryEntry.Impl<ICircuitDataProvider> {

    @Nonnull
    public abstract ItemStack[] getComponents();

    @Nonnull
    public abstract EnumCircuit getCircuitType();

}
