package elec332.eflux.items.circuits;

import elec332.eflux.api.circuit.ICircuit;

/**
 * Created by Elec332 on 29-5-2016.
 */
public interface IEFluxCircuit extends ICircuit {

    public void etch(ICircuitDataProvider dataProvider);

}
