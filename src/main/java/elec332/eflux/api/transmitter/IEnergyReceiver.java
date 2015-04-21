package elec332.eflux.api.transmitter;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyReceiver extends IEnergyTile{
    public boolean canAcceptEnergyFrom(ForgeDirection direction);
}
