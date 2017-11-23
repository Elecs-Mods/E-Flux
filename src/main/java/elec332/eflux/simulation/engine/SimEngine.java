package elec332.eflux.simulation.engine;

import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.simulation.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Vector;

@SuppressWarnings("all")
public enum SimEngine {

	INSTANCE;

	public void preTick(Circuit circuit){
		if (circuit.needsRebuild) {
			circuit.rebuilding = true;
			analyzeCircuit(circuit, true);
		}
	}

	public void tick(Circuit circuit){
		long time = System.nanoTime();
		synchronized (circuit) {
			try {
				if (circuit.isRebuilding()) {
					circuit.wait();
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			double dt = (System.nanoTime() - time) / Math.pow(10, 6);
			System.out.println("--Ticking...--");
			System.out.println("Waited " + dt + " ms " + (circuit.rebuilding?"and rebuild is still in progress":""));
			time = System.nanoTime();
			runCircuit(circuit);
			double d = (System.nanoTime() - time) / Math.pow(10, 6);;
			System.out.println("Circuit elements: "+circuit.getCompressedElementList().size());
			System.out.println("Circuit tick " + "took: " + d + "ns");
			System.out.println("--Finished ticking--");
		}
	}

	private void analyzeCircuit(final Circuit circuit, boolean threaded){
		if (threaded) {
			new Thread() {
				@Override
				public void run() {
					long time = System.nanoTime();
					try {
						//Thread.sleep(3);
					} catch (Exception e){

					}
					analyzeCircuit_(circuit);
					System.out.println("Rebuild took: "+((System.nanoTime() - time) / Math.pow(10, 6)));
				}
			}.start();
		} else {
			analyzeCircuit_(circuit);
		}
	}

	private void analyzeCircuit_(Circuit circuit) {
		synchronized (circuit) {
			System.out.println("--Analyzing...--");
			circuit.rebuilding = true;
			List<CircuitElement<?>> elmList = circuit.getCompressedElementList();
			circuit.needsRebuild = false;
			circuit.stopMessage = null;
			circuit.stopElm = null;
			int i, j;
			int vscount = 0;
			circuit.nodeList = new Vector<>();
			CircuitElement volt = null;
			if (elmList.isEmpty()) {
				circuit.rebuilding = false;
				return;  //ez
			}
			//System.out.println("ac1");
			// look for voltage element
			for (i = 0; i != elmList.size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				if (ce instanceof VoltageElement) {
					volt = ce;
					break;
				}
			}

			// if no ground, and no rails, then the voltage elm's first terminal
			// is ground
			if (volt != null) {
				CircuitNode cn = new CircuitNode();
				ConnectionPoint pt = volt.getPost(0);
				cn.cp = pt.copy();
				circuit.nodeList.addElement(cn);
			} else {
				// otherwise allocate extra node for ground
				CircuitNode cn = new CircuitNode();
				cn.cp = ConnectionPoint.NULL_POINT;
				circuit.nodeList.addElement(cn);
			}
			//System.out.println("ac2");

			// allocate nodes and voltage sources
			for (i = 0; i != elmList.size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				int inodes = ce.getInternalNodeCount();
				int ivs = ce.getVoltageSourceCount();
				int posts = ce.getPostCount();

				// allocate a node for each post and match posts to nodes
				for (j = 0; j != posts; j++) {
					ConnectionPoint pt = ce.getPost(j);
					int k;
					for (k = 0; k != circuit.nodeList.size(); k++) {
						CircuitNode cn = circuit.getCircuitNode(k);
						if (pt.equals(cn.cp)) {
							break;
						}
					}
					CircuitNodeLink cnl = new CircuitNodeLink();
					cnl.num = j;
					cnl.elm = ce;
					if (k == circuit.nodeList.size()) {
						CircuitNode cn = new CircuitNode();
						cn.cp = pt.copy();
						cn.links.addElement(cnl);
						ce.setNode(j, circuit.nodeList.size());
						circuit.nodeList.addElement(cn);
					} else {
						circuit.getCircuitNode(k).links.addElement(cnl);
						ce.setNode(j, k);
						// if it's the ground node, make sure the node voltage is 0,
						// cause it may not get set later
						if (k == 0) {
							ce.setNodeVoltage(j, 0);
						}
					}
				}
				for (j = 0; j != inodes; j++) {
					CircuitNode cn = new CircuitNode();
					cn.cp = ConnectionPoint.NULL_POINT;
					cn.internal = true;
					CircuitNodeLink cnl = new CircuitNodeLink();
					cnl.num = j + posts;
					cnl.elm = ce;
					cn.links.addElement(cnl);
					ce.setNode(cnl.num, circuit.nodeList.size());
					circuit.nodeList.addElement(cn);
				}
				vscount += ivs;
			}
			circuit.voltageSources = new CircuitElement[vscount];
			vscount = 0;
			circuit.circuitNonLinear = false;
			//System.out.println("ac3");

			// determine if circuit is nonlinear
			for (i = 0; i != elmList.size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				if (ce.nonLinear()) {
					circuit.circuitNonLinear = true;
				}
				int ivs = ce.getVoltageSourceCount();
				for (j = 0; j != ivs; j++) {
					circuit.voltageSources[vscount] = ce;
					ce.setVoltageSource(j, vscount++);
				}
			}
			circuit.voltageSourceCount = vscount;

			int matrixSize = circuit.nodeList.size() - 1 + vscount;
			circuit.circuitMatrix = new double[matrixSize][matrixSize];
			circuit.circuitRightSide = new double[matrixSize];
			circuit.origMatrix = new double[matrixSize][matrixSize];
			circuit.origRightSide = new double[matrixSize];
			circuit.circuitMatrixSize = circuit.circuitMatrixFullSize = matrixSize;
			circuit.circuitRowInfo = new RowInfo[matrixSize];
			circuit.circuitPermute = new int[matrixSize];

			for (i = 0; i != matrixSize; i++) {
				circuit.circuitRowInfo[i] = new RowInfo();
			}
			circuit.circuitNeedsMap = false;

			// stamp linear circuit elements
			for (i = 0; i != elmList.size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				ce.stamp();
			}
			//System.out.println("ac4");

			// determine nodes that are unconnected
			boolean closure[] = new boolean[circuit.nodeList.size()];
			boolean changed = true;
			closure[0] = true;
			System.out.println("nodes: " + circuit.nodeList.size());
			while (changed) {
				changed = false;
				for (i = 0; i != elmList.size(); i++) {
					CircuitElement ce = circuit.getElm(i);
					// loop through all ce's nodes to see if they are connected
					// to other nodes not in closure
					for (j = 0; j < ce.getPostCount(); j++) {
						if (!closure[ce.getNode(j)]) {
							continue;
						}
						int k;
						for (k = 0; k != ce.getPostCount(); k++) {
							if (j == k) {
								continue;
							}
							int kn = ce.getNode(k);
							if (ce.getConnection(j, k) && !closure[kn]) {
								closure[kn] = true;
								changed = true;
							}
						}
					}
				}
				if (changed) {
					continue;
				}

				// connect unconnected nodes
				for (i = 0; i != circuit.nodeList.size(); i++) {
					if (!closure[i] && !circuit.getCircuitNode(i).internal) {
						//TODO: remove debug code
						System.out.println("node " + i + " unconnected");
						circuit.stampResistor(0, i, 1e8);
						closure[i] = true;
						changed = true;
						break;
					}
				}
			}
			//System.out.println("ac5");

			for (i = 0; i != elmList.size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				// look for voltage source loops
				if ((ce instanceof VoltageElement && ce.getPostCount() == 2) || ce instanceof WireElement) {
					FindPathInfo fpi = new FindPathInfo(FindPathInfo.VOLTAGE, ce, ce.getNode(1), circuit);
					if (fpi.findPath(ce.getNode(0))) {
						circuit.stop("Voltage source/wire loop with no resistance!", ce);
						circuit.rebuilding = false;
						return;
					}
				}
			}
			//System.out.println("ac6");

			// simplify the matrix; this speeds things up quite a bit
			for (i = 0; i != matrixSize; i++) {
				int qm = -1, qp = -1;
				double qv = 0;
				RowInfo re = circuit.circuitRowInfo[i];
				if (re.lsChanges || re.dropRow || re.rsChanges) {
					continue;
				}
				double rsadd = 0;

				// look for rows that can be removed
				for (j = 0; j != matrixSize; j++) {
					double q = circuit.circuitMatrix[i][j];
					if (circuit.circuitRowInfo[j].type == RowInfo.ROW_CONST) {
						// keep a running total of const values that have been
						// removed already
						rsadd -= circuit.circuitRowInfo[j].value * q;
						continue;
					}
					if (q == 0) {
						continue;
					}
					if (qp == -1) {
						qp = j;
						qv = q;
						continue;
					}
					if (qm == -1 && q == -qv) {
						qm = j;
						continue;
					}
					break;
				}
				if (j == matrixSize) {
					if (qp == -1) {
						circuit.stop("Matrix error", null);
						return;
					}
					RowInfo elt = circuit.circuitRowInfo[qp];
					if (qm == -1) {
						// we found a row with only one nonzero entry; that value
						// is a constant
						int k;
						for (k = 0; elt.type == RowInfo.ROW_EQUAL && k < 100; k++) {
							// follow the chain
							qp = elt.nodeEq;
							elt = circuit.circuitRowInfo[qp];
						}
						if (elt.type == RowInfo.ROW_EQUAL) {
							// break equal chains
							//System.out.println("Break equal chain");
							elt.type = RowInfo.ROW_NORMAL;
							continue;
						}
						if (elt.type != RowInfo.ROW_NORMAL) {
							System.out.println("type already " + elt.type + " for " + qp + "!");
							continue;
						}
						elt.type = RowInfo.ROW_CONST;
						elt.value = (circuit.circuitRightSide[i] + rsadd) / qv;
						circuit.circuitRowInfo[i].dropRow = true;
						//System.out.println(qp + " * " + qv + " = const " + elt.value);
						i = -1; // start over from scratch
					} else if (circuit.circuitRightSide[i] + rsadd == 0) {
						// we found a row with only two nonzero entries, and one
						// is the negative of the other; the values are equal
						if (elt.type != RowInfo.ROW_NORMAL) {
							//System.out.println("swapping");
							int qq = qm;
							qm = qp;
							qp = qq;
							elt = circuit.circuitRowInfo[qp];
							if (elt.type != RowInfo.ROW_NORMAL) {
								// we should follow the chain here, but this
								// hardly ever happens so it's not worth worrying
								// about
								System.out.println("swap failed");
								continue;
							}
						}
						elt.type = RowInfo.ROW_EQUAL;
						elt.nodeEq = qm;
						circuit.circuitRowInfo[i].dropRow = true;
						//System.out.println(qp + " = " + qm);
					}
				}
			}
			//System.out.println("ac7");

			// find size of new matrix
			int nn = 0;
			for (i = 0; i != matrixSize; i++) {
				RowInfo elt = circuit.circuitRowInfo[i];
				if (elt.type == RowInfo.ROW_NORMAL) {
					elt.mapCol = nn++;
					//System.out.println("col " + i + " maps to " + elt.mapCol);
					continue;
				}
				if (elt.type == RowInfo.ROW_EQUAL) {
					RowInfo e2 = null;
					// resolve chains of equality; 100 max steps to avoid loops
					for (j = 0; j != 100; j++) {
						e2 = circuit.circuitRowInfo[elt.nodeEq];
						if (e2.type != RowInfo.ROW_EQUAL) {
							break;
						}
						if (i == e2.nodeEq) {
							break;
						}
						elt.nodeEq = e2.nodeEq;
					}
				}
				if (elt.type == RowInfo.ROW_CONST) {
					elt.mapCol = -1;
				}
			}
			for (i = 0; i != matrixSize; i++) {
				RowInfo elt = circuit.circuitRowInfo[i];
				if (elt.type == RowInfo.ROW_EQUAL) {
					RowInfo e2 = circuit.circuitRowInfo[elt.nodeEq];
					if (e2.type == RowInfo.ROW_CONST) {
						// if something is equal to a const, it's a const
						elt.type = e2.type;
						elt.value = e2.value;
						elt.mapCol = -1;
					} else {
						elt.mapCol = e2.mapCol;
					}
				}
			}

			// make the new, simplified matrix
			int newsize = nn;
			double newmatx[][] = new double[newsize][newsize];
			double newrs[] = new double[newsize];
			int ii = 0;
			for (i = 0; i != matrixSize; i++) {
				RowInfo rri = circuit.circuitRowInfo[i];
				if (rri.dropRow) {
					rri.mapRow = -1;
					continue;
				}
				newrs[ii] = circuit.circuitRightSide[i];
				rri.mapRow = ii;
				//System.out.println("Row " + i + " maps to " + ii);
				for (j = 0; j != matrixSize; j++) {
					RowInfo ri = circuit.circuitRowInfo[j];
					if (ri.type == RowInfo.ROW_CONST) {
						newrs[ii] -= ri.value * circuit.circuitMatrix[i][j];
					} else {
						newmatx[ii][ri.mapCol] += circuit.circuitMatrix[i][j];
					}
				}
				ii++;
			}


			circuit.circuitMatrix = newmatx;
			circuit.circuitRightSide = newrs;
			matrixSize = circuit.circuitMatrixSize = newsize;
			for (i = 0; i != matrixSize; i++) {
				circuit.origRightSide[i] = circuit.circuitRightSide[i];
			}
			for (i = 0; i != matrixSize; i++) {
				for (j = 0; j != matrixSize; j++) {
					circuit.origMatrix[i][j] = circuit.circuitMatrix[i][j];
				}
			}

			circuit.circuitNeedsMap = true;


			// if a matrix is linear, we can do the lu_factor here instead of
			// needing to do it every frame
			if (!circuit.circuitNonLinear) {
				if (!lu_factor(circuit)) {
					circuit.stop("Singular matrix!", null);
				}
			}
			System.out.println(circuit.circuitMatrixSize);
			circuit.rebuilding = false;
			System.out.println("--Analyzed---");
			circuit.notifyAll();
		}
	}

	void runCircuit(Circuit circuit) {
		List<CircuitElement<?>> elmList = circuit.getCompressedElementList();
		if (circuit.circuitMatrix == null || elmList.size() == 0) {
			circuit.circuitMatrix = null;
			return;
		}
		boolean debugprint = circuit.dumpMatrix;
		circuit.dumpMatrix = false;
		for (int c = 0; c < 1; c++) {

			int i, j, k, subiter;
			for (i = 0; i != elmList.size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				ce.startIteration();
			}
			circuit.steps++;
			final int subiterCount = 5000;
			for (subiter = 0; subiter != subiterCount; subiter++) {
				circuit.converged = true;
				circuit.subIterations = subiter;
				for (i = 0; i != circuit.circuitMatrixSize; i++) {
					circuit.circuitRightSide[i] = circuit.origRightSide[i];
				}
				if (circuit.circuitNonLinear) {
					for (i = 0; i != circuit.circuitMatrixSize; i++) {
						for (j = 0; j != circuit.circuitMatrixSize; j++) {
							circuit.circuitMatrix[i][j] = circuit.origMatrix[i][j];
						}
					}
				}
				for (i = 0; i != elmList.size(); i++) {
					CircuitElement ce = circuit.getElm(i);
					ce.doStep();
				}
				if (circuit.stopMessage != null) {
					return;
				}
				boolean printit = debugprint;
				debugprint = false;
				for (j = 0; j != circuit.circuitMatrixSize; j++) {
					for (i = 0; i != circuit.circuitMatrixSize; i++) {
						double x = circuit.circuitMatrix[i][j];
						if (Double.isNaN(x) || Double.isInfinite(x)) {
							circuit.stop("nan/infinite matrix!", null);
							return;
						}
					}
				}
				if (printit) {
					for (j = 0; j != circuit.circuitMatrixSize; j++) {
						for (i = 0; i != circuit.circuitMatrixSize; i++) {
							System.out.print(circuit.circuitMatrix[j][i] + ",");
						}
						System.out.print("  " + circuit.circuitRightSide[j] + "\n");
					}
					System.out.println();
				}
				if (circuit.circuitNonLinear) {
					if (circuit.converged && subiter > 0) {
						break;
					}
					if (!lu_factor(circuit)) {
						circuit.stop("Singular matrix!", null);
						return;
					}
				}
				lu_solve(circuit);

				for (j = 0; j != circuit.circuitMatrixFullSize; j++) {
					RowInfo ri = circuit.circuitRowInfo[j];
					double res;
					if (ri.type == RowInfo.ROW_CONST) {
						res = ri.value;
					} else {
						res = circuit.circuitRightSide[ri.mapCol];
					}
					if (Double.isNaN(res)) {
						circuit.converged = false;
						break;
					}
					if (j < circuit.nodeList.size() - 1) {
						CircuitNode cn = circuit.getCircuitNode(j + 1);
						for (k = 0; k != cn.links.size(); k++) {
							CircuitNodeLink cnl = cn.links.elementAt(k);
							cnl.elm.setNodeVoltage(cnl.num, res);
						}
					} else {
						int ji = j - (circuit.nodeList.size() - 1);
						circuit.voltageSources[ji].setCurrent(ji, res);
					}
				}
				if (!circuit.circuitNonLinear) {
					break;
				}
			}
			if (subiter > 5) {
				System.out.println("converged after " + subiter + " iterations");
			}
			if (subiter == subiterCount) {
				circuit.stop("Convergence failed!", null);
				break;
			}
		}
		for (CircuitElement elm : elmList){
			elm.preApply();
		}
		for (Pair<Class, IElementChecker> p : CircuitElementFactory.INSTANCE.getElementCheckers()){
			if (!p.getRight().elementsValid(circuit.sortedElements.get(p.getLeft()))){
				runCircuit(circuit);
				return;
			}
		}
		for (CircuitElement elm : elmList){
			elm.apply();
		}
	}

	// Solves the set of n linear equations using a LU factorization.
	// On input, b[0..n-1] is the right hand side of the equations,
	// and on output, contains the solution.
	void lu_solve(Circuit circuit) {
		double a[][] = circuit.circuitMatrix;
		int n = circuit.circuitMatrixSize;
		int ipvt[] = circuit.circuitPermute;
		double b[] = circuit.circuitRightSide;
		int i;

		// find first nonzero b element
		for (i = 0; i != n; i++) {
			int row = ipvt[i];

			double swap = b[row];
			b[row] = b[i];
			b[i] = swap;
			if (swap != 0) {
				break;
			}
		}

		int bi = i++;
		for (; i < n; i++) {
			int row = ipvt[i];
			int j;
			double tot = b[row];

			b[row] = b[i];
			// forward substitution using the lower triangular matrix
			for (j = bi; j < i; j++) {
				tot -= a[i][j] * b[j];
			}
			b[i] = tot;
		}
		for (i = n - 1; i >= 0; i--) {
			double tot = b[i];

			// back-substitution using the upper triangular matrix
			int j;
			for (j = i + 1; j != n; j++) {
				tot -= a[i][j] * b[j];
			}
			b[i] = tot / a[i][i];
		}

	}

	// factors a matrix into upper and lower triangular matrices by
	// gaussian elimination.  On entry, a[0..n-1][0..n-1] is the
	// matrix to be factored.  ipvt[] returns an integer vector of pivot
	// indices, used in the lu_solve() routine.
	boolean lu_factor(Circuit circuit) {
		double a[][] = circuit.circuitMatrix;
		int n = circuit.circuitMatrixSize;
		int ipvt[] = circuit.circuitPermute;

		double scaleFactors[];
		int i, j, k;

		scaleFactors = new double[n];

		// divide each row by its largest element, keeping track of the
		// scaling factors
		for (i = 0; i != n; i++) {
			double largest = 0;
			for (j = 0; j != n; j++) {
				double x = Math.abs(a[i][j]);
				if (x > largest) {
					largest = x;
				}
			}
			// if all zeros, it's a singular matrix
			if (largest == 0) {
				return false;
			}
			scaleFactors[i] = 1.0 / largest;
		}

		// use Crout's method; loop through the columns
		for (j = 0; j != n; j++) {

			// calculate upper triangular elements for this column
			for (i = 0; i != j; i++) {
				double q = a[i][j];
				for (k = 0; k != i; k++) {
					q -= a[i][k] * a[k][j];
				}
				a[i][j] = q;
			}

			// calculate lower triangular elements for this column
			double largest = 0;
			int largestRow = -1;
			for (i = j; i != n; i++) {
				double q = a[i][j];
				for (k = 0; k != j; k++) {
					q -= a[i][k] * a[k][j];
				}
				a[i][j] = q;
				double x = Math.abs(q);
				if (x >= largest) {
					largest = x;
					largestRow = i;
				}
			}

			// pivoting
			if (j != largestRow) {
				double x;
				for (k = 0; k != n; k++) {
					x = a[largestRow][k];
					a[largestRow][k] = a[j][k];
					a[j][k] = x;
				}
				scaleFactors[largestRow] = scaleFactors[j];
			}

			// keep track of row interchanges
			ipvt[j] = largestRow;

			// avoid zeros
			if (a[j][j] == 0.0) {
				System.out.println("avoided zero");
				a[j][j] = 1e-18;
			}

			if (j != n - 1) {
				double mult = 1.0 / a[j][j];
				for (i = j + 1; i != n; i++) {
					a[i][j] *= mult;
				}
			}
		}
		return true;
	}

	private class FindPathInfo {

		private FindPathInfo(int t, CircuitElement e, int d, Circuit circuit) {
			dest = d;
			type = t;
			firstElm = e;
			used = new boolean[circuit.nodeList.size()];
			this.circuit = circuit;
		}

		private static final int VOLTAGE = 2;
		private static final int SHORT = 3;
		private static final int CAP_V = 4;
		private boolean used[];
		private int dest;
		private CircuitElement firstElm;
		private int type;
		private Circuit circuit;

		private boolean findPath(int n1) {
			return findPath(n1, -1);
		}

		private boolean findPath(int n1, int depth) {
			if (n1 == dest)
				return true;
			if (depth-- == 0)
				return false;
			if (used[n1]) {
				return false;
			}
			used[n1] = true;
			int i;
			for (i = 0; i != circuit.getCompressedElementList().size(); i++) {
				CircuitElement ce = circuit.getElm(i);
				if (ce == firstElm) {
					continue;
				}
				if (type == VOLTAGE) {
					if (!(ce.isWire() || ce instanceof VoltageElement)) {
						continue;
					}
				}
				if (type == SHORT && !ce.isWire())
					continue;
				if (type == CAP_V) {
					if (!(ce.isWire() || ce instanceof VoltageElement)) {
						continue;
					}
				}
				int j;
				for (j = 0; j != ce.getPostCount(); j++) {
					if (ce.getNode(j) == n1) {
						break;
					}
				}
				if (j == ce.getPostCount()) {
					continue;
				}
				int k;
				for (k = 0; k != ce.getPostCount(); k++) {
					if (j == k) {
						continue;
					}
					if (ce.getConnection(j, k) && findPath(ce.getNode(k), depth)) {
						used[n1] = false;
						return true;
					}
				}
			}
			used[n1] = false;
			return false;
		}

	}

}
