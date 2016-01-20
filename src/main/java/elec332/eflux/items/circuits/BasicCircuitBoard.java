package elec332.eflux.items.circuits;

import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class BasicCircuitBoard extends AbstractCircuit {//extends CircuitBase {
    protected BasicCircuitBoard(int i) {
        super("BasicCircuitBoard", i);
    }

    @Override
    public EnumCircuit getDifficulty() {
        return EnumCircuit.SMALL;
    }

    @Override
    protected ResourceLocation getTexture() {
        return new EFluxResourceLocation("items/board");
    }

}
