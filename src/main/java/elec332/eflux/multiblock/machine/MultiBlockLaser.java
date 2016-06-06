package elec332.eflux.multiblock.machine;

import elec332.core.util.DirectionHelper;
import elec332.core.util.NBTHelper;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.multiblock.EFluxMultiBlockMachine;
import elec332.eflux.tileentity.basic.TileEntityLaser;
import elec332.eflux.util.MultiBlockLogic;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Elec332 on 28-8-2015.
 */
public class MultiBlockLaser extends EFluxMultiBlockMachine {

    public MultiBlockLaser(){
        super();
    }

    @Override
    public void init() {
        super.init();
        this.laserController = getBlockLocAtTranslatedPos(0, 1, 1);
        setItemOutput(4, 1, 0);
        this.distance = 0;
        this.laser = (TileEntityLaser) getTileAtTranslatedPos(1, 1, 1);
    }

    private BlockPos laserController;
    private TileEntityLaser laser;

    private int distance;
    private boolean active;

    @Override
    public void onTick() {
        super.onTick();
        if (isServer() && getWorldObj().getTotalWorldTime() % 99 == 0){
            if (getEnergyContainer().drainPower(3000)) {
                if (!active) {
                    setActive(true);
                }
                List<ItemStack> dugBlocks = MultiBlockLogic.Laser.drill(getWorldObj(), laserController, DirectionHelper.rotateLeft(getMultiBlockFacing()), distance, 1);
                distance = MultiBlockLogic.Laser.getNewDistanceAfterMining(distance, DirectionHelper.rotateLeft(getMultiBlockFacing()));
                for (ItemStack stack : dugBlocks) {
                    ejectStack(stack);
                }
                setLaserPos(getLaserLocation());
            }else if (active){
                setActive(false);
            }
        }
    }

    private void setActive(boolean active){
        this.active = active;
        laser.sendPacket(2, new NBTHelper().addToTag(active, "a").serializeNBT());
    }

    private void setLaserPos(BlockPos pos){
        laser.sendPacket(3, new NBTHelper().addToTag(pos).serializeNBT());
    }

    @Override
    public void invalidate() {
        setActive(false);
        setLaserPos(BlockPos.ORIGIN);
        super.invalidate();
    }

    public boolean isActive(){
        return active;
    }

    public BlockPos getLaserLocation(){
        EnumFacing facing = DirectionHelper.rotateLeft(getMultiBlockFacing());
        return (facing == EnumFacing.EAST || facing == EnumFacing.WEST) ? laserController.add(distance, 0, 0) : ((facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) ? laserController.add(0, 0, distance) : laserController);
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
        return new ItemStack(Items.APPLE);//TODO CircuitHandler.get(EnumCircuit.SMALL).getUnrefinedCircuit();
    }

    @Override
    public float getAcceptance() {
        return 0.23f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 54; //TESTING change to 60-ish
    }

    @Override
    public int getOptimalRP() {
        return 30;
    }

    @Override
    protected int getMaxStoredPower() {
        return 980000;
    }

}
