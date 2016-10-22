package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.model.IQuadProvider;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.util.UniversalUnlistedProperty;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.render.model.MachineFrameQuadProvider;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
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
public class BlockMachineFrame extends BlockWithMeta implements INoJsonBlock, ITileEntityProvider {

    public BlockMachineFrame(String name) {
        super(Material.ROCK, name, EFlux.ModID.toLowerCase());
    }

    public static final IUnlistedProperty<BlockPos> FRAME_POS_PROPERTY = new UniversalUnlistedProperty<BlockPos>("position", BlockPos.class);

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
    public IUnlistedProperty[] getUnlistedProperties() {
        return new IUnlistedProperty[]{FRAME_POS_PROPERTY};
    }

    @Override
    public int getTypes() {
        return 3;
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
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
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
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/default_side"));
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return ((IExtendedBlockState)state).withProperty(FRAME_POS_PROPERTY, pos);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockMachine();
    }

}
