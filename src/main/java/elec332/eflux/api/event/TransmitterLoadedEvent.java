package elec332.eflux.api.event;

import elec332.eflux.api.energy.IEnergyTile;

/**
 * Created by Elec332 on 16-4-2015.
 */
public class TransmitterLoadedEvent extends PowerTransmitterEvent {
    public TransmitterLoadedEvent(IEnergyTile transmitterTile) {
        super(transmitterTile);
    }
}
