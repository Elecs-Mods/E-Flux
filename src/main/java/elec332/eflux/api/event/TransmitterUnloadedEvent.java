package elec332.eflux.api.event;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class TransmitterUnloadedEvent extends PowerTransmitterEvent {
    public TransmitterUnloadedEvent(TileEntity transmitterTile) {
        super(transmitterTile);
    }
}
