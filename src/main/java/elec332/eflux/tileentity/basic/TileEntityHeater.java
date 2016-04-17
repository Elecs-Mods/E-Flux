package elec332.eflux.tileentity.basic;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.multiblock.IMultiBlock;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.heat.IHeatReceiver;
import elec332.eflux.tileentity.multiblock.AbstractTileEntityMultiBlock;
import elec332.eflux.util.HeatHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * Created by Elec332 on 15-4-2016.
 */
@RegisterTile(name = "TileEntityEFluxHeater")
public class TileEntityHeater extends AbstractTileEntityMultiBlock implements ITickable {


    @Override
    protected void onMultiBlockCreated() {
        super.onMultiBlockCreated();
        hasMultiBlock = true;
    }

    @Override
    protected void onMultiBlockRemoved() {
        super.onMultiBlockRemoved();
        hasMultiBlock = false;
    }

    private boolean hasMultiBlock;

    @Override
    public void update() {
        if (hasMultiBlock){
            return;
        }
        EnumFacing facing = getTileFacing(); //TODO: Require power
        IHeatReceiver heatReceiver = HeatHelper.getHeatReceiver(worldObj, pos, facing.getOpposite());
        if (heatReceiver != null){
            heatReceiver.addHeat(20);
        }
    }

}
