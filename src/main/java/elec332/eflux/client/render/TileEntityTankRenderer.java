package elec332.eflux.client.render;

import com.google.common.collect.Maps;
import elec332.core.client.*;
import elec332.core.world.WorldHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.util.IEFluxTank;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fluids.Fluid;

import java.util.Map;

/**
 * Created by Elec332 on 19-4-2016.
 */
public class TileEntityTankRenderer<T extends TileEntity & IEFluxTank> extends TileEntitySpecialRenderer<T> implements ITextureLoader {

    @Override
    public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage) {

        Fluid fluid = te.getClientRenderFluid();
        float height = te.getClientRenderHeight();






        float offset = -0.0002f;

        GlStateManager.pushMatrix();GlStateManager.translate(x, y, z);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderHelper.bindBlockTextures();
        ITessellator tessellator = RenderHelper.getTessellator();
        ((ElecTessellator)tessellator).startDrawingWorldBlock();
        RenderBlocks renderBlocks = RenderHelper.getBlockRenderer();
        renderBlocks.resetTessellator();
        if (!(fluid == null || height <= 0)) {
            TextureAtlasSprite fluidTexture = RenderHelper.getFluidTexture(fluid, false);

            float u1 = fluidTexture.getMinU();
            float v1 = fluidTexture.getMinV();
            float u2 = fluidTexture.getMaxU();
            float v2 = fluidTexture.getMaxV() - ((fluidTexture.getMaxV() - fluidTexture.getMinV()) * (1 - height));
            tessellator.setColorRGBA(255, 255, 255, 128);
            tessellator.setBrightness(240);

            renderBlocks.renderFaceYPos(0, height - 1, 0, fluidTexture); //Im lazy

            tessellator.addVertexWithUV(1, height, -offset, u1, v1);
            tessellator.addVertexWithUV(1, 0, -offset, u1, v2);
            tessellator.addVertexWithUV(0, 0, -offset, u2, v2);
            tessellator.addVertexWithUV(0, height, -offset, u2, v1);

            tessellator.addVertexWithUV(-offset, 0, 1, u1, v2);
            tessellator.addVertexWithUV(-offset, height, 1, u1, v1);
            tessellator.addVertexWithUV(-offset, height, 0, u2, v1);
            tessellator.addVertexWithUV(-offset, 0, 0, u2, v2);

            tessellator.addVertexWithUV(1, 0, 1 + offset, u1, v2);
            tessellator.addVertexWithUV(1, height, 1 + offset, u1, v1);
            tessellator.addVertexWithUV(0, height, 1 + offset, u2, v1);
            tessellator.addVertexWithUV(0, 0, 1 + offset, u2, v2);

            tessellator.addVertexWithUV(1 + offset, height, 1, u1, v1);
            tessellator.addVertexWithUV(1 + offset, 0, 1, u1, v2);
            tessellator.addVertexWithUV(1 + offset, 0, 0, u2, v2);
            tessellator.addVertexWithUV(1 + offset, height, 0, u2, v1);
        }
        ForgeHooksClient.setRenderLayer(BlockRenderLayer.CUTOUT);
        Map<EnumFacing, Boolean> map = Maps.newEnumMap(EnumFacing.class);
        for (EnumFacing facing : EnumFacing.VALUES){
            TileEntity tile = WorldHelper.getTileAt(te.getWorld(), te.getPos().offset(facing));
            map.put(facing, tile != null && tile instanceof IEFluxTank && te.getClientRenderFluid() == ((IEFluxTank) tile).getClientRenderFluid());
        }
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (!map.get(facing) && facing == EnumFacing.DOWN) {
                render(renderBlocks, facing, dDown);
            }
            for (TextureAtlasSprite tas : textureAtlasSprites) {
                if (!map.get(facing) && facing != EnumFacing.DOWN) {
                    render(renderBlocks, facing, tas);
                }
                if (!map.get(facing.getOpposite())) {
                    renderBlocks.render(facing, new BlockPos(-facing.getFrontOffsetX(), -facing.getFrontOffsetY(), -facing.getFrontOffsetZ()), tas);
                }
            }
        }


        tessellator.getMCTessellator().draw();
        GlStateManager.popMatrix();

    }

    private TextureAtlasSprite up, right, down, left, dDown;
    private TextureAtlasSprite[] textureAtlasSprites;

    private void render(RenderBlocks rb, EnumFacing side, TextureAtlasSprite texture){
        rb.render(side, new BlockPos(0, 0, 0), texture);
    }

    @Override
    public void registerTextures(IIconRegistrar iIconRegistrar) {
        up = iIconRegistrar.registerSprite(getResourceLocation("top"));
        right = iIconRegistrar.registerSprite(getResourceLocation("right"));
        down = iIconRegistrar.registerSprite(getResourceLocation("down"));
        left = iIconRegistrar.registerSprite(getResourceLocation("left"));
        dDown = iIconRegistrar.registerSprite(getResourceLocation("default_side"));
        textureAtlasSprites = new TextureAtlasSprite[]{
                up, right, down, left
        };
    }

    private ResourceLocation getResourceLocation(String s){
        return new EFluxResourceLocation("blocks/frame/"+s);
    }

}
