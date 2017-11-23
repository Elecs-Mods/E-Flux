package elec332.eflux.simulation;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.grid.energy4.Wire;
import elec332.eflux.simulation.optimization.ResistorOptimizer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by Elec332 on 12-11-2017.
 */
public enum CircuitElementFactory {

	INSTANCE;

	CircuitElementFactory(){
		this.cache = Maps.newHashMap();
		this.elementCheckers = Maps.newHashMap();
	}

	private Map<Class<?>, BiConsumer<IEnergyObject, Collection<CircuitElement<?>>>> cache;
	private List<ICircuitCompressor> optimizers = Lists.newArrayList();
	private Map<Integer, Pair<Class, IElementChecker>> elementCheckers;
	private int hc = 1000;

	@Nonnull
	public Collection<CircuitElement<?>> wrapComponent(IEnergyObject component){
		if (component == null){
			return Collections.emptySet();
		}
		List<BiConsumer<IEnergyObject, Collection<CircuitElement<?>>>> wrappers = Lists.newArrayList();
		cache.forEach((type, wrapper) -> {
			if (type.isAssignableFrom(component.getClass())) {
				wrappers.add(wrapper);
			}
		});
		int i = wrappers.size();
		if (i == 1){
			Set<CircuitElement<?>> ret = Sets.newHashSet();
			wrappers.get(0).accept(component, ret);
			Preconditions.checkArgument(!ret.isEmpty());
			return ret;
		} else if (i == 0){
			throw new IllegalArgumentException(component.toString());
		} else {
			throw new IllegalStateException(component.toString());
		}
	}

	@SuppressWarnings("all")
	public <O extends IEnergyObject, E extends CircuitElement<?>> void registerComponentWrapper(Class<O> clazz, BiConsumer<O, Collection<CircuitElement<?>>> wrapper){
		cache.put(clazz, (BiConsumer<IEnergyObject, Collection<CircuitElement<?>>>) wrapper);
	}

	@SuppressWarnings("all")
	public <O extends IEnergyObject, E extends CircuitElement<O>> void registerComponentWrapper(Class<O> clazz, Class<E> eClass, Function<O, E> wrapper, int weight, IElementChecker<E> checker){
		cache.put(clazz, (energyObject, circuitElements) -> circuitElements.add(wrapper.apply((O)energyObject)));
		if (elementCheckers.containsKey(weight)){
			throw new IllegalArgumentException();
		}
		elementCheckers.put(weight, Pair.of(eClass, checker));
	}

	public <O extends IEnergyObject, E extends CircuitElement<O>> void registerComponentWrapper(Class<O> clazz, Class<E> eClass, Function<O, E> wrapper){
		registerComponentWrapper(clazz, eClass, wrapper, hc++, elements -> true);
	}

	public List<ICircuitCompressor> getCircuitOptimizers(){
		return optimizers;
	}

	public Collection<Pair<Class, IElementChecker>> getElementCheckers() {
		return elementCheckers.values();
	}

	static {
		INSTANCE.registerComponentWrapper(IEnergyReceiver.class, ResistorElement.class, ResistorElement::new);
		INSTANCE.registerComponentWrapper(Wire.class, WireElement.class, WireElement::new);
		INSTANCE.registerComponentWrapper(IEnergySource.class, VoltageElement.class, VoltageElement::new);
		INSTANCE.optimizers.add(new ResistorOptimizer());
	}

}
