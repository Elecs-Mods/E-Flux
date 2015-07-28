package elec332.eflux.multiblock;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.core.player.PlayerHelper;
import elec332.core.server.ServerHelper;
import elec332.core.util.BlockLoc;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 26-7-2015.
 */
public final class MultiBlockStructureRegistry {

    protected MultiBlockStructureRegistry(MultiBlockRegistry multiBlockRegistry){
        this.multiBlockStructures = Maps.newHashMap();
        this.multiBlockRegistry = multiBlockRegistry;
    }

    private final MultiBlockRegistry multiBlockRegistry;

    protected void registerMultiBlockStructure(IMultiBlockStructure multiBlock, String name){
        multiBlockStructures.put(name, multiBlock);
        //System.out.println("Registered structure with name: "+name);
    }

    private Map<String, IMultiBlockStructure> multiBlockStructures;

    protected String getIdentifier(IMultiBlockStructure structure){
        for (Map.Entry<String, IMultiBlockStructure> entry : multiBlockStructures.entrySet()){
            if (entry.getValue().equals(structure)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("ERROR: Structure: "+structure.getClass().getName()+" is not registered!");
    }

    public boolean attemptCreate(World world, int x, int y, int z, ForgeDirection side){
        if (world.isRemote)
            return false;
        BlockData blockData = atLocation(world, x, y, z);
        if (blockData == null)
            return false;
        for (IMultiBlockStructure multiBlock : multiBlockStructures.values()){
            if (multiBlock.getTriggerBlock().equals(blockData)){
                if (tryCreateStructure(multiBlock, world, x, y, z, side)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean attemptReCreate(String s, TileEntity tile, ForgeDirection facing){
        return tryCreateStructure(multiBlockStructures.get(s), tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord, facing);
    }

    private boolean tryCreateStructure(IMultiBlockStructure multiBlock, World world, int x, int y, int z, ForgeDirection side){
        if (side == ForgeDirection.UNKNOWN || side == ForgeDirection.UP || side == ForgeDirection.DOWN)
            return false;
        BlockData leftBottomCorner = null;
        int newX = x;
        int newY = y;
        int newZ = z;
        BlockData temp1;
        //for (BlockData data : multiBlock.getStructure().getBlockTypes())
        //    System.out.println(data.toString());
        xLoop:
        for (int i = 0; i < multiBlock.getStructure().getHn()+1; i++) {
            if (side == ForgeDirection.NORTH || side == ForgeDirection.EAST) {
                temp1 = atLocation(world, newX + 1, y, z);
                //System.out.println("ScanningX_NE: "+newX+","+newY+","+newZ+" contains: "+temp1);
                if (temp1 != null && multiBlock.getStructure().getBlockTypes().contains(temp1)) {
                    newX++;
                    continue;
                }
            } else if (side == ForgeDirection.SOUTH || side == ForgeDirection.WEST){
                temp1 = atLocation(world, newX - 1, y, z);
                //System.out.println("ScanningX_SW: "+newX+","+newY+","+newZ+" contains: "+temp1);
                if (temp1 != null && multiBlock.getStructure().getBlockTypes().contains(temp1)) {
                    newX--;
                    continue;
                }
            }
            for (int j = 0; j < multiBlock.getStructure().getHn() + 1; j++) {
                newY--;
                temp1 = atLocation(world, newX, newY, z);
                //System.out.println("ScanningY: "+newX+","+newY+","+newZ+" contains: "+temp1);
                if (temp1 != null && multiBlock.getStructure().getBlockTypes().contains(temp1))
                    continue;
                newY++;
                for (int k = 0; k < multiBlock.getStructure().getHn() + 1; k++) {
                    if (side == ForgeDirection.NORTH || side == ForgeDirection.WEST) {
                        temp1 = atLocation(world, newX, newY, newZ - 1);
                        //System.out.println("ScanningZ_NW: "+newX+","+newY+","+newZ+" contains: "+temp1);
                        if (temp1 != null && multiBlock.getStructure().getBlockTypes().contains(temp1)) {
                            newZ--;
                            continue;
                        }
                    } else if (side == ForgeDirection.EAST || side == ForgeDirection.SOUTH){
                        temp1 = atLocation(world, newX, newY, newZ + 1);
                        //System.out.println("ScanningZ_ES: "+newX+","+newY+","+newZ+" contains: "+temp1);
                        if (temp1 != null && multiBlock.getStructure().getBlockTypes().contains(temp1)) {
                            newZ++;
                            continue;
                        }
                    }
                    //newZ++; //Works, but why??
                    //System.out.println("BottomLeft = "+newX+","+newY+","+newZ);
                    leftBottomCorner = atLocation(world, newX, newY, newZ);
                    //System.out.println("Scanning: "+newX+","+newY+","+newZ+" is corner: "+leftBottomCorner);
                    break xLoop;
                }
            }
        }
        if (leftBottomCorner != null && areBlocksAtRightPlace(multiBlock.getStructure(), world, newX, newY, newZ, side)){
            if (multiBlock.replaceUponCreated() != null) {
                BlockStructure main = multiBlock.getStructure();
                replaceAll(main, world, newX, newY, newZ, side, main.newBlockStructureWithSameDimensions(multiBlock.replaceUponCreated()));
            }
            //newZ--;
            multiBlockRegistry.get(world).createNewMultiBlock(multiBlock, new BlockLoc(newX, newY, newZ), getAllMultiBlockLocations(multiBlock.getStructure(), newX, newY, newZ, side), world, side);
            return true;
        }
        return false;
    }

    private List<BlockLoc> getAllMultiBlockLocations(final BlockStructure multiBlock, final int x, final int y, final int z, final ForgeDirection side){
        final List<BlockLoc> ret = Lists.newArrayList();
        multiBlock.startLoop(new BlockStructure.IPositionCall() {
            @Override
            public void forPos(int length, int width, int height) {
                ret.add(getTranslated(x, y, z, side, length, width, height));
            }
        });
        return ret;
    }

    private void replaceAll(final BlockStructure multiBlock, final World world, final int x, final int y, final int z, final ForgeDirection side, final BlockStructure toReplace){
        multiBlock.startLoop(new BlockStructure.IPositionCall() {
            @Override
            public void forPos(int length, int width, int height) {
                BlockLoc loc = getTranslated(x, y, z, side, length, width, height);
                BlockData data = toReplace.getStructure()[length][width][height];
                world.setBlockToAir(loc.xCoord, loc.yCoord, loc.zCoord);
                world.setBlock(loc.xCoord, loc.yCoord, loc.zCoord, data.block, data.meta, 3);
            }
        });
    }

    private boolean areBlocksAtRightPlace(final BlockStructure multiBlock, final World world, final int x, final int y, final int z, final ForgeDirection side){
        try {
            multiBlock.startLoop(new BlockStructure.IPositionCall() {
                @Override
                public void forPos(int length, int width, int height) {
                    if (!atLocation(getTranslated(x, y, z, side, length, width, height), world).equals(multiBlock.getStructure()[length][width][height])){
                        throw new RuntimeException("INVALID");
                    }
                }
            });
        } catch (RuntimeException e){
            System.out.println("INVALID");
            if (e.getMessage().equals("INVALID"))
                return false;
            throw new RuntimeException(e);
        }
        System.out.println("VALID STUFF HERE, YAYZZZ :D");
        return true;
    }

    private BlockLoc getTranslated(int x, int y, int z, ForgeDirection side, int length, int width, int height){
        int newX;
        int newY;
        int newZ;
        if (side == ForgeDirection.NORTH){
            newX = x - length;
            newZ = z + width;
        } else if (side == ForgeDirection.EAST){
            newX = x - width;
            newZ = z - length;
        } else if (side == ForgeDirection.SOUTH){
            newX = x + length;
            newZ = z - width;
        } else if (side == ForgeDirection.WEST){
            newX = x + width;
            newZ = z + length;
        } else throw new IllegalArgumentException("Cannot process side: "+side);
        newY = y + height;
        //newZ--; //Fix for that weird stuff above
        return new BlockLoc(newX, newY, newZ);
    }

    private BlockData atLocation(BlockLoc loc, IBlockAccess world){
        return atLocation(world, loc.xCoord, loc.yCoord, loc.zCoord);
    }

    private BlockData atLocation(IBlockAccess world, int x, int y, int z){
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (block == null)
            return null;
        return new BlockData(block, meta);
    }

}
