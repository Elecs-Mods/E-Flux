package elec332.eflux.simulation;

import com.google.common.collect.Lists;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.util.ConnectionPoint;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 15-11-2017.
 */
public abstract class CompressedCircuitElement<T extends CircuitElement> extends CircuitElement<IEnergyObject> {

	protected CompressedCircuitElement(ConnectionPoint start, ConnectionPoint end, List<T> elements) {
		this(new IEnergyObject() {

			@Nonnull
			@Override
			public EnergyType getEnergyType(int post) {
				throw new RuntimeException();
			}

			@Nonnull
			@Override
			public ConnectionPoint getConnectionPoint(int post) {
				return post == 0 ? start : end;
			}

			@Nullable
			@Override
			public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
				throw new RuntimeException();
			}

		}, elements);
	}

	protected CompressedCircuitElement(IEnergyObject energyTile, List<T> elements) {
		super(energyTile);
		if (elements == null){
			elements = Lists.newArrayList();
		}
		this.elements = elements;
	}

	protected final List<T> elements;

	@Override
	public final void apply() {
		for (T t : elements){
			t.apply();
		}
	}

}
