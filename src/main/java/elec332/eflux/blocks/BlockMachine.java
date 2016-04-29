package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.tile.BlockTileBase;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.BlockStateHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.render.BlockMachineQuadProvider;
import elec332.eflux.util.UniversalUnlistedProperty;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 30-4-2015.
 */
public class BlockMachine extends BlockTileBase implements INoJsonBlock {

    public BlockMachine(IEFluxBlockMachineData machine){
        super(machine.getBlockMaterial(), machine.getTileClass(), machine.getName(), EFlux.ModID.toLowerCase());
        setCreativeTab(EFlux.creativeTab);
        setDefaultState(BlockStateHelper.FACING_NORMAL.setDefaultMetaState(this));
        this.machine = machine;
        this.layer = machine.getRenderingLayer();
    }

    public static final IUnlistedProperty<Boolean> ACTIVATED_PROPERTY;

    private final IEFluxBlockMachineData machine;
    private final BlockRenderLayer layer;

    public IEFluxBlockMachineData getMachine(){
        return this.machine;
    }

    @Override
    public BlockMachine register() {
        GameRegistry.register(this);
        Class<? extends ItemBlock> itemBlockClass = machine.getItemBlockClass();
        try {
            GameRegistry.register(itemBlockClass.getConstructor(Block.class).newInstance(this), getRegistryName());
        } catch (Exception e){
            throw new RuntimeException("Error registering ItemBlock: "+itemBlockClass.getCanonicalName());
        }
        return this;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return machine.getRenderType();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return layer == BlockRenderLayer.SOLID;
    }

    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite[][] textures;
    @SideOnly(Side.CLIENT)
    private IBakedModel model;

    /**
     * This method is used when a model is requested to render the block in a world.
     *
     * @param state The current BlockState.
     * @return The model to render for this block for the given arguments.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(IBlockState state) {
        /*TileEntity tile = WorldHelper.getTileAt(iba, pos);
        int meta = (machine.hasTwoStates() && tile instanceof IActivatableMachine && ((IActivatableMachine) tile).isActive()) ? 1 : 0;
        return rotationMap.forMetaAndRotation(meta, DirectionHelper.getRotationFromFacing(getFacing(state)));*/
        return model;
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     *
     * @param quadBakery     The QuadBakery.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.forQuadProvider(templateBakery.newDefaultBlockTemplate(), new BlockMachineQuadProvider(textures, quadBakery)/*modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(textures[i]))*/);
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        int t = machine.hasTwoStates() ? 2 : 1;
        textures = new TextureAtlasSprite[t][6];
        for (int i = 0; i < t; i++) {
            for (EnumFacing facing : EnumFacing.VALUES){
                String s = machine.getTextureProvider().getIconName(facing, i == 1);
                textures[i][facing.ordinal()] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/"+s));
            }
        }
    }

    public static EnumFacing getFacing(IBlockAccess iba, BlockPos pos){
        return getFacing(WorldHelper.getBlockState(iba, pos));
    }

    public static EnumFacing getFacing(IBlockState state){
        return state.getValue(BlockStateHelper.FACING_NORMAL.getProperty());
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return layer;
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
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{BlockStateHelper.FACING_NORMAL.getProperty()}, new IUnlistedProperty[]{ACTIVATED_PROPERTY});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        return ((IExtendedBlockState)state).withProperty(ACTIVATED_PROPERTY, machine.hasTwoStates() && tile instanceof IActivatableMachine && ((IActivatableMachine) tile).isActive());
    }

    static {
        ACTIVATED_PROPERTY = new UniversalUnlistedProperty<Boolean>("activated", Boolean.class);
    }

}
