package elec332.eflux.grid.energy5;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.util.ConnectionPoint;

/**
 * Created by Elec332 on 18-11-2017.
 */
public class CPPosObj {

	Multimap<ConnectionPoint, CircuitElement> connections = HashMultimap.create();

}
