package elec332.eflux.multipart.cable;

import elec332.eflux.init.ItemRegister;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 10-2-2016.
 */
public class PartBasicCable extends PartAbstractCable {

    @Override
    public String getUniqueIdentifier() {
        return "q49d";
    }

    @Override
    public int getMaxEFTransfer() {
        return 10;
    }

    @Override
    public int getMaxRPTransfer() {
        return 5;
    }

    @Override
    public int getMeta() {
        return 0;
    }
}
