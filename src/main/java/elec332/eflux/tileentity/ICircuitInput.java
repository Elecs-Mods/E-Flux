package elec332.eflux.tileentity;

import elec332.eflux.api.energy.IEnergySource;

/**
 * Created by Elec332 on 9-11-2017.
 */
public interface ICircuitInput extends IWireConnector {

	@Override
	default public int getMaxDifferentConnections(){
		return 2;
	}

	@Override
	default int getMaxConnectionsPerPoint() {
		return 1;
	}

	@Override
	default double getResistance(){
		return -1;
	}

	public IEnergySource getGenerator();

}
