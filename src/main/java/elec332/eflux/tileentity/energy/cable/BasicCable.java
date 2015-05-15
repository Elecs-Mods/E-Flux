package elec332.eflux.tileentity.energy.cable;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.energy.IPowerTransmitter;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Elec332 on 29-4-2015.
 */
public class BasicCable extends TileBase implements IPowerTransmitter {

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote)
            MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }

    @Override
    public String getUniqueIdentifier() {
        return "q49d";
    }

    @Override
    public int getMaxEFTransfer() {
        return 10;
    }

    @Override
    public int getMaxRPTransfer() {
        return 5;
    }
}
