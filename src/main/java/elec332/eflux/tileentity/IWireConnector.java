package elec332.eflux.tileentity;

import elec332.eflux.api.energy.IResistor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

/**
 * Created by Elec332 on 7-11-2017.
 */
public interface IWireConnector extends IResistor {

	default public int getMaxDifferentConnections(){
		return 1;
	}

	default public int getMaxConnectionsPerPoint(){
		return 2;
	}

	public int getConnectionLocationFromHitVec(EnumFacing side, Vec3d hit);

}
