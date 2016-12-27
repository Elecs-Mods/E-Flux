package elec332.eflux.tileentity.energy.machine;

import com.google.common.collect.Lists;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import elec332.eflux.util.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elec332 on 16-5-2015.
 */
@RegisteredTileEntity("TileEntityEFluxGrowthLamp")
public class TileEntityGrowthLamp extends TileEntityBreakableMachine implements ITickable {

    public TileEntityGrowthLamp(){
        this.blockLocations = Lists.newArrayList();
    }

    private List<BlockPos> blockLocations;

    @Override
    public void update() {
        if (energyContainer.drainPower(120)) {
            for (int i = 0; i < 9; i++) {
                if (blockLocations.isEmpty()) {
                    remakeList();
                }
                Collections.shuffle(blockLocations, getWorld().rand);
                BlockPos randomLoc = blockLocations.get(0);
                if (!getWorld().isAirBlock(randomLoc) && isValidCrop(randomLoc) && !isFullyGrownCrop(randomLoc)) {
                    WorldHelper.scheduleBlockUpdate(getWorld(), randomLoc);
                }
                blockLocations.remove(randomLoc);
            }
        }
    }

    private boolean isFullyGrownCrop(BlockPos blockLoc){
        Block block = WorldHelper.getBlockAt(getWorld(), blockLoc);
        int meta = WorldHelper.getBlockMeta(getWorld(), blockLoc);
        return block instanceof IGrowable && !((IGrowable) block).canGrow(getWorld(), blockLoc, WorldHelper.getBlockState(getWorld(), blockLoc), getWorld().isRemote) || block instanceof BlockCrops && meta == 7;
    }

    private boolean isValidCrop(BlockPos blockLoc){
        IBlockState state = WorldHelper.getBlockState(getWorld(), blockLoc);
        Block block = state.getBlock();
        return block instanceof IGrowable && block.getMaterial(state) != Material.GRASS;
    }

    private void remakeList(){
        blockLocations.clear();
        for (int offsetY= -Config.Machines.GrowthLamp.growthLampY; offsetY <= Config.Machines.GrowthLamp.growthLampY; offsetY++) {
            for (int offsetZ = -Config.Machines.GrowthLamp.growthLampXZ; offsetZ <= Config.Machines.GrowthLamp.growthLampXZ; offsetZ++) {
                for (int offsetX = -Config.Machines.GrowthLamp.growthLampXZ; offsetX <= Config.Machines.GrowthLamp.growthLampXZ; offsetX++) {
                    blockLocations.add(pos.add(offsetX, offsetY, offsetZ));
                }
            }
        }
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.GLOWSTONE);
    }

    @Override
    public float getAcceptance() {
        return 0.09f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 10;
    }

    @Override
    protected int getMaxStoredPower() {
        return 700;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction == EnumFacing.UP;
    }

    @Override
    public int getRequestedRP() {
        return 7;
    }

}
