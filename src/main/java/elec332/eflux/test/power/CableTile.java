package elec332.eflux.test.power;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.api.energy.IPowerTransmitter;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Elec332 on 21-4-2015.
 */
public class CableTile extends TileBase implements IPowerTransmitter{

    private boolean validated = false;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!validated) {
            if (!worldObj.isRemote)
                MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
            validated = true;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }
}
