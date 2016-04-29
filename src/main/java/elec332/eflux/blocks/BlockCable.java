package elec332.eflux.blocks;

import com.google.common.collect.Lists;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.RenderHelper;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.core.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.render.CableRenderer;
import elec332.eflux.tileentity.energy.cable.AbstractCable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class BlockCable extends BlockWithMeta implements ITileEntityProvider, INoJsonBlock {

    public BlockCable(String blockName) {
        super(Material.CLOTH, blockName, EFlux.ModID.toLowerCase());
        setCreativeTab(EFlux.creativeTab);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;
    @SideOnly(Side.CLIENT)
    private IBakedModelMetaMap<IBakedModel> models;

    @Override
    public BlockWithMeta register() {
        super.register();
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
            registerISBHR();
        }
        return this;
    }

    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getTextureFor(IBlockState state){
        return textures[getMetaFromState(state)];
    }

    //@Override
    //public int getRenderType() {
    //    return RenderingRegistry.SPECIAL_BLOCK_RENDERER_ID;
    //}

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        /*switch (metadata){
            case 0:
                return new BasicCable();
            case 1:
                return new NormalCable();
            case 2:
                return new AdvancedCable();
            default:
                return null;
        }*/
        return null;
    }

    /*@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        setBlockBoundsBasedOnState(state, worldIn, pos); //setBlockBoundsBasedOnState
        return super.getCollisionBoundingBox(state, worldIn, pos);
    }*/

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return super.getSelectedBoundingBox(state, worldIn, pos);
    }

    @Override
    @SuppressWarnings("all") //setBlockBoundsBasedOnState
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess iba, BlockPos pos) {
        List<EnumFacing> connections = Lists.newArrayList();
        TileEntity tile = WorldHelper.getTileAt(iba, pos);
        if (tile != null) {
            for (EnumFacing direction : EnumFacing.VALUES) {
                TileEntity tile2 = WorldHelper.getTileAt(iba, pos.offset(direction));
                if ((tile2 instanceof AbstractCable && ((AbstractCable) tile2).getUniqueIdentifier().equals(((AbstractCable) tile).getUniqueIdentifier())) || EnergyAPIHelper.isProvider(tile, direction.getOpposite()) || EnergyAPIHelper.isReceiver(tile, direction.getOpposite())){
                    connections.add(direction);
                }
            }
        }
        float thickness = 6 * RenderHelper.renderUnit;
        float heightStuff = (1 - thickness)/2;
        float f1 = thickness + heightStuff;

        float yMin = connections.contains(EnumFacing.DOWN) ? 0 : heightStuff;
        float yMax = connections.contains(EnumFacing.UP) ? 1 : f1;
        float xMin = connections.contains(EnumFacing.WEST) ? 0 : heightStuff;
        float xMax = connections.contains(EnumFacing.EAST) ? 1 : f1;
        float zMin = connections.contains(EnumFacing.NORTH) ? 0 : heightStuff;
        float zMax = connections.contains(EnumFacing.SOUTH) ? 1 : f1;

        return new AxisAlignedBB(xMin, yMin, zMin, xMax, yMax, zMax);
    }

    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
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
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName()+"."+stack.getItemDamage();
    }

    @Override
    public int getTypes() {
        return 3;
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[getTypes()];
        textures[0] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/basicCable"));
        textures[1] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/normalCable"));
        textures[2] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/advancedCable"));
    }

    /**
     * This method is used when a model is requested for every valid BlockState,
     * during the initialisation of the ModelRegistry.
     *
     * @param state The current BlockState, can NOT be an ExtendedBlockState.
     * @return The model to render for this block for the given arguments.
     */
    @Override
    public IBakedModel getBlockModel(IBlockState state) {
        return models.forMeta(getMetaFromState(state));
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return models.forMeta(stack.getItemDamage());
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        models = new BakedModelMetaMap<IBakedModel>();
        for (int i = 0; i < textures.length; i++) {
            models.setModelForMeta(i, modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures[i]).setTexture(textures[i])));
        }
    }

    @SideOnly(Side.CLIENT)
    private void registerISBHR(){
        RenderingRegistry.instance().registerRenderer(this, new CableRenderer());
    }

}
