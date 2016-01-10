package elec332.eflux.multiblock.machine;

import elec332.core.util.BlockLoc;
import elec332.core.util.DirectionHelper;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.items.circuits.CircuitHandler;
import elec332.eflux.multiblock.EFluxMultiBlockMachine;
import elec332.eflux.util.MultiBlockLogic;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

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
        this.itemOutlet = getBlockLocAtTranslatedPos(2, 0, 0);

        this.distance = 0;
    }

    private BlockLoc laserController;
    private BlockLoc itemOutlet;

    private int distance;

    @Override
    public void onTick() {
        super.onTick();
        if (isServer() && getWorldObj().getTotalWorldTime() % 99 == 0 && getEnergyContainer().drainPower(3000)){
            List<ItemStack> dugBlocks = MultiBlockLogic.Laser.drill(getWorldObj(), laserController, DirectionHelper.rotateLeft(getMultiBlockFacing()), distance, 1);
            distance = MultiBlockLogic.Laser.getNewDistanceAfterMining(distance, DirectionHelper.rotateLeft(getMultiBlockFacing()));
            for (ItemStack stack : dugBlocks){
                MultiBlockLogic.dropStack(getWorldObj(), itemOutlet, getMultiBlockFacing(), stack);
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

    @Override
    protected int getOptimalRP() {
        return 30;
    }

    @Override
    protected int getMaxStoredPower() {
        return 980000;
    }

}
