package elec332.eflux.util;

import elec332.eflux.api.energy.IWireType;

import java.awt.*;

/**
 * Created by Elec332 on 9-11-2017.
 */
public enum TestWires implements IWireType {

	TEST1(8.6)// * Math.pow(10, -6))

	;

	TestWires(double r){
		this.r = r;
	}

	private final double r;

	@Override
	public double getResistivity() {
		return r;
	}

	@Override
	public double getMassM3() {
		return 0;
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}

}
