package elec332.eflux.tileentity.multiblock;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyReceiver;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by Elec332 on 28-7-2015.
 */
@RegisteredTileEntity("TileEntityEFluxMultiBlockPowerInlet")
public class TileEntityMultiBlockPowerInlet extends AbstractTileEntityMultiBlockHandler<IEnergyReceiver> /*implements IEnergyReceiver*/ {

    @CapabilityInject(IEnergyReceiver.class)
    private static Capability<IEnergyReceiver> CAPABILITY;
/*
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
    }*/
    private int latsRP, lastEF;


    /**
     * @return The Redstone Potential at which the machine wishes to operate
     *
    @Override
    public int requestedRP() {
        IEnergyReceiver mb = getMultiBlockHandler();
        return mb == null ? 0 : mb.requestedRP();
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     *
    @Override
    public int getRequestedEF(int rp) {
        IEnergyReceiver mb = getMultiBlockHandler();
        return mb == null ? 0 : mb.getRequestedEF(rp);
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     *
    @Override
    public int receivePower(int rp, int ef) {
        lastEF = ef;
        latsRP = rp;
        IEnergyReceiver mb = getMultiBlockHandler();
        return mb == null ? ef : mb.receivePower(rp, ef);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, boolean hasMultiBlock) {
        return (capability == EFluxAPI.RECEIVER_CAPABILITY && facing == getTileFacing()) || super.hasCapability(capability, facing, hasMultiBlock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == EFluxAPI.RECEIVER_CAPABILITY ? (facing == getTileFacing() ? (T)this : null) : super.getCapability(capability, facing, hasMultiBlock);
    }*/

    @Override
    protected Capability<IEnergyReceiver> getCapability() {
        return CAPABILITY;
    }

}
