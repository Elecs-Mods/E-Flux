package elec332.eflux.simulation.optimization;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.simulation.AbstractResistorElement;
import elec332.eflux.simulation.CompressedCircuitElement;
import elec332.eflux.simulation.ICircuitCompressor;

import java.util.Collection;
import java.util.List;

/**
 * Created by Elec332 on 14-11-2017.
 */
public final class ResistorOptimizer implements ICircuitCompressor {

	@Override
	public Multimap<CompressedCircuitElement, CircuitElement> compress(List<CircuitElement> elements, Multimap<ConnectionPoint, CircuitElement<?>> map2) {
		Multimap<CompressedCircuitElement, CircuitElement> ret = HashMultimap.create();
		whileloop:
		while (true){ //Circumvent CME's
			for (CircuitElement<?> c1 : elements){
				if (c1 instanceof AbstractResistorElement){
					AbstractResistorElement re = (AbstractResistorElement) c1;
					List<ConnectionPoint> cp = c1.getConnectionPoints();
					if (cp.size() != 2){
						throw new RuntimeException();
					}
					List<AbstractResistorElement> list = Lists.newArrayList();
					ConnectionPoint cp1 = trace(list, re, cp.get(0), map2);
					list.add(re);
					ConnectionPoint cp2 = trace(list, re, cp.get(1), map2);
					if (list.size() == 1){
						continue;
					}
					CompressedCircuitElement ce = new CombinedResistorElement(cp1, cp2, list);
					ret.putAll(ce, list);
					elements.removeAll(list);
					elements.add(ce);
					continue whileloop;
				}
			}
			return ret;
		}
	}

	private ConnectionPoint trace(List<AbstractResistorElement> list, AbstractResistorElement<?> re, ConnectionPoint cp, Multimap<ConnectionPoint, CircuitElement<?>> map){
		loop:
		while (true){
			Collection<CircuitElement<?>> ceL = map.get(cp);
			if (ceL.size() != 2){
				return cp;
			}
			for (CircuitElement<?> ce : ceL){
				if (ce != re){
					if (ce instanceof AbstractResistorElement){
						re = (AbstractResistorElement<?>) ce;
						if (re.getConnectionPoints().size() != 2){
							throw new RuntimeException();
						}
						list.add(re);
						for (ConnectionPoint co : re.getConnectionPoints()){
							if (!co.equals(cp)){
								cp = co;
								continue loop;
							}
						}
						throw new RuntimeException();
					} else {
						return cp;
					}
				}
			}
			throw new RuntimeException();
		}

	}

	private class CombinedResistorElement extends CompressedCircuitElement<AbstractResistorElement> {

		protected CombinedResistorElement(ConnectionPoint start, ConnectionPoint end, List<AbstractResistorElement> res) {
			super(start, end, res);
			this.resistance = 0.0;
			for (AbstractResistorElement re : elements){
				this.resistance += re.getResistance();
			}
		}

		private double resistance;

		@Override
		public void setNodeVoltage(int n, double c) {
			super.setNodeVoltage(n, c);
		}

		@Override
		protected void calculateCurrent() {
			current = getVoltageDiff() / resistance;
		}

		@Override
		public void stamp() {
			getCircuit().stampResistor(nodes[0], nodes[1], resistance);
		}

		@Override
		public void preApply() {
			double powerLeft = volts[0], powerRight = volts[1], diff = getVoltageDiff(), currentLeft = powerLeft;
			for (AbstractResistorElement are : elements){
				double part = are.getResistance() / resistance;
				are.setNodeVoltage(0, currentLeft);
				currentLeft = Math.max(currentLeft - diff * part, powerRight);
				are.setNodeVoltage(1, currentLeft);
			}
		}

	}

}
