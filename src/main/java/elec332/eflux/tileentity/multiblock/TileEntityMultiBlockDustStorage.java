package elec332.eflux.tileentity.multiblock;

import elec332.core.api.inventory.IDefaultInventory;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multiblock.machine.MultiBlockGrinder;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 10-9-2015.
 */
@RegisteredTileEntity("TileEntityEFluxDustStorage")
public class TileEntityMultiBlockDustStorage extends AbstractTileEntityMultiBlock implements ISidedInventory, IDefaultInventory {

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
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (stored != null) {
            NBTTagCompound tag = new NBTTagCompound();
            stored.writeToNBT(tag);
            tag.setTag("storedDust", tag);
        }
        tagCompound.setBoolean("lastRedstone", redstone);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("storedDust")){
            stored = ItemStackHelper.loadItemStackFromNBT(tagCompound.getCompoundTag("storedDust"));
        }
        redstone = tagCompound.getBoolean("lastRedstone");
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing s) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int p_102007_1_, @Nonnull ItemStack p_102007_2_, @Nonnull EnumFacing p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(int p_102008_1_, @Nonnull ItemStack p_102008_2_, @Nonnull EnumFacing p_102008_3_) {
        return true;//((MultiBlockGrinder)getMultiBlock()).extractItem(p_102008_2_);
    }

    @Nonnull
    @Override
    public IInventory getInventory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int p_70301_1_) {
        if (p_70301_1_ == 0)
            return stored;
        return ItemStackHelper.NULL_STACK;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if (p_70298_1_ == 0) {
            final ItemStack ret = stored;
            stored = null;
            return ret;
        }
        return ItemStackHelper.NULL_STACK;
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int p_70304_1_) {
        return ItemStackHelper.NULL_STACK;
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, @Nonnull ItemStack p_70299_2_) {
        if (stored == null && ItemStackHelper.isStackValid(p_70299_2_) && p_70299_2_.getItem() == ItemRegister.groundMesh){
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
    public boolean isUseableByPlayer(@Nonnull EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, @Nonnull ItemStack p_94041_2_) {
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
    @Nonnull
    public String getName() {
        return "";
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
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentString("");
    }

}
