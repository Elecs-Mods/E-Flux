package elec332.eflux.grid.energy5;

import elec332.core.grid.IPositionable;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.util.ConnectionPoint;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-11-2017.
 */
public final class ETileEntityLink implements IPositionable {

	protected ETileEntityLink(TileEntity tile) {
		pos = DimensionCoordinate.fromTileEntity(tile);
	}

	private final DimensionCoordinate pos;
	private ConnectionPoint[] cp;

	@Nonnull
	@Override
	public DimensionCoordinate getPosition() {
		return pos;
	}

	@Override
	public boolean hasChanged() {
		if (cp == null){
			if (pos.isLoaded()){
				TileEntity tile = pos.getTileEntity();
				if (tile == null){
					return true;
				}
				IEnergyObject eio = tile.getCapability(EFluxAPI.ENERGY_CAP, null);
				if (eio == null){
					return true;
				}
				cp = new ConnectionPoint[eio.getPosts()];
				for (int i = 0; i < cp.length; i++) {
					cp[i] = eio.getConnectionPoint(i);
				}
				return true;
			} else {
				return false;
			}
		} else {
			if (pos.isLoaded()){
				TileEntity tile = pos.getTileEntity();
				if (tile == null){
					return true;
				}
				IEnergyObject eio = tile.getCapability(EFluxAPI.ENERGY_CAP, null);
				if (eio == null) {
					return true;
				}
				ConnectionPoint[] newCP = new ConnectionPoint[eio.getPosts()];
				for (int i = 0; i < newCP.length; i++) {
					newCP[i] = eio.getConnectionPoint(i);
				}
				if (newCP.length != cp.length){
					cp = newCP;
					return true;
				} else {
					boolean ch = false;
					for (int i = 0; i < cp.length; i++) {
						if (!newCP[i].equals(cp[i])){
							ch = true;
							break;
						}
					}
					cp = newCP;
					return ch;
				}
			} else {
				cp = null;
				return true;
			}
		}
	}

}
