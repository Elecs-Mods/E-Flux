package elec332.eflux.grid;

import com.google.common.collect.Lists;
import elec332.core.main.ElecCore;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.grid.power.WorldGridHolder;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.WeakHashMap;

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

    public static void tick(TickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (World world : worlds)
                get(world).tickInternal();
        }
    }
    //////////////////////////////////////////////////////

    private WorldRegistry(World world) {
        this.gridHolderPower = new WorldGridHolder(world);
        this.tickables = Lists.newArrayList();
        this.world = world;
        EFlux.logger.info("Created new WorldHandler");
        ElecCore.tickHandler.registerCall(new Runnable() {
            @Override
            public void run() {
                MinecraftForge.EVENT_BUS.register(WorldRegistry.this);
            }
        }, world);
    }

    private final WorldGridHolder gridHolderPower;
    private final List<ITickable> tickables;
    private final World world;

    public WorldGridHolder getWorldPowerGrid() {
        return gridHolderPower;
    }

    public void tickInternal() {
        gridHolderPower.onServerTickInternal();
    }

    public void addTickable(ITickable tickable){
        this.tickables.add(tickable);
    }

    public void removeTickable(ITickable tickable){
        this.tickables.remove(tickable);
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
        mappings.remove(world);
        worlds.remove(world);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (WorldHelper.getDimID(world) == WorldHelper.getDimID(event.world))
            unload();
    }
}

