package elec332.eflux.grid.energy4;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import elec332.core.grid.IPositionable;
import elec332.core.world.DimensionCoordinate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class WireNode implements IPositionable, INBTSerializable<NBTTagCompound> {

	protected WireNode(DimensionCoordinate coord) {
		this.coord = coord;
		this.wires = HashMultimap.create();
	}

	private final DimensionCoordinate coord;
	protected Multimap<Integer, Wire> wires;
	protected Map<Integer, WireGrid> grids;

	@Nonnull
	@Override
	public DimensionCoordinate getPosition() {
		return coord;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return null;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {

	}

}
