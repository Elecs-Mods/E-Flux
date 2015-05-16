package elec332.eflux.tileentity.energy.cable;

import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.tileentity.EnergyTileBase;

/**
 * Created by Elec332 on 29-4-2015.
 */
public class BasicCable extends EnergyTileBase implements IEnergyTransmitter {

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
