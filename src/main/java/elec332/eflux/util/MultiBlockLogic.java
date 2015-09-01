package elec332.eflux.util;

import com.google.common.collect.Lists;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 1-9-2015.
 */
public class MultiBlockLogic {

    public static void dropStack(World world, BlockLoc loc, ForgeDirection direction, ItemStack stack){
        WorldHelper.dropStack(world, loc.atSide(direction), stack);
    }

    public static class Laser{

        public static List<ItemStack> drill(World worldObj, BlockLoc loc, ForgeDirection facing, int distance, int range){
            if (!worldObj.isRemote){
                List<ItemStack> toDrop = Lists.newArrayList();
                for (BlockLoc blockLoc : getNewBlocksToMine(loc, facing, distance, range)) {
                    toDrop.addAll(WorldHelper.getBlockAt(worldObj, blockLoc).getDrops(worldObj, blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord, WorldHelper.getBlockMeta(worldObj, blockLoc), 1));
                    worldObj.setBlockToAir(blockLoc.xCoord, blockLoc.yCoord, blockLoc.zCoord);
                }
                return toDrop;
            }
            return null;
        }

        private static List<BlockLoc> getNewBlocksToMine(BlockLoc loc, ForgeDirection facing, int distance, int range){
            List<BlockLoc> ret = Lists.newArrayList();
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

        private static void addAllAround(List<BlockLoc> list, axis axis, BlockLoc loc, int distance, int range){
            BlockLoc blockLoc;
            if (axis == MultiBlockLogic.Laser.axis.z){
                blockLoc = new BlockLoc(loc.xCoord + distance, loc.yCoord, loc.zCoord);
            } else blockLoc = new BlockLoc(loc.xCoord, loc.yCoord, loc.zCoord+distance);
            for (int offsetY = -range; offsetY <= range; offsetY++) {
                for (int otherAxis = -range; otherAxis <= range; otherAxis++) {
                    if (axis == MultiBlockLogic.Laser.axis.x){
                        list.add(new BlockLoc(blockLoc.xCoord + otherAxis, blockLoc.yCoord+offsetY, blockLoc.zCoord));
                    } else if (axis == MultiBlockLogic.Laser.axis.z){
                        list.add(new BlockLoc(blockLoc.xCoord, blockLoc.yCoord+offsetY, blockLoc.zCoord+otherAxis));
                    }
                }
            }
        }

        private enum axis{
            x, z
        }
    }

}
