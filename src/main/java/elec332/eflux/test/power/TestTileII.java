package elec332.eflux.test.power;

import elec332.core.baseclasses.tileentity.TileBase;
import elec332.eflux.api.energy.ISpecialEnergySource;
import elec332.eflux.api.event.TransmitterLoadedEvent;
import elec332.eflux.api.event.TransmitterUnloadedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 29-4-2015.
 */
public class TestTileII extends TileBase implements ISpecialEnergySource {

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
     * @param rp        the RedstonePotential in the network
     * @param direction the direction where the power will be provided to
     * @param reqEF     the requested amount of EnergeticFlux
     * @return The amount of EnergeticFlux the tile will provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergeticFlux(int rp, ForgeDirection direction, int reqEF) {
        if (reqEF > provideEnergy(rp, direction, false))
            return provideEnergy(rp, direction, true);
        else {
            storedPower = storedPower-reqEF*rp;
            return reqEF;
        }
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
     * @param execute   weather the power is actually drawn from the tile,
     *                  This does NOTHING if the tile doesn't implement ISpecialEnergySource
     * @return The amount of EnergeticFlux the tile can provide for the given Redstone Potential.
     */
    @Override
    public int provideEnergy(int rp, ForgeDirection direction, boolean execute) {
        if (!execute)
            return storedPower/rp;
        else {
            int i = storedPower/rp;
            storedPower = 0;
            return i;
        }
    }
}
