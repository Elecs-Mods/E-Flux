package elec332.eflux.grid.energy4;

import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.energy.WireConnectionMethod;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.util.WireData;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class Wire implements IEnergyObject {

	public Wire(ConnectionPoint start, Vec3d startV, ConnectionPoint end, Vec3d endV, WireData wireData){
		this.startPos = start;
		this.endPos = end;
		this.start = startV;
		this.end = endV;
		this.renderStart = startV.addVector(start.getPos().getX(), start.getPos().getY(), start.getPos().getZ());
		this.renderEnd = endV.addVector(end.getPos().getX(), end.getPos().getY(), end.getPos().getZ());
		this.wireData = wireData;
	}

	private Vec3d start, end, renderStart, renderEnd;
	private ConnectionPoint startPos, endPos;
	private WireData wireData;

	public Vec3d getStart() {
		return renderStart;
	}

	public Vec3d getEnd() {
		return renderEnd;
	}

	public double getLength(){
		return start.distanceTo(end);
	}

	public void drop(){

	}

	public boolean isOverhead(){
		return wireData.getConnectionMethod() == WireConnectionMethod.OVERHEAD;
	}

	//@Override
	public double getResistance() {
		return wireData.getResistivity(getLength());
	}

	@Nonnull
	@Override
	public EnergyType getEnergyType(int post) {
		return wireData.getEnergyType();
	}

	@Nonnull
	@Override
	public ConnectionPoint getConnectionPoint(int post) {
		return post == 0 ? startPos : endPos;
	}

	@Nullable
	@Override
	public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
		return null;
	}

}
