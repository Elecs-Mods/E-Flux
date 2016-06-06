package elec332.eflux.api.circuit;

import elec332.eflux.api.EFluxAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Created by Elec332 on 4-6-2016.
 */
@SuppressWarnings("all")
public class CircuitHelper {

    @Nullable
    public static EnumCircuit getCircuitLevel(ItemStack stack){
        ICircuit circuit = getCircuit(stack);
        if (circuit != null){
            return circuit.getDifficulty();
        }
        return null;
    }

    @Nullable
    public static ICircuit getCircuit(ItemStack stack){
        if (stack == null || !stack.hasCapability(EFluxAPI.EFLUX_CIRCUIT_CAPABILITY, null)){
            return null;
        }
        return stack.getCapability(EFluxAPI.EFLUX_CIRCUIT_CAPABILITY, null);
    }

    public static boolean isValidCircuit(ItemStack stack){
        ICircuit circuit = getCircuit(stack);
        return circuit != null && circuit.isValidCircuit();
    }

    public static boolean isEtchedCircuit(ItemStack stack){
        ICircuit circuit = getCircuit(stack);
        return circuit != null && circuit.isEtchedCircuit();
    }

    public static int getCircuitBoardSize(ItemStack stack){
        ICircuit circuit = getCircuit(stack);
        return circuit == null ? 0 : circuit.boardSize();
    }

    public static ItemStack getRequiredCircuitComponent(ItemStack stack, int slot){
        ICircuit circuit = getCircuit(stack);
        return circuit == null ? null : circuit.getRequiredComponent(slot);
    }

    public static boolean isCircuitType(ItemStack stack, @Nonnull ResourceLocation name){
        ICircuit circuit = getCircuit(stack);
        return circuit != null && Objects.requireNonNull(name).equals(circuit.getCircuitName());
    }

}
