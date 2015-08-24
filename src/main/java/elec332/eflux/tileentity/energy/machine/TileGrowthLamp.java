package elec332.eflux.tileentity.energy.machine;

import com.google.common.collect.Lists;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.BreakableReceiverTile;
import elec332.eflux.util.Config;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elec332 on 16-5-2015.
 */
public class TileGrowthLamp extends BreakableReceiverTile {

    public TileGrowthLamp(){
        this.blockLocations = Lists.newArrayList();
    }

    private List<BlockLoc> blockLocations;

    @Override
    public void updateEntity() {
        super.updateEntity();
        for (int i = 0; i < 9; i++) {
            if (blockLocations.isEmpty()) {
                remakeList();
            }
            Collections.shuffle(blockLocations, worldObj.rand);
            BlockLoc randomLoc = blockLocations.get(0);
            if (!worldObj.isAirBlock(randomLoc.xCoord, randomLoc.yCoord, randomLoc.zCoord) && isValidCrop(randomLoc) && !isFullyGrownCrop(randomLoc)) {
                WorldHelper.scheduleBlockUpdate(worldObj, randomLoc);
            }
            blockLocations.remove(randomLoc);
        }
    }

    private boolean isFullyGrownCrop(BlockLoc blockLoc){
        Block block = WorldHelper.getBlockAt(worldObj, blockLoc);
        int meta = WorldHelper.getBlockMeta(worldObj, blockLoc);
        return block instanceof IGrowable && !((IGrowable) block).func_149851_a(worldObj, blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord, worldObj.isRemote) || block instanceof BlockCrops && meta == 7;
    }

    private boolean isValidCrop(BlockLoc blockLoc){
        Block block = WorldHelper.getBlockAt(worldObj, blockLoc);
        return block instanceof IGrowable && block.getMaterial() != Material.grass;
    }

    private void remakeList(){
        blockLocations.clear();
        for (int offsetY= -Config.Machines.growthLampY; offsetY <= Config.Machines.growthLampY; offsetY++) {
            for (int offsetZ = -Config.Machines.growthLampXZ; offsetZ <= Config.Machines.growthLampXZ; offsetZ++) {
                for (int offsetX = -Config.Machines.growthLampXZ; offsetX <= Config.Machines.growthLampXZ; offsetX++) {
                    blockLocations.add(new BlockLoc(xCoord+offsetX, yCoord+offsetY, zCoord+offsetZ));
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
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return true;
    }

    @Override
    public int getRequestedRP() {
        return 7;
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }
}
