package elec332.eflux.blocks;

import elec332.core.tile.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Elec332 on 19-11-2017.
 */
public class BlockGroundWire extends AbstractBlock {

	public BlockGroundWire() {
		super(Material.CLOTH);
	}

	private static final double thickness = 0.2;
	private static final PropertyEnum<EnumFacing> SIDE;
	private static final AxisAlignedBB[] BBs;

	private static boolean canPlaceBlock(World worldIn, BlockPos pos, EnumFacing direction) {
		BlockPos blockpos = pos.offset(direction.getOpposite());
		IBlockState iblockstate = worldIn.getBlockState(blockpos);
		boolean flag = iblockstate.getBlockFaceShape(worldIn, blockpos, direction) == BlockFaceShape.SOLID;
		flag &= iblockstate.isSideSolid(worldIn, pos, direction);
		return flag;
	}

	@Override
	public void neighborChangedC(World world, BlockPos pos, IBlockState state, Block neighbor, BlockPos fromPos) {
		if (!canPlaceBlock(world, pos, state.getValue(SIDE))){
			this.dropBlockAsItem(world, pos, state, 0);
			world.setBlockToAir(pos);
		}
	}

	@Nonnull
	@Override
	public IBlockState getBlockStateForPlacementC(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, @Nullable EnumHand hand) {
		if (facing != null && canPlaceBlock(world, pos, facing)){
			return getDefaultState().withProperty(SIDE, facing);
		} else {
			for (EnumFacing enumfacing : EnumFacing.values()) {
				if (enumfacing != facing && canPlaceBlock(world, pos, enumfacing)) {
					return getDefaultState().withProperty(SIDE, enumfacing);
				}
			}
		}
		throw new RuntimeException();
	}

	@Override
	public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, EnumFacing side) {
		return canPlaceBlock(worldIn, pos, side);
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
		for (EnumFacing enumfacing : EnumFacing.values()) {
			if (canPlaceBlock(worldIn, pos, enumfacing)) {
				return true;
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBoxC(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, IBlockState state) {
		return NULL_AABB;
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BBs[getMetaFromState(state)];
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SIDE).ordinal();
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SIDE, EnumFacing.VALUES[meta]);
	}

	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SIDE);
	}

	static {
		SIDE = PropertyEnum.create("placedside", EnumFacing.class);
		BBs = new AxisAlignedBB[EnumFacing.VALUES.length];
		for (EnumFacing side : EnumFacing.VALUES) {
			AxisAlignedBB bb;
			switch (side){
				case UP:
					bb = new AxisAlignedBB(0, 1, 0, 1, 1 - thickness, 1);
					break;
				case DOWN:
					bb = new AxisAlignedBB(0, 0, 0, 1, thickness, 1);
					break;
				default:
					bb = FULL_BLOCK_AABB;
			}
			BBs[side.ordinal()] = bb;
		}
	}

}
