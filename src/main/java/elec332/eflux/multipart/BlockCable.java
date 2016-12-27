package elec332.eflux.multipart;

import elec332.core.tile.BlockTileBase;
import elec332.core.world.WorldHelper;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static elec332.eflux.multipart.TileEntityCable.*;

/**
 * Created by Elec332 on 17-12-2016.
 */
public class BlockCable extends BlockTileBase {

    public BlockCable() {
        super(Material.GLASS, TileEntityCable.class, new EFluxResourceLocation("cable"));
        setHardness(1.3f);
    }

    public static final PropertyEnum<EnumCableType> TYPE = PropertyEnum.create("cabletype", EnumCableType.class);

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{
                TYPE
        }, new IUnlistedProperty[]{
                DOWN, UP, NORTH, SOUTH, WEST, EAST
        });
    }

    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState ibs, IBlockAccess world, BlockPos pos) {
        return ((TileEntityCable) WorldHelper.getTileAt(world, pos)).getExtendedState((IExtendedBlockState) ibs);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMeta();
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getStateFromMeta(meta);
    }

    @Override
    @SuppressWarnings("all")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, EnumCableType.values()[meta]);
    }

    @Override
    protected TileEntity createTile(Class<? extends TileEntity> clazz, @Nonnull World world, int metadata) throws Exception {
        return new TileEntityCable(EnumCableType.values()[metadata]);
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return ((TileEntityCable) WorldHelper.getTileAt(world, pos)).getPartStack();
    }

    @Override
    @SuppressWarnings("all")
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        ((TileEntityCable) WorldHelper.getTileAt(world, pos)).addCollisionBoxes(entityBox, collidingBoxes, entityIn);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

}
