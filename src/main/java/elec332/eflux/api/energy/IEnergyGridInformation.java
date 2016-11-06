package elec332.eflux.api.energy;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 9-10-2016.
 */
public interface IEnergyGridInformation {

    public int getActiveConnections();

    public int getCurrentRP(EnumFacing side);

    public int getLastProcessedEF(EnumFacing side);

}
