package elec332.eflux.tileentity.multiblock;

import elec332.core.multiblock.*;
import elec332.core.util.DirectionHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.multiblock.EFluxMultiBlockMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 28-7-2015.
 */
public class TileMultiBlockPowerInlet extends AbstractMultiBlockTile implements IEnergyReceiver {


    public TileMultiBlockPowerInlet() {
        super(EFlux.multiBlockRegistry);
    }

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

    /**
     * Returns the multiblock this tile belongs too, can be null
     *
     * @return said multiblock
     */
    @Override
    public EFluxMultiBlockMachine getMultiBlock() {
        return (EFluxMultiBlockMachine) super.getMultiBlock();
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;//direction == DirectionHelper.getDirectionFromNumber(getBlockMetadata());
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return getMultiBlock() == null ? 0 : getMultiBlock().requestedRP();
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @param direction The requested direction
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp, ForgeDirection direction) {
        return getMultiBlock() == null ? 0 : getMultiBlock().getRequestedEF(rp);
    }

    /**
     * @param direction the direction where the power will be provided to
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(ForgeDirection direction, int rp, int ef) {
        return getMultiBlock() == null ? ef : getMultiBlock().receivePower(rp, ef);
    }

}
