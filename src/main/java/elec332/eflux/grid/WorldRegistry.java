package elec332.eflux.grid;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import elec332.eflux.EFlux;
import elec332.eflux.grid.power.WorldGridHolder;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class WorldRegistry {

    private static Map<World, WorldRegistry> mappings = new WeakHashMap<World, WorldRegistry>();

    public static WorldRegistry get(World world){
        if (world == null)
            throw new IllegalArgumentException();
        WorldRegistry ret = mappings.get(world);
        if (ret == null){
            ret = new WorldRegistry(world);
            mappings.put(world, ret);
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

   /* public void unload(){
        EFlux.logger.info("Unloaded World!!");
        gridHolderPower.unload();
        gridHolderPower = null;
        mappings.remove(world.provider.dimensionId);
        world = null;
    }*/

    /*@SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event){
        if (event.world.provider.dimensionId == world.provider.dimensionId)
            unload();
    }*/
}
