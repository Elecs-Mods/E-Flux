package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 14-4-2016.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockFluidOutlet")
public class TileEntityMultiBlockFluidOutlet extends AbstractTileEntityMultiBlockFluidHandler {

    @Override
    protected boolean canFillFrom(EnumFacing facing) {
        return false;
    }

    @Override
    public boolean canFaceUpOrDown() {
        return true;
    }

}
