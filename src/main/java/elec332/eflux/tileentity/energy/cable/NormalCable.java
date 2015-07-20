package elec332.eflux.tileentity.energy.cable;

import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.tileentity.EnergyTileBase;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class NormalCable extends EnergyTileBase implements IEnergyTransmitter {
    @Override
    public String getUniqueIdentifier() {
        return "mjhg";
    }

    @Override
    public int getMaxEFTransfer() {
        return 50;
    }

    @Override
    public int getMaxRPTransfer() {
        return 20;
    }
}
