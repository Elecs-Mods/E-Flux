package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.render.MachineFrameRenderer;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockMachineFrame extends BlockWithMeta implements INoJsonBlock, ITileEntityProvider {

    public BlockMachineFrame(String name) {
        super(Material.rock, name, EFlux.ModID);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    public static IBlockModel model;

    @Override
    public BlockMachineFrame register() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
            registerClient();
        }
        super.register();
        return this;
    }

    @Override
    public int getTypes() {
        return 3;
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public int getRenderType() {
        return RenderingRegistry.SPECIAL_BLOCK_RENDERER_ID;
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
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(Item item, int meta) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(texture));
    }

    @SideOnly(Side.CLIENT)
    private void registerClient(){
        RenderingRegistry.instance().registerRenderer(this, new MachineFrameRenderer(model));
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityBlockMachine();
    }

}
