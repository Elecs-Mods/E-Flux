package elec332.eflux.tileentity.energy.machine.furnace;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 6-5-2015.
 */
public class T1Furnace extends TileFurnaceBase {

    @Override
    public int getRequiredPowerPerTick() {
        return 10;
    }

    @Override
    protected int getMaxStoredPower() {
        return 3000;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.iron_ingot);
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 10;
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 5;
    }
}
