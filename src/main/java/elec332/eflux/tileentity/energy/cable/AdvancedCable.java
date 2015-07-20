package elec332.eflux.tileentity.energy.cable;

import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.tileentity.EnergyTileBase;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class AdvancedCable extends EnergyTileBase implements IEnergyTransmitter {
    @Override
    public String getUniqueIdentifier() {
        return "avdc";
    }

    @Override
    public int getMaxEFTransfer() {
        return 100;
    }

    @Override
    public int getMaxRPTransfer() {
        return 50;
    }
}
