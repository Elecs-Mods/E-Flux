package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.map.BakedModelMetaRotationMap;
import elec332.core.client.model.map.IBakedModelMetaRotationMap;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.tile.BlockTileBase;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.BlockStateHelper;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 30-4-2015.
 */
public class BlockMachine extends BlockTileBase implements INoJsonBlock {

    public BlockMachine(IEFluxBlockMachineData machine){
        super(machine.getBlockMaterial(), machine.getTileClass(), machine.getName(), EFlux.ModID);
        setCreativeTab(EFlux.creativeTab);
        setDefaultState(BlockStateHelper.FACING_NORMAL.setDefaultMetaState(this));
        this.machine = machine;
        this.layer = machine.getRenderingLayer();
    }

    private final IEFluxBlockMachineData machine;
    private final EnumWorldBlockLayer layer;

    public IEFluxBlockMachineData getMachine(){
        return this.machine;
    }

    @Override
    public int getRenderType() {
        return machine.getRenderID();
    }

    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite[][] textures;
    @SideOnly(Side.CLIENT)
    protected IBakedModelMetaRotationMap<IBlockModel> rotationMap;

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
        TileEntity tile = WorldHelper.getTileAt(iba, pos);
        int meta = (machine.hasTwoStates() && tile instanceof IActivatableMachine && ((IActivatableMachine) tile).isActive()) ? 1 : 0;
        return rotationMap.forMetaAndRotation(meta, DirectionHelper.getRotationFromFacing(getFacing(state)));
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(Item item, int meta) {
        return rotationMap.get();
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
        rotationMap = new BakedModelMetaRotationMap<IBlockModel>();
        int t = machine.hasTwoStates() ? 2 : 1;
        for (int i = 0; i < t; i++) {
            rotationMap.setModelsForRotation(i, modelBakery.forTemplateRotation(templateBakery.newDefaultBlockTemplate(textures[i])));
        }
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
    public EnumWorldBlockLayer getBlockLayer() {
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
    protected BlockState createBlockState() {
        return BlockStateHelper.FACING_NORMAL.createMetaBlockState(this);
    }

}
