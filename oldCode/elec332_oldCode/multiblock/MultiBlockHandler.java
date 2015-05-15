package elec332_oldCode.multiblock;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.WeakHashMap;

/**
 * Created by Elec332 on 14-5-2015.
 */
public class MultiBlockHandler {

    private static WeakHashMap<World, MultiBlockWorldHandler> registry = new WeakHashMap<World, MultiBlockWorldHandler>();

    @SubscribeEvent
    public void onMBPLoaded(MultiBlockEvent.Loaded event){
        getHandler(event.world).onMBPLoaded(event);
    }

    @SubscribeEvent
    public void onMBPUnloaded(MultiBlockEvent.Unloaded event) {
        getHandler(event.world).onMBPUnloaded(event);
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event){
        if (registry.containsKey(event.world))
            registry.remove(event.world);
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event){
        if (registry.containsKey(event.world))
            getHandler(event.world).onChunkLoaded(event.getChunk());
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        if (event.phase == TickEvent.Phase.START)
            if (registry.containsKey(event.world))
                getHandler(event.world).tick();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event){
        if (event.phase == TickEvent.Phase.START)
            getHandler(Minecraft.getMinecraft().theWorld).tick();
    }

    public static MultiBlockWorldHandler getHandler(World world) {
        MultiBlockWorldHandler handler = registry.get(world);
        if (handler == null){
            handler = new MultiBlockWorldHandler(world);
            registry.put(world, handler);
        }
        return handler;
    }
}
