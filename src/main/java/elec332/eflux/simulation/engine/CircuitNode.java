package elec332.eflux.simulation.engine;

import elec332.eflux.api.util.ConnectionPoint;

import java.util.Vector;

public final class CircuitNode {

	CircuitNode() {
		links = new Vector<>();
	}

	ConnectionPoint cp;
	Vector<CircuitNodeLink> links;
	boolean internal;

}
