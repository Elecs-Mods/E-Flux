package elec332.eflux.tileentity.energy.cable;

import elec332.core.api.annotations.RegisterTile;

/**
 * Created by Elec332 on 20-7-2015.
 */
@RegisterTile(name = "TileEntityEFluxAdvancedCable")
public class AdvancedCable extends AbstractCable {

    @Override
    public String getUniqueIdentifier() {
        return "avdc";
    }

    @Override
    public int getMaxEFTransfer() {
        return 200;
    }

    @Override
    public int getMaxRPTransfer() {
        return 50;
    }

}
