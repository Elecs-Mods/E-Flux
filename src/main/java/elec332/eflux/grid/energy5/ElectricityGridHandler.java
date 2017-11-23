package elec332.eflux.grid.energy5;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import elec332.core.grid.AbstractGridHandler;
import elec332.core.world.DefaultMultiWorldPositionedObjectHolder;
import elec332.core.world.DimensionCoordinate;
import elec332.core.world.IMultiWorldPositionedObjectHolder;
import elec332.core.world.PositionedObjectHolder;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.energy.circuit.ICircuit;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.simulation.engine.Circuit;
import elec332.eflux.simulation.CircuitElementFactory;
import elec332.eflux.api.energy.circuit.CircuitElement;
import elec332.eflux.simulation.engine.SimEngine;
import elec332.eflux.grid.energy4.Wire;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Elec332 on 13-11-2017.
 */
public final class ElectricityGridHandler extends AbstractGridHandler<ETileEntityLink> {

	public ElectricityGridHandler(){
		MinecraftForge.EVENT_BUS.register(this);
		this.cache = new DefaultMultiWorldPositionedObjectHolder<>();
		this.circuits = Maps.newHashMap();
		this.toAddNextTick = Sets.newHashSet();
	}

	//Circuit circuit = new Circuit();
	private Multimap<Object, CircuitElement<?>> map = HashMultimap.create();
	public Set<Wire> wires = Sets.newHashSet();
	public final Set<Wire> wirez = Collections.unmodifiableSet(wires);
	private IMultiWorldPositionedObjectHolder<CPPosObj> cache;
	private Map<UUID, Circuit> circuits;
	private Set<CircuitElement<?>> toAddNextTick;

	public void clear(){
		//circuit.clear();
		map.clear();
		wires.clear();
		cache.clear();
		circuits.values().forEach(Circuit::clear);
		circuits.clear();
	}

	public void addWire(Wire wire){
		if (wires.add(wire)){
			Collection<CircuitElement<?>> elm = CircuitElementFactory.INSTANCE.wrapComponent(wire);
			elm.forEach(element -> {
				map.put(wire, element);
				addElm(element);
			});
		}
	}

	public void removeWire(Wire wire){
		Collection<CircuitElement<?>> elm = map.get(wire);
		if (elm != null){
			elm.forEach(this::removeElm);
			map.removeAll(wire);
			wires.remove(wire);
		}
	}

	@Override
	protected void onObjectRemoved(ETileEntityLink o, Set<DimensionCoordinate> allUpdates) {
		Collection<CircuitElement<?>> elm = map.get(o);
		if (elm != null){
			elm.forEach(this::removeElm);
			map.removeAll(o);
		}
	}

	@Override
	protected void internalAdd(ETileEntityLink o) {
		if (!toAddNextTick.isEmpty()){
			toAddNextTick.forEach(this::addElm);
			toAddNextTick.clear();
		}
		TileEntity tile = o.getPosition().getTileEntity();
		if (tile == null){
			return;
		}
		Collection<CircuitElement<?>> elm = CircuitElementFactory.INSTANCE.wrapComponent(tile.getCapability(EFluxAPI.ENERGY_CAP, null));
		elm.forEach(element -> {
			map.put(o, element);
			addElm(element);
		});
	}

	private void addElm(CircuitElement<?> elm){
		if (elm == null){
			return;
		}
		Set<CircuitElement> ceL = Sets.newHashSet(elm);
		Circuit myCircuit = null;
		for (ConnectionPoint cp : elm.getConnectionPoints()){
			PositionedObjectHolder<CPPosObj> woj = cache.getOrCreate(cp.getWorld());
			CPPosObj bla = woj.get(cp.getPos());
			if (bla == null){
				woj.put(bla = new CPPosObj(), cp.getPos());
			}
			for (CircuitElement<?> otherElmsAtPos : bla.connections.get(cp)){
				ICircuit c = otherElmsAtPos.getCircuit();
				if (c == null){
					if (myCircuit == null) {
						ceL.add(otherElmsAtPos);
					} else {
						myCircuit.addElement(otherElmsAtPos);
					}
				} else {
					Circuit circuit = (Circuit) c;
					if (myCircuit == null) {
						ceL.forEach(circuit::addElement);
						ceL = null;
						myCircuit = circuit;
					} else {
						if (circuit != myCircuit) {
							myCircuit.consumeCircuit(circuit);
							circuit.clear();
							circuits.remove(circuit.getId());
						}
					}
				}
			}
			bla.connections.put(cp, elm);
		}
		if (ceL != null && ceL.size() > 1){
			Circuit newCircuit = new Circuit();
			ceL.forEach(newCircuit::addElement);
			circuits.put(newCircuit.getId(), newCircuit);
		}
	}

	private void removeElm(CircuitElement<?> elm){
		if (elm == null){
			return;
		}
		ICircuit c = elm.getCircuit();
		if (c == null){
			toAddNextTick.remove(elm);
			return;
		}
		Circuit circuit = (Circuit) c;
		if (circuit.removeElement(elm, toAddNextTick)){
			circuits.remove(circuit.getId());
			circuit.clear();
		}
		for (ConnectionPoint cp : elm.getConnectionPoints()){
			PositionedObjectHolder<CPPosObj> woj = cache.getOrCreate(cp.getWorld());
			CPPosObj bla = woj.get(cp.getPos());
			if (bla == null){
				throw new RuntimeException();
			}
			bla.connections.remove(cp, elm);
		}
		elm.destroy();
	}

	@Override
	public void tick() {
		System.out.println("-------------------------------");
		//SimEngine.INSTANCE.preTick(circuit);
		System.out.println("Siz: "+circuits.keySet().size());
		circuits.values().forEach(SimEngine.INSTANCE::preTick);
	}

	@Override
	public boolean isValidObject(TileEntity tile) {
		if (!tile.hasCapability(EFluxAPI.ENERGY_CAP, null)){
			return false;
		}
		IEnergyObject eObj = tile.getCapability(EFluxAPI.ENERGY_CAP, null);
		return eObj != null && !eObj.isPassiveConnector();
	}

	@Override
	protected ETileEntityLink createNewObject(TileEntity tile) {
		return new ETileEntityLink(tile);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void serverTick(TickEvent.ServerTickEvent event){
		if (event.side.isServer() && event.phase == TickEvent.Phase.END) {
			System.out.println("TickLow: "+ FMLCommonHandler.instance().getMinecraftServerInstance().worlds[0].getWorldTime());

			circuits.values().forEach(SimEngine.INSTANCE::tick);
			//SimEngine.INSTANCE.tick(circuit);
			System.out.println("-------------------------");
		}
	}

}
