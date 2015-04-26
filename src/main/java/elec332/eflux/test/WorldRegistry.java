package elec332.eflux.test;

import elec332.eflux.EFlux;
import elec332.eflux.test.power.WorldGridHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class WorldRegistry {

    private static Map<Integer, WorldRegistry> mappings = new WeakHashMap<Integer, WorldRegistry>();
    //private static List<World> registeredWorlds = new ArrayList<World>();
    //private static ArrayList<WorldRegistryPowerNetwork> mappings = new ArrayList<WorldRegistryPowerNetwork>();

    public static WorldRegistry get(World world){
        if (world == null)
            throw new IllegalArgumentException();
        /*try{
            return mappings.get(world.provider.dimensionId);
        } catch (Throwable t){
            WorldRegistryPowerNetwork ret = new WorldRegistryPowerNetwork(world);
            mappings.set(world.provider.dimensionId, ret);
            return ret;
        }*/
        WorldRegistry ret = mappings.get(world.provider.dimensionId);
        if (ret == null){
            ret = new WorldRegistry(world);
            mappings.put(world.provider.dimensionId, ret);
        }
        return ret;

        /*WorldRegistryPowerNetwork ret = mappings.get(world);
        if (ret == null){
            ret = new WorldRegistryPowerNetwork(world);
            //mappings.put(world, ret);
            mappings.put(world, ret);
        }
        return ret;*/
    }
    //////////////////////////////////////////////////////

    private WorldRegistry(World world){
        this.gridHolderPower = new WorldGridHolder(world);
        this.world = world;
        //this.world = world;
        //this.grids = new WeakHashMap<EFluxCableGrid, Object>();
        //FMLCommonHandler.instance().bus().register(this);  Didn't work, dunno why
        //registeredWorlds.add(world);
        EFlux.logger.info("Created new WorldHandler");
    }

    private WorldGridHolder gridHolderPower;
    private World world;

    public WorldGridHolder getWorldPowerGrid(){
        return gridHolderPower;
    }

    public void sm(String s){
        ((EntityPlayer) world.playerEntities.get(0)).addChatMessage(new ChatComponentText(s));
    }

    public void unload(){
        //sm("Unloaded World!!");
        EFlux.logger.info("Unloaded World!!");
        //world.getSaveHandler().loadWorldInfo().getNBTTagCompound().setTag("Derp", gridHolderPower.);
        gridHolderPower = null;
        mappings.remove(world.provider.dimensionId);
        world = null;
    }
}
