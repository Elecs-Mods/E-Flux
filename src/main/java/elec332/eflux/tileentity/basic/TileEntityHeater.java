package elec332.eflux.tileentity.basic;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.api.heat.IHeatReceiver;
import elec332.eflux.tileentity.multiblock.AbstractTileEntityMultiBlock;
import elec332.eflux.util.HeatHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Created by Elec332 on 15-4-2016.
 */
@RegisterTile(name = "TileEntityEFluxHeater")
public class TileEntityHeater extends AbstractTileEntityMultiBlock implements ITickable, IEFluxPowerHandler {

    public TileEntityHeater(){
        energyContainer = new EnergyContainer(500, this);
    }

    private boolean hasMultiBlock;
    private EnergyContainer energyContainer;

    @Override
    public void onTileUnloaded() {
        if (!worldObj.isRemote) {
            EnergyAPIHelper.postUnloadEvent(this);
        }
    }

    @Override
    public void onTileLoaded() {
        if (!worldObj.isRemote) {
            EnergyAPIHelper.postLoadEvent(this);
        }
    }

    @Override
    protected void onMultiBlockCreated() {
        super.onMultiBlockCreated();
        hasMultiBlock = true;
        worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
    }

    @Override
    protected void onMultiBlockRemoved() {
        super.onMultiBlockRemoved();
        hasMultiBlock = false;
        worldObj.notifyNeighborsOfStateChange(pos, getBlockType());
    }

    @Override
    public void update() {
        if (hasMultiBlock){
            return;
        }
        EnumFacing facing = getTileFacing(); //TODO: Require power
        IHeatReceiver heatReceiver = HeatHelper.getHeatReceiver(worldObj, pos, facing);
        if (heatReceiver != null){
            heatReceiver.addHeat(20);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, boolean hasMultiBlock) {
        return !hasMultiBlock && facing == getTileFacing().getOpposite() && capability == EFluxAPI.RECEIVER_CAPABILITY || super.hasCapability(capability, facing, hasMultiBlock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, boolean hasMultiBlock) {
        return !hasMultiBlock && facing == getTileFacing().getOpposite() && capability == EFluxAPI.RECEIVER_CAPABILITY ? (T) energyContainer : super.getCapability(capability, facing, hasMultiBlock);
    }

    @Override
    public int getEFForOptimalRP() {
        return 15;
    }

    @Override
    public float getAcceptance() {
        return 0.2f;
    }

    @Override
    public int getOptimalRP() {
        return 20;
    }

    @Override
    public void markObjectDirty() {
        markDirty();
    }

}
