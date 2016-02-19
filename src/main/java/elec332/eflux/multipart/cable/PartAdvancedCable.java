package elec332.eflux.multipart.cable;

import elec332.eflux.init.ItemRegister;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 10-2-2016.
 */
public class PartAdvancedCable extends PartAbstractCable {

    @Override
    public String getUniqueIdentifier() {
        return "avdc";
    }

    @Override
    public int getMaxEFTransfer() {
        return 200;
    }

    @Override
    public int getMaxRPTransfer() {
        return 50;
    }

    @Override
    public int getMeta() {
        return 2;
    }

}
