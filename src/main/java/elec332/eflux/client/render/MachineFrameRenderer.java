package elec332.eflux.client.render;

import com.google.common.collect.Maps;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.ITessellator;
import elec332.core.client.ITextureLoader;
import elec332.core.client.RenderBlocks;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.render.AbstractBlockRenderer;
import elec332.core.world.WorldHelper;
import elec332.eflux.blocks.BlockMachineFrame;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.multiblock.TileMultiBlockTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * Created by Elec332 on 13-1-2016.
 */
@SideOnly(Side.CLIENT)
public class MachineFrameRenderer extends AbstractBlockRenderer implements ITextureLoader {

    public MachineFrameRenderer(IBlockModel model){
        RenderingRegistry.instance().registerTextureLoader(this);
        this.defaultModel = BlockMachineFrame.model;
    }

    private IBlockModel defaultModel;
    private TextureAtlasSprite main;
    private TextureAtlasSprite[] lines;

    @Override
    public void renderBlock(IBlockAccess iba, IBlockState state, BlockPos pos, RenderBlocks renderBlocks, ITessellator tessellator, WorldRenderer renderer) {
        boolean one = false;
        iba = Minecraft.getMinecraft().theWorld;
        Map<EnumFacing, Boolean> cache = Maps.newHashMap();
        for (EnumFacing facing : EnumFacing.VALUES){
            BlockPos offset = pos.offset(facing);
            IBlockState state1 = WorldHelper.getBlockState(iba, offset);
            TileMultiBlockTile tile1, tile2;
            boolean b = false;
            if (state1.getBlock() == state.getBlock() && WorldHelper.getBlockMeta(state) == WorldHelper.getBlockMeta(state1)){
                tile2 = (TileMultiBlockTile) WorldHelper.getTileAt(iba, offset);
                tile1 = (TileMultiBlockTile) WorldHelper.getTileAt(iba, pos);
                /*if (tile1 == null || tile2 == null){
                    System.out.println((tile1==null)+"   "+(tile2==null));
                    Minecraft.getMinecraft().theWorld.markBlockRangeForRenderUpdate(pos, pos);
                } else*/ if (tile1.getMultiBlock() != null && tile1.getMultiBlock() == tile2.getMultiBlock()){
                    one = b = true;
                }
            }
            cache.put(facing, b);
        }
        if (!one){
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(iba, BlockMachineFrame.model, state, pos, renderer);
        } else {
            for (EnumFacing facing : EnumFacing.VALUES){
                BlockPos offSet = pos.offset(facing);
                if (state.getBlock().shouldSideBeRendered(iba, offSet, facing)){
                    tessellator.setBrightness(state.getBlock().getMixedBrightnessForBlock(iba, pos.offset(facing)) - 2);
                    renderFacing(facing, cache, renderBlocks, pos);
                }
            }
        }
    }

    private void renderFacing(EnumFacing facing, Map<EnumFacing, Boolean> map, RenderBlocks renderBlocks, BlockPos pos){
        renderBlocks.render(facing, pos, main);
        switch (facing){
            case UP:
            case DOWN:
                for (int i = 0; i < udF.length; i++) {
                    EnumFacing facing1 = udF[i];
                    if (!map.get(facing1)){
                        renderBlocks.render(facing, pos, lines[i]);
                    }
                }
                return;
            case NORTH:
            case SOUTH:
                for (int i = 0; i < nsF.length; i++) {
                    EnumFacing facing1 = facing == EnumFacing.SOUTH && nsF[i].getAxis() != EnumFacing.Axis.Y ? nsF[i].getOpposite() : nsF[i];
                    if (!map.get(facing1)){
                        renderBlocks.render(facing, pos, lines[i]);
                    }
                }
                return;
            case EAST:
            case WEST:
                for (int i = 0; i < ewF.length; i++) {
                    EnumFacing facing1 = facing == EnumFacing.WEST && ewF[i].getAxis() != EnumFacing.Axis.Y ? ewF[i].getOpposite() : ewF[i];
                    if (!map.get(facing1)){
                        renderBlocks.render(facing, pos, lines[i]);
                    }
                }
                return;
        }
    }

    private final EnumFacing[] udF = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
    private final EnumFacing[] nsF = new EnumFacing[]{EnumFacing.UP, EnumFacing.WEST, EnumFacing.DOWN, EnumFacing.EAST};
    private final EnumFacing[] ewF = new EnumFacing[]{EnumFacing.UP, EnumFacing.NORTH, EnumFacing.DOWN, EnumFacing.SOUTH};

    @Override
    public boolean shouldRenderBlock(IBlockAccess iba, IBlockState state, BlockPos pos) {
        return true;
    }

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
}
