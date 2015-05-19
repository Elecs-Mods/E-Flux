package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.tileentity.BreakableReceiverTile;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 16-5-2015.
 */
public class TileGrowthLamp extends BreakableReceiverTile {

    @Override
    public void updateEntity() {
        super.updateEntity();
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.glowstone);
    }

    @Override
    public float getAcceptance() {
        return 0.09f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 10;
    }

    @Override
    public int getMaxEF(int rp) {
        return 10;
    }

    @Override
    protected int getMaxStoredPower() {
        return 700;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 7;
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }
}
