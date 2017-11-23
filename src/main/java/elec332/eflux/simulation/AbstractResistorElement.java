package elec332.eflux.simulation;

import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergyReceiver;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 12-11-2017.
 */
public abstract class AbstractResistorElement<T extends IEnergyObject> extends CircuitElement<T> {

	public AbstractResistorElement(T receiver) {
		super(receiver);
	}


	public abstract double getResistance();

	@Nullable
	protected abstract IEnergyReceiver getReceiver();

	@Override
	protected void calculateCurrent() {
		current = getVoltageDiff() / getResistance();
	}

	@Override
	public void stamp() {
		getCircuit().stampResistor(nodes[0], nodes[1], getResistance());
	}

	@Override
	public abstract void apply();

}
