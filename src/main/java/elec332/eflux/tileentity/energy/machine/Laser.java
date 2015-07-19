package elec332.eflux.tileentity.energy.machine;

import com.google.common.collect.Lists;
import elec332.core.util.BlockLoc;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.items.circuits.CircuitHandler;
import elec332.eflux.tileentity.BreakableReceiverTile;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 24-5-2015.
 */
public class Laser extends BreakableReceiverTile {

    public Laser(){
        this.distance = 0;
        this.range = 1;
    }

    @Override
    public void onTileLoaded() {
        super.onTileLoaded();
        updateFacing();
    }

    private int distance;
    private ForgeDirection facing;
    private int range;


    @Override
    public void onWrenched(ForgeDirection forgeDirection) {
        super.onWrenched(forgeDirection);
        updateFacing();
    }

    public void updateFacing(){
        this.facing = DirectionHelper.getDirectionFromNumber(getBlockMetadata());
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (timeCheck() && !worldObj.isRemote && storedPower >=3000) {
            storedPower -= 3000;
            List<ItemStack> toDrop = Lists.newArrayList();
            for (BlockLoc blockLoc : getNewBlocksToMine()) {
                toDrop.addAll(WorldHelper.getBlockAt(worldObj, blockLoc).getDrops(worldObj, blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord, WorldHelper.getBlockMeta(worldObj, blockLoc), 1));
                worldObj.setBlockToAir(blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord);
            }
            for (ItemStack stack : toDrop){
                WorldHelper.dropStack(worldObj, myLocation(), stack);
            }
            syncData();
            markDirty();
        }
    }

    public boolean timeCheck() {
        return this.worldObj.getTotalWorldTime() % 99 == 0;
    }

    private List<BlockLoc> getNewBlocksToMine(){
        List<BlockLoc> ret = Lists.newArrayList();
        switch (facing){
            case NORTH:
                distance--;
                addAllAround(ret, axis.x);
                break;
            case SOUTH:
                distance++;
                addAllAround(ret, axis.x);
                break;
            case EAST:
                distance++;
                addAllAround(ret, axis.z);
                break;
            case WEST:
                distance--;
                addAllAround(ret, axis.z);
                break;
        }
        return ret;
    }

    private void addAllAround(List<BlockLoc> list, axis axis){
        int i = range;
        BlockLoc blockLoc;
        if (axis == Laser.axis.z){
            blockLoc = new BlockLoc(myLocation().xCoord + distance, myLocation().yCoord, myLocation().zCoord);
        } else blockLoc = new BlockLoc(myLocation().xCoord, myLocation().yCoord, myLocation().zCoord+distance);
        for (int offsetY = -i; offsetY <= i; offsetY++) {
            for (int otherAxis = -i; otherAxis <= i; otherAxis++) {
                if (axis == Laser.axis.x){
                    list.add(new BlockLoc(blockLoc.xCoord + otherAxis, blockLoc.yCoord+offsetY, blockLoc.zCoord));
                } else if (axis == Laser.axis.z){
                    list.add(new BlockLoc(blockLoc.xCoord, blockLoc.yCoord+offsetY, blockLoc.zCoord+otherAxis));
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("dist", distance);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.distance = tagCompound.getInteger("dist");
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(CircuitHandler.get(EnumCircuit.SMALL).circuitItem, 1, 0);
    }

    @Override
    public float getAcceptance() {
        return 0.23f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 10; //TESTING change to 60-ish
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
        return 30;
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }

    @Override
    protected int getMaxStoredPower() {
        return 980000;
    }

    private enum axis{
        x, z
    }
}
