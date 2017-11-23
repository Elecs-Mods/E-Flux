 package elec332.eflux.simulation;

import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.grid.energy4.Wire;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 13-11-2017.
 */
public class WireElement extends AbstractResistorElement<Wire> {

	protected WireElement(Wire energyTile) {
		super(energyTile);
	}

	@Override
	public double getResistance() {
		return 0.000001;
	}

	@Nullable
	@Override
	protected IEnergyReceiver getReceiver() {
		return null;
	}

	@Override
	public void apply() {

	}

	/*@Override
	public void stamp() {
		getCircuit().stampVoltageSource(nodes[0], nodes[1], voltSource, 0);
		getCircuit().stampResistor(nodes[0], nodes[1], 1);
	}

	@Override
	public int getVoltageSourceCount() {
		return 1;
	}

	@Override
	protected double getPower() {
		return 0;
	}

	@Override
	protected double getVoltageDiff() {
		return volts[0];
	}

	@Override
	public boolean isWire() {
		return true;
	}*/

}
