package elec332.eflux.util;

import elec332.eflux.api.energy.EnergyType;

/**
 * Created by Elec332 on 5-11-2017.
 */
public class Formulas {

	public double getTotalPowerLoss(double current, float resistivity, float radius, float length, float cableDistance, double voltage, EnergyType type){
		double resistance = getWireResistance(resistivity, radius, length);
		double normalLoss = getConductorLoss(current, resistance);
		double coronaLoss = getCoronaLoss(current, resistivity, radius, length, type);
		if (type == EnergyType.DC){
			return normalLoss + coronaLoss;
		} else {
			return normalLoss + coronaLoss + getDielectricLoss(current, Constants.DIELECTRIC_CONSTANT_VACUUM, cableDistance, radius, length) + getPowerFactorLoss(voltage, current);//plus some more
		}
	}

	public double getVoltageDrop(double voltage, double wireResistance, double otherResistance, EnergyType type){
		double amp = voltage / (wireResistance + otherResistance);
		return wireResistance * amp;
	}

	private double getPowerFactorLoss(double voltage, double current){
		return (voltage * current) - (voltage * (Constants.AC_POWER_FACTOR * current));
	}

	private double getConductorLoss(double current, double resistance){
		return current * current * resistance;
	}

	private double getDielectricLoss(double current, double permittivity, float distance, float radius, float length){
		return getSimplifiedDielectricLoss(current, permittivity, distance, radius, length);
	}

	private double getSimplifiedDielectricLoss(double current, double permittivity, float distance, float radius, float length){
		return getConductorLoss(current, getWireResistance(2 * Math.PI * getCapacitance(permittivity, distance, radius) * Constants.AC_LINE_FREQUENCY, radius, length));
	}

	private double getCapacitance(double permittivity, float distance, float radius){
		return (Math.PI * permittivity)/Math.log(distance/radius);
	}

	//Sorry, not yet
	private double getCoronaLoss(double current, float resM, float straal, float length, EnergyType type){
		return 0;
	}

	private double getWireResistance(double resM, float straal, float length){
		float surfaceArea = (float) (straal * straal * Math.PI);
		return (resM * length) / surfaceArea;
	}

}
