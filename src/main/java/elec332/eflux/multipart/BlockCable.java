package elec332.eflux.multipart;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.client.model.loading.INoJsonBlock;
import elec332.core.tile.BlockTileBase;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.proxies.ClientProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static elec332.eflux.multipart.TileEntityCable.*;

/**
 * Created by Elec332 on 17-12-2016.
 */
public class BlockCable extends BlockTileBase implements INoJsonBlock {

    public BlockCable() {
        super(Material.GLASS, TileEntityCable.class, new EFluxResourceLocation("cable"));
        setHardness(1.3f);
    }

    public static final PropertyEnum<EnumCableType> TYPE = PropertyEnum.create("cabletype", EnumCableType.class);

    @SideOnly(Side.CLIENT)
    private IBakedModel[] models;
    
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
    public void getSubBlocksC(@Nonnull Item item, List<ItemStack> subBlocks, CreativeTabs creativeTab) {
        for (int i = 0; i < EnumCableType.values().length; i++) {
            subBlocks.add(new ItemStack(item, 1, i));
        }
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
    public IBlockState getBlockStateForPlacementC(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
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
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
    }

    //@Override
    @SuppressWarnings("all")
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn) {
        super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entityIn, false);//(TileEntityCable) WorldHelper.getTileAt(world, pos)).addCollisionBoxes(entityBox, collidingBoxes, entityIn);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return super.getBoundingBox(state, source, pos);
    }

    @Nullable
    @Override
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end) {
        return super.collisionRayTrace(blockState, worldIn, pos, start, end);
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBoxC(IBlockAccess world, BlockPos pos, IBlockState state) {
        return super.getCollisionBoundingBoxC(world, pos, state);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return super.getSelectedBoundingBox(state, worldIn, pos);
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullyOpaque(IBlockState state) {
        return false;
    }


    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        return layer == BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerTextures(IIconRegistrar iconRegistrar) {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        models = new IBakedModel[EnumCableType.values().length];
        for (int i = 0; i < models.length; i++) {
            models[i] = ((ClientProxy) EFlux.proxy).getCableModel(i);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IBakedModel getBlockModel(IBlockState state) {
        return models[getMetaFromState(state)];
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return models[stack.getMetadata()];
    }
    
}
