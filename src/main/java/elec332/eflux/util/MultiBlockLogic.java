package elec332.eflux.util;

import com.google.common.collect.Lists;
import elec332.core.world.WorldHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 1-9-2015.
 */
public class MultiBlockLogic {

    public static void dropStack(World world, BlockPos loc, EnumFacing direction, ItemStack stack){
        WorldHelper.dropStack(world, loc.offset(direction), stack);
    }

    public static class Laser{

        public static List<ItemStack> drill(World worldObj, BlockPos loc, EnumFacing facing, int distance, int range){
            if (!worldObj.isRemote){
                List<ItemStack> toDrop = Lists.newArrayList();
                for (BlockPos blockLoc : getNewBlocksToMine(loc, facing, distance, range)) {
                    toDrop.addAll(WorldHelper.getBlockAt(worldObj, blockLoc).getDrops(worldObj, blockLoc, WorldHelper.getBlockState(worldObj, blockLoc), 1));
                    worldObj.setBlockToAir(blockLoc);
                }
                return toDrop;
            }
            return null;
        }

        public static int getNewDistanceAfterMining(int oldDistance, EnumFacing facing){
            switch (facing){
                case NORTH:
                    oldDistance--;
                    break;
                case SOUTH:
                    oldDistance++;
                    break;
                case EAST:
                    oldDistance++;
                    break;
                case WEST:
                    oldDistance--;
                    break;
            }
            return oldDistance;
        }

        private static List<BlockPos> getNewBlocksToMine(BlockPos loc, EnumFacing facing, int distance, int range){
            List<BlockPos> ret = Lists.newArrayList();
            switch (facing){
                case NORTH:
                    distance--;
                    addAllAround(ret, axis.x, loc, distance, range);
                    break;
                case SOUTH:
                    distance++;
                    addAllAround(ret, axis.x, loc, distance, range);
                    break;
                case EAST:
                    distance++;
                    addAllAround(ret, axis.z, loc, distance, range);
                    break;
                case WEST:
                    distance--;
                    addAllAround(ret, axis.z, loc, distance, range);
                    break;
            }
            return ret;
        }

        private static void addAllAround(List<BlockPos> list, axis axis, BlockPos loc, int distance, int range){
            BlockPos blockLoc;
            if (axis == MultiBlockLogic.Laser.axis.z){
                blockLoc = loc.add(distance, 0, 0);
            } else {
                blockLoc = loc.add(0, 0, distance);
            }
            for (int offsetY = -range; offsetY <= range; offsetY++) {
                for (int otherAxis = -range; otherAxis <= range; otherAxis++) {
                    if (axis == MultiBlockLogic.Laser.axis.x){
                        list.add(blockLoc.add(otherAxis, offsetY, 0));
                    } else if (axis == MultiBlockLogic.Laser.axis.z){
                        list.add(blockLoc.add(0, offsetY, otherAxis));
                    }
                }
            }
        }

        private enum axis{
            x, z
        }
    }

}
