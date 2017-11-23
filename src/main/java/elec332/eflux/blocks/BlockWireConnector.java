package elec332.eflux.blocks;

import elec332.core.tile.BlockTileBase;
import elec332.eflux.tileentity.TileWireConnector;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class BlockWireConnector extends BlockTileBase {

	public BlockWireConnector(ResourceLocation name) {
		super(Material.GLASS, TileWireConnector.class, name);
	}

	@Override
	@SuppressWarnings("all")
	public IBlockState getBlockStateForPlacementC(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @Nullable EnumHand hand) {
		return this.getStateFromMeta(meta);
	}

}
