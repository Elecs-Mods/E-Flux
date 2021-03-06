package elec332.eflux.blocks;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.api.client.model.model.IQuadProvider;
import elec332.core.client.model.loading.INoJsonBlock;
import elec332.core.util.IDefaultStringSerializable;
import elec332.core.util.UniversalUnlistedProperty;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.render.model.MachineFrameQuadProvider;
import elec332.eflux.tileentity.basic.TileEntityMultiBlockMachinePart;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockMachineFrame extends BlockWithMeta<BlockMachineFrame.EnumMachineFrameType> implements ITileEntityProvider, INoJsonBlock {

    public BlockMachineFrame(String name) {
        super(Material.ROCK, name, EFlux.ModID.toLowerCase());
    }

    public static final IUnlistedProperty<BlockPos> FRAME_POS_PROPERTY = new UniversalUnlistedProperty<>("position", BlockPos.class);

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    public static IBakedModel model, itemModel;
    @SideOnly(Side.CLIENT)
    private static IQuadProvider quadProvider;

    @Override
    public BlockMachineFrame register() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
            registerClient();
        }
        super.register();
        return this;
    }

    @Override
    public Class<EnumMachineFrameType> getEnumClass() {
        return EnumMachineFrameType.class;
    }

    @Override
    public IUnlistedProperty[] getUnlistedProperties() {
        return new IUnlistedProperty[]{FRAME_POS_PROPERTY};
    }

    @Override
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * This method is used when a model is requested to render the block in a world.
     *
     * @param state The current BlockState.
     * @return The model to render for this block for the given arguments.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(IBlockState state) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        System.out.println("registermodels");
        itemModel = modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(texture));
        model = modelBakery.forQuadProvider(templateBakery.newDefaultBlockTemplate(), quadProvider);
    }

    @SideOnly(Side.CLIENT)
    private void registerClient(){
        quadProvider = new MachineFrameQuadProvider();
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        System.out.println("registertextures");
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/default_side"));
    }

    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        return ((IExtendedBlockState)state).withProperty(FRAME_POS_PROPERTY, pos);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new TileEntityMultiBlockMachinePart();
    }

    public enum EnumMachineFrameType implements IDefaultStringSerializable {

        BASIC,
        NORMAL,
        ADVANCED

    }

}
