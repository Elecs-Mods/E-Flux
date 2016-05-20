package elec332.eflux.client.render.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.model.IModelAndTextureLoader;
import elec332.core.client.model.model.IQuadProvider;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.WorldHelper;
import elec332.eflux.blocks.BlockMachineFrame;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.multiblock.AbstractTileEntityMultiBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 18-3-2016.
 */
public class MachineFrameQuadProvider implements IQuadProvider, IModelAndTextureLoader {

    public MachineFrameQuadProvider(){
        RenderingRegistry.instance().registerLoader(this);
    }

    @Override
    public List<BakedQuad> getBakedQuads(@Nullable IBlockState state, EnumFacing side, long random) {
        if (side == null){
            return ImmutableList.of();
        }
        boolean one = false;
        Map<EnumFacing, Boolean> cache = Maps.newHashMap();
        if (state != null) {
            World world = Minecraft.getMinecraft().theWorld;
            BlockPos pos = new BlockPos(((IExtendedBlockState)state).getValue(BlockMachineFrame.FRAME_POS_PROPERTY));
            for (EnumFacing facing : EnumFacing.VALUES) {
                BlockPos offset = pos.offset(facing);
                IBlockState state1 = WorldHelper.getBlockState(world, offset);
                AbstractTileEntityMultiBlock tile1, tile2;
                boolean b = false;
                if (state1.getBlock() == state.getBlock() && WorldHelper.getBlockMeta(state) == WorldHelper.getBlockMeta(state1)) {
                    tile2 = (AbstractTileEntityMultiBlock) WorldHelper.getTileAt(world, offset);
                    tile1 = (AbstractTileEntityMultiBlock) WorldHelper.getTileAt(world, pos);
                /*if (tile1 == null || tile2 == null){
                    System.out.println((tile1==null)+"   "+(tile2==null));
                    Minecraft.getMinecraft().theWorld.markBlockRangeForRenderUpdate(pos, pos);
                } else*/
                    if (tile1.getMultiBlock() != null && tile1.getMultiBlock() == tile2.getMultiBlock()) {
                        one = b = true;
                    }
                }
                cache.put(facing, b);
            }
        }
        if (!one){
            return BlockMachineFrame.itemModel.getQuads(state, side, random);
        } else {
            return renderFacing(side, cache);
        }
    }

    private TextureAtlasSprite main;
    private TextureAtlasSprite[] lines;
    private ElecQuadBakery quadBakery;
    private ElecTemplateBakery templateBakery;

    private List<BakedQuad> renderFacing(EnumFacing facing, Map<EnumFacing, Boolean> map){
        ImmutableList.Builder<BakedQuad> quadBuilder = new ImmutableList.Builder<BakedQuad>();
        //renderBlocks.render(facing, pos, main);
        quadBuilder.add(bakeQuad(facing, main));
        switch (facing){
            case UP:
            case DOWN:
                for (int i = 0; i < udF.length; i++) {
                    EnumFacing facing1 = udF[i];
                    if (!map.get(facing1)){
                        quadBuilder.add(bakeQuad(facing, lines[i]));
                    }
                }
                break;
            case NORTH:
            case SOUTH:
                for (int i = 0; i < nsF.length; i++) {
                    EnumFacing facing1 = facing == EnumFacing.SOUTH && nsF[i].getAxis() != EnumFacing.Axis.Y ? nsF[i].getOpposite() : nsF[i];
                    if (!map.get(facing1)){
                        quadBuilder.add(bakeQuad(facing, lines[i]));
                    }
                }
                break;
            case EAST:
            case WEST:
                for (int i = 0; i < ewF.length; i++) {
                    EnumFacing facing1 = facing == EnumFacing.WEST && ewF[i].getAxis() != EnumFacing.Axis.Y ? ewF[i].getOpposite() : ewF[i];
                    if (!map.get(facing1)){
                        quadBuilder.add(bakeQuad(facing, lines[i]));
                    }
                }
                break;
        }
        return quadBuilder.build();
    }

    private BakedQuad bakeQuad(EnumFacing side, TextureAtlasSprite texture){
        return quadBakery.bakeQuad(templateBakery.templateQuadForTexture(side, texture));
    }

    private final EnumFacing[] udF = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
    private final EnumFacing[] nsF = new EnumFacing[]{EnumFacing.UP, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.EAST};
    private final EnumFacing[] ewF = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH};

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    public void registerTextures(IIconRegistrar iconRegistrar) {
        main = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/frame/full"));
        lines = new TextureAtlasSprite[4];
        lines[0] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/frame/top"));
        lines[1] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/frame/right"));
        lines[2] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/frame/down"));
        lines[3] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/frame/left"));
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        this.quadBakery = quadBakery;
        this.templateBakery = templateBakery;
    }
}
