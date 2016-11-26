package elec332.eflux.api.circuit;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public interface ICircuit {

    public int boardSize();

    @Nullable
    public ItemStack getRequiredComponent(int slot);

    public void breakRandomComponent();

    public void validate();

    public boolean isValidCircuit();

    public EnumCircuit getDifficulty();

    public boolean isEtchedCircuit();

    @Nonnull
    public List<ItemStack> getSolderedComponents();

    public void setSolderedComponents(List<ItemStack> components);

    @Nullable
    public ResourceLocation getCircuitName();

}
