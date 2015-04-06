package elec332.eflux.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 5-4-2015.
 */
public abstract class BaseMultiBlockMachine extends BaseMachineTEWithInventory{

    public BaseMultiBlockMachine(int maxEnergy, int maxReceive, int invSize) {
        super(maxEnergy, maxReceive, invSize);
    }

    private boolean formed = false;

    public boolean isFormed() {
        return this.formed;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (timeCheck()) {
            if (this.formed) {
                if (!areBlocksAtRightLocations()) {
                    this.formed = false;
                    this.modifyEnergyStored(-this.getMaxEnergyStored(ForgeDirection.UNKNOWN));
                    this.markDirty();
                }
            } else this.modifyEnergyStored(-this.getMaxEnergyStored(ForgeDirection.UNKNOWN));
        }
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return this.isFormed() && super.onBlockActivated(player, side, hitX, hitY, hitZ);
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
        if (areBlocksAtRightLocations()) {
            this.formed = true;
            this.markDirty();
            this.onCreated();
        }
        return areBlocksAtRightLocations();
    }

    protected abstract boolean areBlocksAtRightLocations();

    protected abstract void onCreated();

    public Block getBlockAtOffset(int x, int y, int z){
        return worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z);
    }
}
