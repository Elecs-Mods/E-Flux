package elec332.eflux.simulation;

import com.google.common.collect.Multimap;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.util.ConnectionPoint;

import java.util.List;

/**
 * Created by Elec332 on 14-11-2017.
 */
public interface ICircuitCompressor {

	public Multimap<CompressedCircuitElement, CircuitElement> compress(List<CircuitElement> elements, Multimap<ConnectionPoint, CircuitElement<?>> map2);

}
