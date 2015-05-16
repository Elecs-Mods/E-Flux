package elec332.eflux.tileentity;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.energy.EnergyAPIHelper;

/**
 * Created by Elec332 on 16-5-2015.
 */
public class EnergyTileBase extends TileBase {

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote)
            EnergyAPIHelper.postLoadEvent(this);
    }

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote)
            EnergyAPIHelper.postUnloadEvent(this);
    }
}
