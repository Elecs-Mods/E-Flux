package elec332.eflux.util;

import com.google.common.collect.Lists;
import elec332.core.util.BasicInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

/**
 * Created by Elec332 on 7-9-2015.
 */
public class RIWInventory extends BasicInventory{

    public RIWInventory(int mode){
        this(mode, null);
    }

    public RIWInventory(int mode, TileEntity tile) {
        super("render", mode == 0 ? 64 : 8, tile);
        list = Lists.newArrayList();
        this.mode = (byte)mode;
    }

    private byte mode;
    private List<IRIWInventoryChangedHook> list;

    public void addHook(IRIWInventoryChangedHook hook){
        list.add(hook);
    }

    public void removeHook(IRIWInventoryChangedHook hook){
        list.remove(hook);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        for (IRIWInventoryChangedHook hook : list)
            hook.onRIWInventoryChanged();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int id, ItemStack stack) {
        boolean b = stack.getItem() instanceof ItemBlock;
        return mode == 0 ? !b : b;
    }

    public NBTTagCompound getPacketData(){
        NBTTagCompound tagCompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < this.inventoryContents.length; ++i) {
            if(this.inventoryContents[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte)i);
                ItemStack toSend = this.inventoryContents[i].copy();
                toSend.setTagCompound(null);
                toSend.writeToNBT(tag);
                nbttaglist.appendTag(tag);
            }
        }
        tagCompound.setTag("Items", nbttaglist);
        tagCompound.setByte("mode", mode);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.mode = compound.getByte("mode");
        inventoryContents = new ItemStack[mode == 0 ? 64 : 0];
        super.readFromNBT(compound);

    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setByte("mode", mode);
    }

    public boolean isEmpty(){
        for (int i = 0; i < getSizeInventory(); i++) {
            if (getStackInSlot(i) != null)
                return false;
        }
        return true;
    }


    public interface IRIWInventoryContainer{

        public RIWInventory getInventory();

    }

    public interface IRIWInventoryChangedHook{

        public void onRIWInventoryChanged();

    }

}
