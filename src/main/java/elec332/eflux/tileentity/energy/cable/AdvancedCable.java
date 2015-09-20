package elec332.eflux.tileentity.energy.cable;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class AdvancedCable extends AbstractCable {

    @Override
    public String getUniqueIdentifier() {
        return "avdc";
    }

    @Override
    public int getMaxEFTransfer() {
        return 100;
    }

    @Override
    public int getMaxRPTransfer() {
        return 50;
    }

}
