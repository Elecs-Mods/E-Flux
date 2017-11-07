package elec332.eflux.blocks;

import elec332.core.api.wrench.IWrenchable;
import elec332.core.client.model.loading.INoBlockStateJsonBlock;
import elec332.core.tile.AbstractBlock;
import elec332.core.tile.TileBase;
import elec332.core.util.BlockStateHelper;
import elec332.core.util.DirectionHelper;
import elec332.core.util.RegistryHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.tileentity.basic.TileEntityMonitor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 14-1-2016.
 */
public class BlockMonitor extends AbstractBlock implements IWrenchable, ITileEntityProvider, INoBlockStateJsonBlock.RotationImpl {

    public BlockMonitor() {
        super(Material.ROCK);
        setUnlocalizedName(EFlux.ModID+"monitor");
        setRegistryName(EFlux.ModID.toLowerCase(), "monitor");
        setResistance(5.0f);
        setHardness(2.5f);
        setDefaultState(BlockStateHelper.FACING_NORMAL.setDefaultMetaState(this));
    }
/*
    @SideOnly(Side.CLIENT)
    private IBakedModelMetaRotationMap<IBakedModel> rotationMap;
    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite normal, monitorF, monitorR, monitorL;&*/

    public BlockMonitor register(){
        RegistryHelper.register(this);
        RegistryHelper.register(new ItemBlock(this).setRegistryName(getRegistryName()));
        return this;
    }

    @Override
    public boolean onBlockActivatedC(World world, BlockPos pos, EntityPlayer player, EnumHand hand, IBlockState state, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileBase)
            return ((TileBase) tile).onBlockActivated(state, player, hand, facing, hitX, hitY, hitZ);
        return super.onBlockActivatedC(world, pos, player, hand, state, facing, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack stack) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityMonitor) {
            ((TileEntityMonitor) tile).onBlockPlacedBy(entityLiving, stack);
        } else {
            super.onBlockPlacedBy(world, pos, state, entityLiving, stack);
        }
    }

    @Override
    public ItemStack itemDropped(World world, BlockPos pos) {
        return new ItemStack(this, 1, WorldHelper.getBlockMeta(world, pos));
    }

    @Override
    public boolean onWrenched(World world, BlockPos pos, EnumFacing forgeDirection) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        return tile instanceof TileBase && ((TileBase) tile).onWrenched(forgeDirection);
    }

    @Override
    @Nonnull
    public IBlockState getBlockStateForPlacementC(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        pokeCheck(world, pos);
        return getDefaultState().withProperty(BlockStateHelper.FACING_NORMAL.getProperty(), DirectionHelper.getFacingOnPlacement(placer));
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        pokeCheck(worldIn, pos);
        super.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        pokeCheck(worldIn, pos);
        super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    @Override
    public void neighborChangedC(World world, BlockPos pos, IBlockState state, Block neighbor, BlockPos p_189540_5_) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityMonitor) {
            ((TileEntityMonitor) tile).pokeCheckStuff();
        } else if (tile instanceof TileBase) {
            ((TileBase) tile).onNeighborBlockChange(neighbor);
        } else {
            super.neighborChangedC(world, pos, state, neighbor, p_189540_5_);
        }
    }

    private void pokeCheck(IBlockAccess world, BlockPos pos){
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityMonitor)
            ((TileEntityMonitor) tile).pokeCheckStuff();
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityMonitor();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return BlockStateHelper.FACING_NORMAL.getStateForMeta(this, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return BlockStateHelper.FACING_NORMAL.getMetaForState(state);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return BlockStateHelper.FACING_NORMAL.createMetaBlockState(this);
    }

}
