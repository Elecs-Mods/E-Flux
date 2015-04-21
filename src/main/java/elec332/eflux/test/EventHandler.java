package elec332.eflux.test;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class EventHandler {

    @SubscribeEvent
    public void onEnergyTileAdded(TransmitterLoadedEvent event){
        WorldRegistryPowerNetwork.get(event.world).addTile(event.transmitterTile);
    }

    @SubscribeEvent
    public void onEnergyTileRemoved(TransmitterUnloadedEvent event){
        WorldRegistryPowerNetwork.get(event.world).addTile(event.transmitterTile);
    }
}
