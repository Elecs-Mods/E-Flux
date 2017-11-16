package elec332.eflux.api.energy;

import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyTransmitter extends IEnergyTile {

    @Override
    public boolean canConnectTo(ConnectionType myType, @Nonnull TileEntity otherTile, ConnectionType otherType, @Nonnull IEnergyTile otherConnector);

    public int getMaxEFTransfer();

    public int getMaxRPTransfer();

    default public int getMaxPowerTransfer() {
        return getMaxEFTransfer() * getMaxRPTransfer();
    }

    public default float getCableLength(){
        return 1;
    }

}
