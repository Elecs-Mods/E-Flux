package elec332.eflux.tileentity;

import elec332.core.tile.TileBase;
import elec332.eflux.grid.energy4.Wire;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class TileOverheadWireBreakout extends TileBase {

	private final EnumFacing connection = EnumFacing.UP;

	private Wire wire1, wire2;
	private boolean shortCircuit;

	public double getResistivity(){
		if (shortCircuit){
			return 0;
		}
		return 1;
	}

}
