package elec332.eflux.util;

import com.google.common.base.Preconditions;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.energy.IWireType;
import elec332.eflux.api.energy.WireConnectionMethod;
import elec332.eflux.api.energy.WireThickness;
import net.minecraft.item.EnumDyeColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

/**
 * Created by Elec332 on 19-11-2017.
 */
public class WireData {

	public WireData(IWireType wireType, WireThickness thickness, WireConnectionMethod connectionMethod, EnergyType energyType){
		this(wireType, thickness, connectionMethod, energyType, null);

	}

	public WireData(IWireType wireType, WireThickness thickness, WireConnectionMethod connectionMethod, EnergyType energyType, EnumDyeColor color){
		this.wireType = Preconditions.checkNotNull(wireType);
		this.thickness = Preconditions.checkNotNull(thickness);
		this.connectionMethod = Preconditions.checkNotNull(connectionMethod);
		this.energyType = Preconditions.checkNotNull(energyType);
		this.color = color;
	}

	private IWireType wireType;
	private WireConnectionMethod connectionMethod;
	private WireThickness thickness;
	private EnergyType energyType;
	@Nullable
	private EnumDyeColor color;

	public double getResistivity(double length){
		Preconditions.checkArgument(length > 0);
		return length * (wireType.getResistivity() / (thickness.surfaceAreaR * 0.001 * 0.001));
	}

	@Nonnull
	public EnergyType getEnergyType() {
		return energyType;
	}

	@Nonnull
	public WireConnectionMethod getConnectionMethod() {
		return connectionMethod;
	}

	@Nullable
	public EnumDyeColor getColor() {
		return color;
	}

	public Color getWireTypeColor(){
		return wireType.getColor();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof WireData && equals_((WireData) obj);
	}

	private boolean equals_(WireData wireData){
		return wireType == wireData.wireType && connectionMethod == wireData.connectionMethod && thickness == wireData.thickness && energyType == wireData.energyType && color == wireData.color;
	}

}
