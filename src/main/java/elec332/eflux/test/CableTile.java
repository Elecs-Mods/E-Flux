package elec332.eflux.test;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.api.transmitter.IPowerTransmitter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 21-4-2015.
 */
public class CableTile extends TileBase implements IPowerTransmitter{

    @Override
    public void validate() {
        super.validate();
        MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
    }

    @Override
    public void invalidate() {
        super.invalidate();
        MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }

    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;
    }

    @Override
    public boolean canProvidePowerTo(ForgeDirection direction) {
        return true;
    }
}
