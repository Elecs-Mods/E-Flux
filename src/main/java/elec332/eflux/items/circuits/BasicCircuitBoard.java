package elec332.eflux.items.circuits;

import elec332.eflux.api.circuit.AbstractCircuit;
import elec332.eflux.api.circuit.EnumCircuit;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class BasicCircuitBoard extends AbstractCircuit{//extends CircuitBase {
    protected BasicCircuitBoard(int i) {
        super("BasicCircuitBoard", i);
    }

    @Override
    public EnumCircuit getDifficulty() {
        return EnumCircuit.SMALL;
    }

}
