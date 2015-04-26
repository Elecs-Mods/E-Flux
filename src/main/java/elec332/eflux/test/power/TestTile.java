package elec332.eflux.test.power;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.api.transmitter.IEnergyReceiver;
import elec332.eflux.api.transmitter.IEnergySource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 26-4-2015.
 */
public class TestTile extends TileBase implements IEnergySource, IEnergyReceiver {

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

    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction == ForgeDirection.DOWN;
    }

    @Override
    public boolean canProvidePowerTo(ForgeDirection direction) {
        return direction == ForgeDirection.UP;
    }
}
