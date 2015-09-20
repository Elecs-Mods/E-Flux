package elec332.eflux.tileentity.energy.cable;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class NormalCable extends AbstractCable {

    @Override
    public String getUniqueIdentifier() {
        return "mjhg";
    }

    @Override
    public int getMaxEFTransfer() {
        return 50;
    }

    @Override
    public int getMaxRPTransfer() {
        return 20;
    }

}
