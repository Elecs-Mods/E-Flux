package elec332.eflux.tileentity.energy.machine;

import com.google.common.collect.Lists;
import elec332.core.api.annotations.RegisterTile;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.util.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elec332 on 16-5-2015.
 */
@RegisterTile(name = "TileEntityEFluxGrowthLamp")
public class TileGrowthLamp extends BreakableMachineTile {

    public TileGrowthLamp(){
        this.blockLocations = Lists.newArrayList();
    }

    private List<BlockPos> blockLocations;

    @Override
    public void update() {
        super.update();
        if (energyContainer.drainPower(120)) {
            for (int i = 0; i < 9; i++) {
                if (blockLocations.isEmpty()) {
                    remakeList();
                }
                Collections.shuffle(blockLocations, worldObj.rand);
                BlockPos randomLoc = blockLocations.get(0);
                if (!worldObj.isAirBlock(randomLoc) && isValidCrop(randomLoc) && !isFullyGrownCrop(randomLoc)) {
                    WorldHelper.scheduleBlockUpdate(worldObj, randomLoc);
                }
                blockLocations.remove(randomLoc);
            }
        }
    }

    private boolean isFullyGrownCrop(BlockPos blockLoc){
        Block block = WorldHelper.getBlockAt(worldObj, blockLoc);
        int meta = WorldHelper.getBlockMeta(worldObj, blockLoc);
        return block instanceof IGrowable && !((IGrowable) block).canGrow(worldObj, blockLoc, WorldHelper.getBlockState(worldObj, blockLoc), worldObj.isRemote) || block instanceof BlockCrops && meta == 7;
    }

    private boolean isValidCrop(BlockPos blockLoc){
        Block block = WorldHelper.getBlockAt(worldObj, blockLoc);
        return block instanceof IGrowable && block.getMaterial() != Material.grass;
    }

    private void remakeList(){
        blockLocations.clear();
        for (int offsetY= -Config.Machines.growthLampY; offsetY <= Config.Machines.growthLampY; offsetY++) {
            for (int offsetZ = -Config.Machines.growthLampXZ; offsetZ <= Config.Machines.growthLampXZ; offsetZ++) {
                for (int offsetX = -Config.Machines.growthLampXZ; offsetX <= Config.Machines.growthLampXZ; offsetX++) {
                    blockLocations.add(pos.add(offsetX, offsetY, offsetZ));
                }
            }
        }
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Blocks.glowstone);
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
