package elec332.eflux.api.energy;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 16-4-2015.
 */
interface IEnergyTile {

    default public boolean canConnectTo(ConnectionType myType, @Nonnull TileEntity otherTile, ConnectionType otherType, @Nonnull IEnergyTile otherConnector){
        return true;
    }

}
