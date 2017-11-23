package elec332.eflux.blocks;

import elec332.core.tile.BlockTileBase;
import elec332.eflux.tileentity.TileWireHub;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 16-11-2017.
 */
public class BlockWireHub extends BlockTileBase implements IMultipart {

	public BlockWireHub(ResourceLocation name) {
		super(Material.IRON, TileWireHub.class, name);
	}


	@Override
	@SuppressWarnings("all")
	public IBlockState getBlockStateForPlacementC(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @Nullable EnumHand hand) {
		return getStateFromMeta(meta);
	}

	@Override
	public IPartSlot getSlotForPlacement(World world, BlockPos pos, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ, EntityLivingBase placer) {
		return EnumCenterSlot.CENTER;
	}

	@Override
	public IPartSlot getSlotFromWorld(IBlockAccess world, BlockPos pos, IBlockState state) {
		return EnumCenterSlot.CENTER;
	}

}
