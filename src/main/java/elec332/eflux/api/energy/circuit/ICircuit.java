package elec332.eflux.api.energy.circuit;

import java.util.UUID;

/**
 * Created by Elec332 on 14-11-2017.
 */
public interface ICircuit {

	public UUID getId();

	public void stampVCVS(int n1, int n2, double coef, int vs);

	// stamp independent voltage source #vs, from n1 to n2, amount v
	public void stampVoltageSource(int n1, int n2, int vs, double v);

	// use this if the amount of voltage is going to be updated in doStep()
	public void stampVoltageSource(int n1, int n2, int vs);

	public void updateVoltageSource(int n1, int n2, int vs, double v);

	public void stampResistor(int n1, int n2, double r);

	public void stampConductance(int n1, int n2, double r0);

	// current from cn1 to cn2 is equal to voltage from vn1 to 2, divided by g
	public void stampVCCurrentSource(int cn1, int cn2, int vn1, int vn2, double g);

	public void stampCurrentSource(int n1, int n2, double i);

	// stamp a current source from n1 to n2 depending on current through vs
	public void stampCCCS(int n1, int n2, int vs, double gain);

	// stamp value x in row i, column j, meaning that a voltage change
	// of dv in node j will increase the current into node i by x dv.
	// (Unless i or j is a voltage source node.)
	public void stampMatrix(int i, int j, double x);

	// stamp value x on the right side of row i, representing an
	// independent current source flowing into node i
	public void stampRightSide(int i, double x);

	// indicate that the value on the right side of row i changes in doStep()
	public void stampRightSide(int i);

	// indicate that the values on the left side of row i change in doStep()
	public void stampNonLinear(int i);

}
