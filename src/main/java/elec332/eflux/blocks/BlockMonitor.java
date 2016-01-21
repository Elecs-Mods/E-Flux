package elec332.eflux.blocks;

import elec332.core.api.wrench.IWrenchable;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.map.BakedModelMetaRotationMap;
import elec332.core.client.model.map.IBakedModelMetaRotationMap;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.tile.TileBase;
import elec332.core.util.BlockStateHelper;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.basic.TileEntityMonitor;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 14-1-2016.
 */
public class BlockMonitor extends Block implements IWrenchable, INoJsonBlock, ITileEntityProvider {

    public BlockMonitor() {
        super(Material.rock);
        setUnlocalizedName(EFlux.ModID+"monitor");
        setResistance(5.0f);
        setHardness(2.5f);
        setDefaultState(BlockStateHelper.FACING_NORMAL.setDefaultMetaState(this));
    }

    @SideOnly(Side.CLIENT)
    private IBakedModelMetaRotationMap<IBlockModel> rotationMap;
    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite normal, monitorF, monitorR, monitorL;

    public BlockMonitor register(){
        GameRegistry.registerBlock(this, "monitor");
        return this;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileBase)
            return ((TileBase) tile).onBlockActivated(player, side, hitX, hitY, hitZ);
        return super.onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
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
    public ItemStack ItemDropped(World world, BlockPos pos) {
        return new ItemStack(this, 1, WorldHelper.getBlockMeta(world, pos));
    }

    @Override
    public void onWrenched(World world, BlockPos pos, EnumFacing forgeDirection) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileBase)
            ((TileBase) tile).onWrenched(forgeDirection);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        pokeCheck(worldIn, pos);
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
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
    public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityMonitor) {
            ((TileEntityMonitor) tile).pokeCheckStuff();
        } else if (tile instanceof TileBase) {
            ((TileBase) tile).onNeighborBlockChange(block);
        } else {
            super.onNeighborBlockChange(world, pos, state, block);
        }
    }

    /**
     * This method is used when a model is requested to render the block in a world.
     *
     * @param state The current BlockState.
     * @param iba   The IBlockAccess the block is in.
     * @param pos   The position of the block.
     * @return The model to render for this block for the given arguments.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockModel getBlockModel(IBlockState state, IBlockAccess iba, BlockPos pos) {
        TileEntityMonitor tile = (TileEntityMonitor)WorldHelper.getTileAt(iba, pos);
        int meta = 0;
        //if (tile != null){
            meta = tile.getMonitorSide();
        //} else {
         //   Minecraft.getMinecraft().theWorld.markBlockRangeForRenderUpdate(pos, pos);
        //}

        return rotationMap.forMetaAndRotation(meta, DirectionHelper.getRotationFromFacing(tile.getTileFacing()));
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(Item item, int meta) {
        return rotationMap.forMeta(meta);
    }


    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     *
     * @param quadBakery     The QuadBakery.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(final ElecQuadBakery quadBakery, ElecModelBakery modelBakery, final ElecTemplateBakery templateBakery) {
        rotationMap = new BakedModelMetaRotationMap<IBlockModel>();
        rotationMap.setModelsForRotation(0, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(monitorF))));
        rotationMap.setModelsForRotation(1, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(monitorR))));
        rotationMap.setModelsForRotation(2, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(forSpecialFront(monitorL))));
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        normal = iconRegistrar.registerSprite(getTextureLocation("normal"));
        monitorF = iconRegistrar.registerSprite(getTextureLocation("monitorFull"));
        monitorR = iconRegistrar.registerSprite(getTextureLocation("monitorRightSide"));
        monitorL = iconRegistrar.registerSprite(getTextureLocation("monitorLeftSide"));
    }

    @SideOnly(Side.CLIENT)
    protected ResourceLocation getTextureLocation(String s){
        return new EFluxResourceLocation("blocks/"+s);
    }

    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite[] forSpecialFront(TextureAtlasSprite front){
        return new TextureAtlasSprite[]{
                normal, normal, front, normal, normal, normal
        };
    }

    private void pokeCheck(IBlockAccess world, BlockPos pos){
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityMonitor)
            ((TileEntityMonitor) tile).pokeCheckStuff();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMonitor();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return BlockStateHelper.FACING_NORMAL.getStateForMeta(this, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return BlockStateHelper.FACING_NORMAL.getMetaForState(state);
    }

    @Override
    protected BlockState createBlockState() {
        return BlockStateHelper.FACING_NORMAL.createMetaBlockState(this);
    }

}
