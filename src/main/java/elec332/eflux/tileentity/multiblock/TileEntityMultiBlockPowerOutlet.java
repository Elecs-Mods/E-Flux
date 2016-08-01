package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyProvider;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * Created by Elec332 on 13-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockPowerOutlet")
public class TileEntityMultiBlockPowerOutlet extends AbstractTileEntityMultiBlockHandler<IEnergyProvider> implements IEnergyProvider {

    @CapabilityInject(IEnergyProvider.class)
    public static Capability<IEnergyProvider> CAPABILITY;
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

    /**
     * @param rp        the RedstonePotential in the network
     * @param execute   weather the power is actually drawn from the tile,
     *                  this flag is always true for IEnergySource.
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, boolean execute) {
        IEnergyProvider mb = getMultiBlockHandler();
        return mb == null ? 0 : mb.provideEnergy(rp, execute);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing, boolean hasMultiBlock) {
        return (capability == EFluxAPI.PROVIDER_CAPABILITY && facing == getTileFacing()) || super.hasCapability(capability, facing, hasMultiBlock);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing, boolean hasMultiBlock) {
        return capability == EFluxAPI.PROVIDER_CAPABILITY ? (facing == getTileFacing() ? (T)this : null) : super.getCapability(capability, facing, hasMultiBlock);
    }

    @Override
    protected Capability<IEnergyProvider> getCapability() {
        return CAPABILITY;
    }

}
