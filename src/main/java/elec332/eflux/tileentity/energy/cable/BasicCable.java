package elec332.eflux.tileentity.energy.cable;

import elec332.core.api.annotations.RegisterTile;

/**
 * Created by Elec332 on 29-4-2015.
 */
@RegisterTile(name = "TileEntityEFluxBasicCable")
public class BasicCable extends AbstractCable {

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
