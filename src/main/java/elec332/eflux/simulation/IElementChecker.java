package elec332.eflux.simulation;

import elec332.eflux.api.energy.circuit.CircuitElement;

import java.util.Collection;
import java.util.List;

/**
 * Created by Elec332 on 16-11-2017.
 */
public interface IElementChecker<T extends CircuitElement> {

	public boolean elementsValid(Collection<T> elements);

}
