package elec332.eflux.api.energy;

/**
 * Created by Elec332 on 16-4-2015.
 */
public interface IEnergyTransmitter extends IEnergyTile{

    public String getUniqueIdentifier();

    public int getMaxEFTransfer();

    public int getMaxRPTransfer();

}
