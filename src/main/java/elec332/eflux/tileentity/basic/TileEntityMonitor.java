package elec332.eflux.tileentity.basic;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.core.world.location.BlockStateWrapper;
import elec332.eflux.init.BlockRegister;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Elec332 on 14-1-2016.
 */
@RegisterTile(name = "TileEntityEFluxMonitor")
public class TileEntityMonitor extends TileEntityBlockMachine {

    private int monitorSide;

    public void pokeCheckStuff() {
        if (getBlockType() != BlockRegister.monitor.block || getBlockMetadata() != BlockRegister.monitor.meta)
            return;
        boolean neighbour = false;
        for (EnumFacing direction : EnumFacing.VALUES) {
            if (direction == EnumFacing.UP || direction == EnumFacing.DOWN)
                continue;
            BlockPos toCheck = pos.offset(direction);
            Block block = WorldHelper.getBlockAt(worldObj, toCheck);
            int meta = WorldHelper.getBlockMeta(worldObj, toCheck);
            if (new BlockStateWrapper(block, meta).equals(BlockRegister.monitor)) {
                neighbour = true;
                TileEntityMonitor tile = (TileEntityMonitor) WorldHelper.getTileAt(worldObj, toCheck);
                if (monitorSide == 0 && tile.getTileFacing() == getTileFacing()){
                    if (DirectionHelper.rotateLeft(getTileFacing()) == direction){
                        monitorSide = 1;
                        tile.onNeighborBlockChange(getBlockType());
                        if (tile.monitorSide == monitorSide)
                            monitorSide = 0;
                        reRenderBlock();
                    } else if (DirectionHelper.rotateRight(getTileFacing()) == direction){
                        monitorSide = 2;
                        tile.onNeighborBlockChange(getBlockType());
                        if (tile.monitorSide == monitorSide)
                            monitorSide = 0;
                        reRenderBlock();
                    }
                }
            }
        }
        if (!neighbour){
            monitorSide = 0;
            reRenderBlock();
        }
    }

    public int getMonitorSide() {
        return monitorSide;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("monS", monitorSide);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.monitorSide = tagCompound.getInteger("monS");
    }

}
