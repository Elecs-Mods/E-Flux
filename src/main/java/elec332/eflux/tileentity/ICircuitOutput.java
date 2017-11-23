package elec332.eflux.tileentity;

/**
 * Created by Elec332 on 9-11-2017.
 */
public interface ICircuitOutput extends IWireConnector {

	@Override
	default public int getMaxDifferentConnections(){
		return 2;
	}

	@Override
	default int getMaxConnectionsPerPoint() {
		return 1;
	}

}
