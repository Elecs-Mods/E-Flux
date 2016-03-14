package elec332.eflux.items;

import elec332.core.util.PlayerHelper;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 10-1-2016.
 */
public class ChunkLoaderItemBlock extends ItemBlock {

    public ChunkLoaderItemBlock(Block block) {
        super(block);
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        BlockMachine block = (BlockMachine) getBlock();
        Class<? extends TileEntity> tile = block.getMachine().getTileClass();
        boolean canSuper = super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
        if (tile == MainChunkLoaderTile.class) {
            return !ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID(player)).hasHandler() && canSuper;
        } else if (tile == ChunkLoaderSubTile.class){
            return canSuper; /* To make future editing easier */
        } else {
            throw new IllegalStateException();
        }
    }

}
