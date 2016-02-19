package elec332.eflux.multipart.cable;

/**
 * Created by Elec332 on 10-2-2016.
 */
public class PartNormalCable extends PartAbstractCable {

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

    @Override
    public int getMeta() {
        return 1;
    }

}
