package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multiblock.machine.MultiBlockGrinder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

/**
 * Created by Elec332 on 10-9-2015.
 */
@RegisterTile(name = "TileEntityEFluxDustStorage")
public class TileEntityDustStorage extends TileMultiBlockTile implements ISidedInventory{

    private ItemStack stored;
    private boolean redstone;

    @Override
    public void onNeighborBlockChange(Block block) {
        if (!worldObj.isRemote) {
            boolean b = worldObj.isBlockIndirectlyGettingPowered(pos) != 0;
            if (redstone != b) {
                if (b) {
                    pulse();
                }
                redstone = b;
            }
        }
    }

    private void pulse(){
        if (stored == null && getMultiBlock() != null){
            stored = ((MultiBlockGrinder)getMultiBlock()).extractItem();
            markDirty();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (stored != null) {
            NBTTagCompound tag = new NBTTagCompound();
            stored.writeToNBT(tag);
            tag.setTag("storedDust", tag);
        }
        tagCompound.setBoolean("lastRedstone", redstone);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("storedDust")){
            stored = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("storedDust"));
        }
        redstone = tagCompound.getBoolean("lastRedstone");
    }

    @Override
    public int[] getSlotsForFace(EnumFacing s) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, ItemStack p_102007_2_, EnumFacing p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, EnumFacing p_102008_3_) {
        return true;//((MultiBlockGrinder)getMultiBlock()).extractItem(p_102008_2_);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int p_70301_1_) {
        if (p_70301_1_ == 0)
            return stored;
        return null;
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (p_70298_1_ == 0) {
            final ItemStack ret = stored;
            stored = null;
            return ret;
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int p_70304_1_) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        if (stored == null && p_70299_2_ != null && p_70299_2_.getItem() == ItemRegister.groundMesh){
            stored = p_70299_2_;
            return;
        }
        throw new RuntimeException();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName() {
        return false;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    public IChatComponent getDisplayName() {
        return null;
    }
}
