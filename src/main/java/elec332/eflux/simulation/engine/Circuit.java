package elec332.eflux.simulation.engine;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.energy.circuit.ICircuit;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.simulation.CircuitElementFactory;
import elec332.eflux.simulation.CompressedCircuitElement;
import elec332.eflux.simulation.ICircuitCompressor;

import java.util.*;

/**
 * Created by Elec332 on 11-11-2017.
 */
public final class Circuit implements ICircuit {

	public boolean needsRebuild;
	public final UUID uuid = UUID.randomUUID();
	public final Object SYNC_OBJ = new Object();
	public boolean rebuilding = false;

	double circuitMatrix[][], circuitRightSide[], origRightSide[], origMatrix[][];
	RowInfo circuitRowInfo[];
	int circuitPermute[];
	boolean circuitNonLinear;
	int voltageSourceCount;
	int circuitMatrixSize, circuitMatrixFullSize;
	boolean circuitNeedsMap;
	CircuitElement stopElm;
	boolean dumpMatrix;
	int steps = 0;
	Vector<CircuitNode> nodeList;
	CircuitElement voltageSources[];
	boolean converged;
	int subIterations;
	String stopMessage;
	private Set<CircuitElement<?>> elmList = Sets.newHashSet();
	private List<CircuitElement<?>> compressedElm = null;
	Multimap<Class, CircuitElement> sortedElements = HashMultimap.create();

	List<CircuitElement<?>> getCompressedElementList(){
		if (compressedElm == null){
			compressList();
		}
		return compressedElm;
	}

	public boolean isRebuilding(){
		//synchronized (uuid){
			return rebuilding;
		//}
	}

	private void compressList(){
		long time = System.currentTimeMillis();
		List<CircuitElement<?>> copy = Lists.newArrayList(elmList), immC = Collections.unmodifiableList(copy);
		Multimap<ConnectionPoint, CircuitElement<?>> data = getData(copy);
		for (ICircuitCompressor c : CircuitElementFactory.INSTANCE.getCircuitOptimizers()){
			Multimap<CompressedCircuitElement, CircuitElement> p = c.compress(Lists.newArrayList(immC), data);
			if (p.keySet().size() > 0){
				boolean doneWork = false;
				for (CompressedCircuitElement ce1 : p.keySet()){
					Collection<CircuitElement> r = p.get(ce1);
					if (r == null || r.size() == 0){
						continue;
					}
					doneWork = true;
					copy.removeAll(p.get(ce1));
					copy.add(ce1);
					ce1.setCircuit(this);
				}
				if (doneWork){
					data = getData(copy);
				}
			}
		}
		this.compressedElm = copy;
		System.out.println("Compression took: "+(System.currentTimeMillis() - time));
	}

	private Multimap<ConnectionPoint, CircuitElement<?>> getData(List<CircuitElement<?>> elements){
		Multimap<ConnectionPoint, CircuitElement<?>> ret2 = HashMultimap.create();
		elements.forEach(e -> e.getConnectionPoints().forEach(c -> ret2.put(c, e)));
		return ret2;
	}

	public void addElement(CircuitElement<?> element){
		if (element.getCircuit() != null && element.getCircuit() != this){
			throw new RuntimeException();
		}
		if (elmList.add(element)) {
			sortedElements.put(element.getClass(), element);
			element.setCircuit(this);
			compressedElm = null;
			needsRebuild = true;
		}
	}

	public void consumeCircuit(Circuit circuit){
		circuit.elmList.forEach(element -> {
			element.setCircuit(null);
			Circuit.this.addElement(element);
		});
	}

	public boolean removeElement(CircuitElement<?> element, Set<CircuitElement<?>> reAdd){
		elmList.remove(element);
		sortedElements.remove(element.getClass(), element);
		compressedElm = null;
		needsRebuild = true;
		reAdd.addAll(elmList);
		elmList.forEach(element1 -> element1.setCircuit(null));
		return true;
	}

	public void clear(){
		elmList.clear();
		sortedElements.clear();
		sortedElements.clear();
		compressedElm = null;
	}

	CircuitNode getCircuitNode(int n) {
		if (n >= nodeList.size()) {
			return null;
		}
		return nodeList.elementAt(n);
	}

	CircuitElement getElm(int n) {
		if (n >= compressedElm.size()) {
			return null;
		}
		return compressedElm.get(n);
	}

	@Override
	public UUID getId() {
		return uuid;
	}

