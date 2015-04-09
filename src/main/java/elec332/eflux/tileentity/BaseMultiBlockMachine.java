package elec332.eflux.tileentity;

import elec332.core.world.WorldHelper;
import elec332.eflux.util.IMultiBlock;
import elec332.eflux.util.ISaveData;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Elec332 on 5-4-2015.
 */
public abstract class BaseMultiBlockMachine extends BaseMachineTEWithInventory implements IMultiBlock{

    public BaseMultiBlockMachine(int maxEnergy, int maxReceive, int invSize) {
        super(maxEnergy, maxReceive, invSize);
        this.setAllMultiBlockDataToNull();
    }

    @ISaveData
    protected boolean formed;
    @ISaveData
    protected boolean master;
    @ISaveData
    protected boolean slave;
    @ISaveData
    protected int masterX;
    @ISaveData
    protected int masterY;
    @ISaveData
    protected int masterZ;

    public boolean isFormed() {
        if (this.isMaster())
            return this.formed;
        return this.isSlave() && this.getMaster().isFormed();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (timeCheck()) {
            if (this.isFormed() && this.isMaster()) {
                if (!areBlocksAtRightLocations()) {
                    this.invalidateMultiBlock();
                    this.modifyEnergyStored(-this.getMaxEnergyStored(ForgeDirection.UNKNOWN));
                    this.markDirty();
                    this.notifyNeighboursOfDataChange();
                }
            } else this.modifyEnergyStored(-this.getMaxEnergyStored(ForgeDirection.UNKNOWN));
        }
    }

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!this.isFormed() && !this.isSlave())
            return tryToForm(player);
        else if (this.isSlave())
            return this.getMaster().onBlockActivated(player, side, hitX, hitY, hitZ);
        return this.isMaster() && super.onBlockActivated(player, side, hitX, hitY, hitZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        super.readFromNBT(tagCompound);
        /*this.formed = tagCompound.getBoolean("formed");
        this.master = tagCompound.getBoolean("master");
        this.slave = tagCompound.getBoolean("slave");
        this.masterX = tagCompound.getInteger("masterX");
        this.masterY = tagCompound.getInteger("masterY");
        this.masterZ = tagCompound.getInteger("masterZ");*/
        try {
            for (Field field : this.getClass().getFields()) {
                System.out.println("There are " + this.getClass().getFields().length + " valid fields");
                field.setAccessible(true);
                if (field.isAnnotationPresent(ISaveData.class)) {
                    System.out.println(field.getName() + " is annotated with @ISaveData");
                    if (field.getType().equals(Integer.TYPE)) {
                        System.out.println(field.getName() + " was a valid integer");
                        tagCompound.setInteger(field.getName(), field.getInt(this));
                        System.out.println(field.getName() + " was read from NBT");
                    } else if (field.getType().equals(Boolean.TYPE)) {
                        System.out.println(field.getName() + " was a valid boolean");
                        tagCompound.setBoolean(field.getName(), field.getBoolean(this));
                        System.out.println(field.getName() + " was read from NBT");
                    }
                }
            }
        } catch (Throwable throwable){
            throwable.printStackTrace();
        }
        System.out.println("Read");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        /*tagCompound.setBoolean("formed", this.formed);
        tagCompound.setBoolean("master", this.master);
        tagCompound.setBoolean("slave", this.slave);
        tagCompound.setInteger("masterX", this.masterX);
        tagCompound.setInteger("masterY", this.masterY);
        tagCompound.setInteger("masterZ", this.masterZ);*/
        try {
            for (Field field : this.getClass().getFields()) {
                System.out.println("There are " + this.getClass().getFields().length + " valid fields");
                if (field.isAnnotationPresent(ISaveData.class)) {
                    System.out.println(field.getName() + " is annotated with @ISaveData");
                    if (field.getType().equals(Integer.TYPE)) {
                        System.out.println(field.getName() + " was a valid integer");
                        field.setInt(this, tagCompound.getInteger(field.getName()));
                        System.out.println(field.getName() + " was read from NBT");
                    } else if (field.getType().equals(Boolean.TYPE)){
                        System.out.println(field.getName() + " was a valid boolean");
                        field.setBoolean(this, tagCompound.getBoolean(field.getName()));
                        System.out.println(field.getName() + " was read from NBT");
                    }
                }
            }
        } catch (Throwable throwable){
            throwable.printStackTrace();
        }
    }

    public boolean tryToForm(EntityPlayer player){
        if (areBlocksAtRightLocations() && arePlayerConditionsMet(player) && areIMultiBlockTilesAtRightPosition()) {
            this.formed = true;
            this.onCreated();
            this.setMaster();
            this.markDirty();
            this.notifyNeighboursOfDataChange();
            return true;
        }
        return false;
    }

    private boolean areIMultiBlockTilesAtRightPosition(){
        for(Vec3 loc : this.getIMultiBlockLocations())
            if (!(getTileAt(this.worldObj, mergeLocationAndOffset(this.myLocation(), loc)) instanceof IMultiBlock))
                return false;
        return true;
    }

    protected abstract boolean arePlayerConditionsMet(EntityPlayer player);

    protected abstract boolean areBlocksAtRightLocations();

    protected abstract List<Vec3> getIMultiBlockLocations();

    protected abstract void onCreated();

    public Block getBlockAtOffset(int x, int y, int z){
        return worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z);
    }

    @Override
    public boolean isMaster() {
        return this.master;
    }

    @Override
    public boolean setMaster() {
        for (Vec3 loc : this.getIMultiBlockLocations()){
            ((BaseMultiBlockMachine)getTileAt(this.worldObj, mergeLocationAndOffset(this.myLocation(), loc))).setSlave(xCoord, yCoord, zCoord);
        }
        this.master = true;
        return true;
    }

    @Override
    public void setSlave(int masterX, int masterY, int masterZ) {
        this.setAllMultiBlockDataToNull();
        this.slave = true;
        this.masterX = masterX;
        this.masterY = masterY;
        this.masterZ = masterZ;
        this.notifyNeighboursOfDataChange();
    }

    @Override
    public void invalidateMultiBlock() {
        if (this.isMaster())
            for (Vec3 loc : this.getIMultiBlockLocations())
                if (getTileAt(this.worldObj, mergeLocationAndOffset(this.myLocation(), loc)) != null)
                    ((BaseMultiBlockMachine) getTileAt(this.worldObj, mergeLocationAndOffset(this.myLocation(), loc))).invalidateMultiBlock();
        this.setAllMultiBlockDataToNull();
        this.notifyNeighboursOfDataChange();
    }

    private void setAllMultiBlockDataToNull(){
        this.formed = false;
        this.master = false;
        this.slave = false;
        this.masterX = 0;
        this.masterY = 0;
        this.masterZ = 0;
    }

    @Override
    public boolean isSlave() {
        return this.slave;
    }

    @Override
    public BaseMultiBlockMachine getMaster() {
        return (BaseMultiBlockMachine) this.worldObj.getTileEntity(this.masterX, this.masterY, this.masterZ);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (this.isMaster())
            this.invalidateMultiBlock();
        else if (this.isSlave())
            this.getMaster().invalidateMultiBlock();
    }

    public TileEntity getTileAt(World world, Vec3 vec3){
        return WorldHelper.getTileAt(world, vec3);
    }

    public Vec3 mergeLocationAndOffset(Vec3 location, Vec3 offset){
        return Vec3.createVectorHelper(location.xCoord+offset.xCoord, location.yCoord+offset.yCoord, location.zCoord+offset.zCoord);
    }

    //Overrides for master block

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (this.isSlave())
            return this.getMaster().receiveEnergy(from, maxReceive, simulate);
        return this.isFormed()?super.receiveEnergy(from, maxReceive, simulate):0;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (this.isSlave())
            return this.getMaster().getEnergyStored(from);
        return this.isFormed()?super.getEnergyStored(from):0;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (this.isSlave())
            return this.getMaster().getMaxEnergyStored(from);
        return this.isFormed()?super.getMaxEnergyStored(from):0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        if (this.isSlave())
            return this.getMaster().canConnectEnergy(from);
        return super.canConnectEnergy(from) && this.isFormed();
    }
}
