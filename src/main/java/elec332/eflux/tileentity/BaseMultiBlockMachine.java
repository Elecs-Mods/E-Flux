package elec332.eflux.tileentity;

import elec332.core.world.WorldHelper;
import elec332.eflux.util.IMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 5-4-2015.
 */
public abstract class BaseMultiBlockMachine extends BaseMachineTEWithInventory implements IMultiBlock{

    public BaseMultiBlockMachine(int maxEnergy, int maxReceive, int invSize) {
        super(maxEnergy, maxReceive, invSize);
        this.setAllMultiBlockDataToNull();
    }

    private boolean formed;
    private boolean master;
    private boolean slave;
    private int masterX;
    private int masterY;
    private int masterZ;
    private List<Vec3> slaveLocations;

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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.formed = tagCompound.getBoolean("formed");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("formed", this.formed);
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
    public World world() {
        return this.worldObj;
    }

    @Override
    public int x() {
        return this.xCoord;
    }

    @Override
    public int y() {
        return this.yCoord;
    }

    @Override
    public int z() {
        return this.zCoord;
    }

    @Override
    public boolean isMaster() {
        return this.master;
    }

    @Override
    public boolean setMaster() {
        this.slaveLocations = this.getIMultiBlockLocations();
        for (Vec3 loc : this.slaveLocations){
            ((BaseMultiBlockMachine)getTileAt(this.worldObj, mergeLocationAndOffset(this.myLocation(), loc))).setSlave(x(), y(), z());
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
            for (Vec3 loc : this.slaveLocations)
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
        this.slaveLocations = null;
    }

    @Override
    public boolean isSlave() {
        return this.slave;
    }

    @Override
    public BaseMultiBlockMachine getMaster() {
        return (BaseMultiBlockMachine) this.world().getTileEntity(this.masterX, this.masterY, this.masterZ);
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
        return super.receiveEnergy(from, maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        if (this.isSlave())
            return this.getMaster().getEnergyStored(from);
        return super.getEnergyStored(from);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        if (this.isSlave())
            return this.getMaster().getMaxEnergyStored(from);
        return super.getMaxEnergyStored(from);
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        if (this.isSlave())
            return this.getMaster().canConnectEnergy(from);
        return super.canConnectEnergy(from);
    }
}