	// control voltage source vs with voltage from n1 to n2 (must
	// also call stampVoltageSource())
	@Override
	public void stampVCVS(int n1, int n2, double coef, int vs) {
		int vn = nodeList.size() + vs;
		stampMatrix(vn, n1, coef);
		stampMatrix(vn, n2, -coef);
	}

	// stamp independent voltage source #vs, from n1 to n2, amount v
	@Override
	public void stampVoltageSource(int n1, int n2, int vs, double v) {
		int vn = nodeList.size() + vs;
		stampMatrix(vn, n1, -1);
		stampMatrix(vn, n2, 1);
		stampRightSide(vn, v);
		stampMatrix(n1, vn, 1);
		stampMatrix(n2, vn, -1);
	}

	// use this if the amount of voltage is going to be updated in doStep()
	@Override
	public void stampVoltageSource(int n1, int n2, int vs) {
		int vn = nodeList.size() + vs;
		stampMatrix(vn, n1, -1);
		stampMatrix(vn, n2, 1);
		stampRightSide(vn);
		stampMatrix(n1, vn, 1);
		stampMatrix(n2, vn, -1);
	}

	@Override
	public void updateVoltageSource(int n1, int n2, int vs, double v) {
		int vn = nodeList.size() + vs;
		stampRightSide(vn, v);
	}

	@Override
	public void stampResistor(int n1, int n2, double r) {
		double r0 = 1 / r;
		if (Double.isNaN(r0) || Double.isInfinite(r0)) {
			System.out.print("bad resistance " + r + " " + r0 + "\n");
		}
		stampConductance(n1, n2, r0);
	}

	@Override
	public void stampConductance(int n1, int n2, double r0) {
		stampMatrix(n1, n1, r0);
		stampMatrix(n2, n2, r0);
		stampMatrix(n1, n2, -r0);
		stampMatrix(n2, n1, -r0);
	}

	// current from cn1 to cn2 is equal to voltage from vn1 to 2, divided by g
	@Override
	public void stampVCCurrentSource(int cn1, int cn2, int vn1, int vn2, double g) {
		stampMatrix(cn1, vn1, g);
		stampMatrix(cn2, vn2, g);
		stampMatrix(cn1, vn2, -g);
		stampMatrix(cn2, vn1, -g);
	}

	@Override
	public void stampCurrentSource(int n1, int n2, double i) {
		stampRightSide(n1, -i);
		stampRightSide(n2, i);
	}

	// stamp a current source from n1 to n2 depending on current through vs
	@Override
	public void stampCCCS(int n1, int n2, int vs, double gain) {
		int vn = nodeList.size() + vs;
		stampMatrix(n1, vn, gain);
		stampMatrix(n2, vn, -gain);
	}

	// stamp value x in row i, column j, meaning that a voltage change
	// of dv in node j will increase the current into node i by x dv.
	// (Unless i or j is a voltage source node.)
	@Override
	public void stampMatrix(int i, int j, double x) {
		if (i > 0 && j > 0) {
			if (circuitNeedsMap) {
				i = circuitRowInfo[i - 1].mapRow;
				RowInfo ri = circuitRowInfo[j - 1];
				if (ri.type == RowInfo.ROW_CONST) {
					circuitRightSide[i] -= x * ri.value;
					return;
				}
				j = ri.mapCol;
			} else {
				i--;
				j--;
			}
			circuitMatrix[i][j] += x;
		}
	}

	// stamp value x on the right side of row i, representing an
	// independent current source flowing into node i
	@Override
	public void stampRightSide(int i, double x) {
		if (i > 0) {
			if (circuitNeedsMap) {
				i = circuitRowInfo[i - 1].mapRow;
			} else {
				i--;
			}
			circuitRightSide[i] += x;
		}
	}

	// indicate that the value on the right side of row i changes in doStep()
	@Override
	public void stampRightSide(int i) {
		if (i > 0) {
			circuitRowInfo[i - 1].rsChanges = true;
		}
	}

	// indicate that the values on the left side of row i change in doStep()
	@Override
	public void stampNonLinear(int i) {
		if (i > 0) {
			circuitRowInfo[i - 1].lsChanges = true;
		}
	}

	void stop(String s, CircuitElement ce) {
		System.out.println("ERROR: "+s);
		stopMessage = s;
		circuitMatrix = null;
		stopElm = ce;
		for (int i = 0; i < 10; i++) {
			System.out.println("ERROR: "+s);
		}
		System.out.println("ERROR: "+s);
	}

}
