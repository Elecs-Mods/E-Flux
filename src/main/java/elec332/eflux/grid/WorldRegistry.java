package elec332.eflux.grid;

import com.google.common.collect.Lists;
import elec332.core.main.ElecCore;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.grid.tank.EFluxDynamicTankWorldHolder;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class WorldRegistry {

    private static WeakHashMap<World, WorldRegistry> mappings = new WeakHashMap<World, WorldRegistry>();
    private static List<World> worlds = Lists.newArrayList();

    public static WorldRegistry get(World world) {
        if (world == null)
            throw new IllegalArgumentException();
        if (!world.isRemote) {
            WorldRegistry ret = mappings.get(world);
            if (ret == null) {
                ret = new WorldRegistry(world);
                mappings.put(world, ret);
                worlds.add(world);
            }
            return ret;
        }
        return null;
    }

    @SuppressWarnings("all")
    public static void tick(TickEvent event) {
        for (World world : worlds) {
            if (event.phase == TickEvent.Phase.START) {
                get(world).tickStart();
            } else {
                get(world).tickEnd();
            }
        }
    }

    //////////////////////////////////////////////////////

    private WorldRegistry(World world) {
//        this.gridHolderPower = new WorldGridHolder(world);
        this.tankRegistry = new EFluxDynamicTankWorldHolder(world);
        this.tickables = new ArrayDeque<ITickable>();
        this.world = world;
        EFlux.logger.info("Created new WorldHandler");
        ElecCore.tickHandler.registerCall(new Runnable() {
            @Override
            public void run() {
                MinecraftForge.EVENT_BUS.register(WorldRegistry.this);
            }
        }, world);
    }

//    private final WorldGridHolder gridHolderPower;
    private final EFluxDynamicTankWorldHolder tankRegistry;
    private final Queue<ITickable> tickables;
    private final World world;

//    public WorldGridHolder getWorldPowerGrid() {
//        return gridHolderPower;
//    }

    public EFluxDynamicTankWorldHolder getTankRegistry(){
        return tankRegistry;
    }

    private void tickStart() {
        Iterator<ITickable> ti = tickables.iterator();
        ITickable tickable;
        while (ti.hasNext()){
            tickable = ti.next();
            tickable.update();
        }
    }

    private void tickEnd(){
        tankRegistry.tick();
//        gridHolderPower.tickEnd();
    }

    public void addTickable(ITickable tickable){
        this.tickables.add(tickable);
    }

    public void removeTickable(ITickable tickable){
        this.tickables.remove(tickable);
    }

    private void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
        mappings.remove(world);
        worlds.remove(world);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onWorldUnload(WorldEvent.Unload event) {
        if (WorldHelper.getDimID(world) == WorldHelper.getDimID(event.getWorld())) {
            unload();
        }
    }

}

