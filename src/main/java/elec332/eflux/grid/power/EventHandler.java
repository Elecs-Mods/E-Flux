package elec332.eflux.grid.power;

import elec332.eflux.EFlux;
import elec332.eflux.api.event.PowerTransmitterEvent;
import elec332.eflux.grid.WorldRegistry;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class EventHandler {

    @SubscribeEvent
    public void onEnergyTileAdded(PowerTransmitterEvent.Load event){
        EFlux.systemPrintDebug("AddEvent");
        WorldRegistry.get(event.world).getWorldPowerGrid().addTile(event.transmitterTile);
        EFlux.systemPrintDebug("PT NonNull: " + (WorldRegistry.get(event.world).getWorldPowerGrid().getPowerTile(event.transmitterTile.getPos()) != null));
    }

    @SubscribeEvent
    public void onEnergyTileRemoved(PowerTransmitterEvent.UnLoad event){
        EFlux.systemPrintDebug("RemEvent");
        WorldRegistry.get(event.world).getWorldPowerGrid().removeTile(event.transmitterTile);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        WorldRegistry.tick(event);
    }

    @SubscribeEvent
    public void onNeighborNotify(BlockEvent.NeighborNotifyEvent event){
        if (!event.world.isRemote) {
            EFlux.systemPrintDebug("nbnEvent");
            WorldRegistry.get(event.world).getWorldPowerGrid().onBlockChange(event.pos, event.getNotifiedSides());
        }
    }

}
