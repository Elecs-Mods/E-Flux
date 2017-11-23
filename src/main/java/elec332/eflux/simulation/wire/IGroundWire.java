package elec332.eflux.simulation.wire;

import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.grid.energy4.Wire;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 19-11-2017.
 */
public interface IGroundWire extends IEnergyObject {

	public Wire[] getWires();

	@Nullable
	public ConnectionPoint getForColor(EnumDyeColor color, EnumFacing side);

	@Override
	default String getDescription(int post) {
		Wire wire = getWires()[post];
		return ;
	}

}
