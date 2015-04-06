package elec332.eflux.tileentity;

import elec332.eflux.util.IEFluxTile;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 5-4-2015.
 */
public abstract class BaseMultiBlockMachine extends BaseMachineTEWithInventory{

    public BaseMultiBlockMachine(int maxEnergy, int maxReceive, int invSize) {
        super(maxEnergy, maxReceive, invSize);
        if (this.formed)
            tryToForm();
        else this.formed = false;
    }

    private boolean formed;

    public boolean isFormed() {
        return formed;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.formed = tagCompound.getBoolean("formed");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("formed", this.formed);
    }

    public boolean tryToForm(){
        if (areBlocksAtRightLocations())
            this.formed = true;
        return areBlocksAtRightLocations();
    }

    protected abstract boolean areBlocksAtRightLocations();
}
