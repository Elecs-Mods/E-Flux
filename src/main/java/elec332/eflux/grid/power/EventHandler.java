package elec332.eflux.grid.power;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.grid.WorldRegistry;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class EventHandler {

    @SubscribeEvent
    public void onEnergyTileAdded(TransmitterLoadedEvent event){
        WorldRegistry.get(event.world).getWorldPowerGrid().addTile(event.transmitterTile);
    }

    @SubscribeEvent
    public void onEnergyTileRemoved(TransmitterUnloadedEvent event){
        WorldRegistry.get(event.world).getWorldPowerGrid().removeTile(event.transmitterTile);
    }

    /*@SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        WorldRegistryPowerNetwork.onServerTick(event);
    }*/

    /*@SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        WorldRegistry.get(event.world).getWorldPowerGrid().onServerTickInternal(event);
    }*/

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event){
        WorldRegistry.get(event.world).unload();
    }
}
