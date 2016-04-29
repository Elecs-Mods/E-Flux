package elec332.eflux.client.render;

import elec332.core.client.ElecTessellator;
import elec332.core.client.ITessellator;
import elec332.core.client.RenderBlocks;
import elec332.core.client.RenderHelper;
import elec332.eflux.util.IEFluxTank;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by Elec332 on 19-4-2016.
 */
public class TileEntityTankRenderer<T extends TileEntity & IEFluxTank> extends TileEntitySpecialRenderer<T> {

    @Override
    public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage) {

        Fluid fluid = te.getClientRenderFluid();
        float height = te.getClientRenderHeight();
        if (fluid == null || height <= 0){
            return;
        }

        float offset = 0.0f;

        GlStateManager.pushMatrix();GlStateManager.translate(x, y, z);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderHelper.bindBlockTextures();
        ITessellator tessellator = RenderHelper.getTessellator();
        ((ElecTessellator)tessellator).startDrawingWorldBlock();
        RenderBlocks renderBlocks = RenderHelper.getBlockRenderer();
        renderBlocks.resetTessellator();

        TextureAtlasSprite fluidTexture = RenderHelper.getFluidTexture(fluid, false);

        float u1 = fluidTexture.getMinU();
        float v1 = fluidTexture.getMinV();
        float u2 = fluidTexture.getMaxU();
        float v2 = fluidTexture.getMaxV() - ((fluidTexture.getMaxV() - fluidTexture.getMinV()) * (1 - height));
        tessellator.setColorRGBA(255, 255, 255, 128);
        tessellator.setBrightness(240);

        renderBlocks.renderFaceYPos(0, height - 1, 0, fluidTexture); //Im lazy

        tessellator.addVertexWithUV(1, height, -offset, u1, v1);
        tessellator.addVertexWithUV(1 , 0, -offset, u1, v2);
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

        tessellator.getMCTessellator().draw();
        GlStateManager.popMatrix();

    }

}
