package elec332.eflux.api.energy.circuit;

import com.google.common.collect.Lists;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.util.ConnectionPoint;

import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 12-11-2017.
 */
public abstract class CircuitElement<T extends IEnergyObject> {

	protected CircuitElement(T energyTile) {
		this.energyTile = energyTile;
		allocateNodes();
		reset();
	}

	protected int nodes[], voltSource;
	protected double volts[];
	protected double current, curcount;
	private ConnectionPoint[] connectionPoints;
	private List<ConnectionPoint> connectionPoints2;
	protected T energyTile;
	private ICircuit circuit;

	public final void setCircuit(ICircuit circuit){
		this.circuit = circuit;
		reset();
	}

	public void preApply(){

	}

	public void apply(){

	}

	public final UUID getCircuitId(){
		return circuit == null ? null : circuit.getId();
	}

	public void destroy(){
		circuit = null;
		energyTile = null;
		reset();
	}

	public final ICircuit getCircuit(){
		return this.circuit;
	}

	public ConnectionPoint getPost(int i){
		return connectionPoints[i];
	}

	protected final void allocateNodes() {
		int posts = getPostCount();
		int totalNodes = posts + getInternalNodeCount();
		nodes = new int[totalNodes];
		volts = new double[totalNodes];
		connectionPoints = new ConnectionPoint[posts];
		for (int i = 0; i < posts; i++) {
			connectionPoints[i] = energyTile.getConnectionPoint(i);
		}
		connectionPoints2 = Lists.newArrayList(connectionPoints);
	}

	public final List<ConnectionPoint> getConnectionPoints(){
		return connectionPoints2;
	}

	protected void reset() {
		int i;
		for (i = 0; i != getPostCount() + getInternalNodeCount(); i++) {
			volts[i] = 0;
		}
		curcount = 0;
	}

	public void setCurrent(int x, double c) {
		current = c;
	}

	protected double getCurrent() {
		return current;
	}

	public void doStep() {
	}

	public void startIteration() {
	}

	protected final double getPostVoltage(int x) {
		return volts[x];
	}

	public void setNodeVoltage(int n, double c) {
		volts[n] = c;
		calculateCurrent();
	}

	protected void calculateCurrent() {
	}

	public void stamp() {
	}

	public int getVoltageSourceCount() {
		return 0;
	}

	public int getInternalNodeCount() {
		return 0;
	}

	public void setNode(int p, int n) {
		nodes[p] = n;
	}

	public void setVoltageSource(int n, int v) {
		voltSource = v;
	}

	protected int getVoltageSource() {
		return voltSource;
	}

	protected double getVoltageDiff() {
		return volts[0] - volts[1];
	}

	public boolean nonLinear() {
		return false;
	}

	public int getPostCount() {
		return 2;
	}

	public final int getNode(int n) {
		return nodes[n];
	}

	protected double getPower() {
		return getVoltageDiff() * current;
	}

	public boolean getConnection(int n1, int n2) {
		return true;
	}

	public boolean isWire() {
		return false;
	}

	protected boolean canViewInScope() {
		return getPostCount() <= 2;
	}

	protected boolean comparePair(int x1, int x2, int y1, int y2) {
		return ((x1 == y1 && x2 == y2) || (x1 == y2 && x2 == y1));
	}

}
