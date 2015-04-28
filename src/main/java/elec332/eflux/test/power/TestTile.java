package elec332.eflux.test.power;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 26-4-2015.
 */
public class TestTile extends TileBase implements IEnergySource, IEnergyReceiver {

    private boolean validated = false;

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!validated) {
            if (!worldObj.isRemote)
                MinecraftForge.EVENT_BUS.post(new TransmitterLoadedEvent(this));
            validated = true;
        }
    }

    public int storedPower = 0;

    @Override
    public void invalidate() {
        super.invalidate();
        MinecraftForge.EVENT_BUS.post(new TransmitterUnloadedEvent(this));
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction == ForgeDirection.DOWN;
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 30;
    }

    /**
     * @param rp        The Redstone Potential in the network
     * @param direction The requested direction
     * @return The amount of EnergeticFlux requested for the Redstone Potential in the network
     */
    @Override
    public int getRequestedEF(int rp, ForgeDirection direction) {
        return 600/rp;
    }

    /**
     * @param direction the direction where the power will be provided to
     * @param rp        the RedstonePotential in the network
     * @param ef        the amount of EnergeticFlux that is being provided
     * @return The amount of EnergeticFlux that wasn't used
     */
    @Override
    public int receivePower(ForgeDirection direction, int rp, int ef) {
        storedPower = storedPower+rp*ef;
        return 0;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and provide power to the given side
     */
    @Override
    public boolean canProvidePowerTo(ForgeDirection direction) {
        return direction == ForgeDirection.UP;
    }

    /**
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, ForgeDirection direction) {
        int i = storedPower/rp;
        storedPower = 0;
        return i;
    }
}
