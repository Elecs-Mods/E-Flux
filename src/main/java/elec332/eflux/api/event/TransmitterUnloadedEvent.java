package elec332.eflux.api.event;

import elec332.eflux.api.transmitter.IEnergyTile;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class TransmitterUnloadedEvent extends PowerTransmitterEvent {
    public TransmitterUnloadedEvent(IEnergyTile transmitterTile) {
        super(transmitterTile);
    }
}
