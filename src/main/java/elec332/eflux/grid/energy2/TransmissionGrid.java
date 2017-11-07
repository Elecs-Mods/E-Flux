package elec332.eflux.grid.energy2;

/**
 * Created by Elec332 on 6-11-2017.
 */
public class TransmissionGrid {



	/**
	 * Part of a wire network.
	 * There is 1 "WirePart" between every joint.
	 */
	private static class WirePart {

		public float length;
		public float radius;
		public double resistivity;

		public boolean hasEndPoint;

	}

}
