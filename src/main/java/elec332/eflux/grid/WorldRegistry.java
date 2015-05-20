package elec332.eflux.grid;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.eflux.EFlux;
import elec332.eflux.grid.power.WorldGridHolder;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class WorldRegistry {

    private static WeakHashMap<World, WorldRegistry> mappings = new WeakHashMap<World, WorldRegistry>();
    private static List<World> worlds = new ArrayList<World>();

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
        this.world = world;
        EFlux.logger.info("Created new WorldHandler");
        MinecraftForge.EVENT_BUS.register(this);
    }

    private WorldGridHolder gridHolderPower;
    private World world;

    public WorldGridHolder getWorldPowerGrid() {
        return gridHolderPower;
    }

    public void tickInternal() {
        gridHolderPower.onServerTickInternal();
    }

    public void unload() {
        MinecraftForge.EVENT_BUS.unregister(this);
        mappings.remove(world);
        worlds.remove(world);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (event.world.provider.dimensionId == world.provider.dimensionId)
            unload();
    }
}

