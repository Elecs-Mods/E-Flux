package elec332.eflux.grid.power;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.grid.WorldRegistry;

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

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        WorldRegistry.tick(event);
    }

}
