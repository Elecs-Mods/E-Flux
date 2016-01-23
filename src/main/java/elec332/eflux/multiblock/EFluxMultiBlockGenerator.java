package elec332.eflux.multiblock;

import elec332.core.multiblock.AbstractMultiBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 13-9-2015.
 */
public abstract class EFluxMultiBlockGenerator extends AbstractMultiBlock implements MultiBlockInterfaces.IEFluxMultiBlockPowerProvider {

    public EFluxMultiBlockGenerator(){
        storedPower = 0;
    }

    protected void generatePower(int power){
        int i = maxStoredPower() - storedPower;
        if (power > i){
            storedPower = maxStoredPower();
            return;
        }
        storedPower += power;
    }

    protected int storedPower;

    @Override
    public int provideEnergy(int rp, boolean execute) {
        int maxPower = Math.min(storedPower, getMaxProvidedPower());
        if (execute){
            storedPower -= maxPower;
        }
        return maxPower/rp;
    }

    /**
     * This returns the maximum amount of power that can be drawn from 1 request,
     * remember, this not the amount of EF or RP, but RP * EF.
     *
     * @return The maximum amount of power allowed to be drawn.
     */
    public abstract int getMaxProvidedPower();


    /**
     * This returns the maximum amount of power that can be stored, but remember:
     * This value should be low, only a buffer for a couple of ticks of generated power.
     *
     * @return the maximum amount of power that can be stored.
     */
    public abstract int maxStoredPower();

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("internalStoredPower", storedPower);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        storedPower = tagCompound.getInteger("internalStoredPower");
    }

    /**
     * Invalidate your multiblock here
     */
    @Override
    public void invalidate() {

    }

    @Override
    public boolean onAnyBlockActivated(EntityPlayer player) {
        return false;
    }

}
