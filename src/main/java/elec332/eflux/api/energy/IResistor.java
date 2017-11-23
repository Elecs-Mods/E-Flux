package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 9-11-2017.
 */
public interface IResistor {

	/**
	 * Unfortunately, it is not possible to let this depend on the voltage (which it really should).
	 *
	 * Reason: Lets take a very simple circuit, with 2 wires (1ohm each) leading to a machine with a
	 * resistance of R(Vmachine) (unknown, because it is a function). Now lets try to solve this:
	 * Lets say we have a power source capable of providing unlimited power at 100Volts.
	 * Total resistance would be 2 + R(Vmachine)
	 * Current would be 100 / (2 + R(Vmachine))
	 * The "lost" voltage would be:
	 * Vlost = 2 * (100 / (2 + R(Vmachine)))
	 * Vmachine = 100 - Vlost
	 * VMachine = 100 - 2 * (100 / (2 + R(Vmachine)))
	 *
	 * The above function cannot be solved (without guesswork, or interpolation) in Java,
	 * as we have no idea what R(Vmachine) looks like.
	 * (And this function would become event more difficult with EG more machines in parralel)
	 *
	 * @return The current resistance for this receiver
	 */
	public double getResistance(/*int ef*/);

}
