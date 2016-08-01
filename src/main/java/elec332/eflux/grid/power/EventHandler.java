package elec332.eflux.grid.power;

import elec332.eflux.grid.WorldRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
@SuppressWarnings("all")
public class EventHandler {
/*
    @SubscribeEvent
    public void onEnergyTileAdded(PowerTransmitterEvent.Load event){
        EFlux.systemPrintDebug("AddEvent");
        WorldRegistry.get(event.getWorld()).getWorldPowerGrid().addTile(event.transmitterTile);
        EFlux.systemPrintDebug("PT NonNull: " + (WorldRegistry.get(event.getWorld()).getWorldPowerGrid().getPowerTile(event.transmitterTile.getPos()) != null));
    }

    @SubscribeEvent
    public void onEnergyTileRemoved(PowerTransmitterEvent.UnLoad event){
        EFlux.systemPrintDebug("RemEvent");
        WorldRegistry.get(event.getWorld()).getWorldPowerGrid().removeTile(event.transmitterTile);
    }*/

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        WorldRegistry.tick(event);
    }
/*
    @SubscribeEvent
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent event){
        if (!event.getWorld().isRemote) {
            EFlux.systemPrintDebug("nbnEvent");
            WorldRegistry.get(event.getWorld()).getWorldPowerGrid().onBlockChange(event.getPos(), event.getNotifiedSides());
        }
    }
*/
}
