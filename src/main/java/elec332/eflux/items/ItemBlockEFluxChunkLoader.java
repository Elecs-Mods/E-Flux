package elec332.eflux.items;

import elec332.core.item.AbstractItemBlock;
import elec332.core.util.PlayerHelper;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntityMainChunkLoader;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntitySubChunkLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 10-1-2016.
 */
public class ItemBlockEFluxChunkLoader extends AbstractItemBlock {

    public ItemBlockEFluxChunkLoader(Block block) {
        super(block);
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side, EntityPlayer player, ItemStack stack) {
        BlockMachine block = (BlockMachine) getBlock();
        Class<? extends TileEntity> tile = block.getMachine().getTileClass();
        boolean canSuper = super.canPlaceBlockOnSide(worldIn, pos, side, player, stack);
        if (tile == TileEntityMainChunkLoader.class) {
            return !ChunkLoaderPlayerProperties.get(PlayerHelper.getPlayerUUID(player)).hasHandler() && canSuper;
        } else if (tile == TileEntitySubChunkLoader.class){
            return canSuper; /* To make future editing easier */
        } else {
            throw new IllegalStateException();
        }
    }

}
