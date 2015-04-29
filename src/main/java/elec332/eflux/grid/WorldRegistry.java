package elec332.eflux.grid;

import elec332.eflux.EFlux;
import elec332.eflux.grid.power.WorldGridHolder;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class WorldRegistry {

    private static Map<Integer, WorldRegistry> mappings = new WeakHashMap<Integer, WorldRegistry>();

    public static WorldRegistry get(World world){
        if (world == null)
            throw new IllegalArgumentException();
        WorldRegistry ret = mappings.get(world.provider.dimensionId);
        if (ret == null){
            ret = new WorldRegistry(world);
            mappings.put(world.provider.dimensionId, ret);
        }
        return ret;
    }
    //////////////////////////////////////////////////////

    private WorldRegistry(World world){
        this.gridHolderPower = new WorldGridHolder(world);
        this.world = world;
        EFlux.logger.info("Created new WorldHandler");
    }

    private WorldGridHolder gridHolderPower;
    private World world;

    public WorldGridHolder getWorldPowerGrid(){
        return gridHolderPower;
    }

    public void unload(){
        EFlux.logger.info("Unloaded World!!");
        gridHolderPower = null;
        mappings.remove(world.provider.dimensionId);
        world = null;
    }
}
