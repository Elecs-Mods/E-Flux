package elec332.eflux.client.render.tesr;

import elec332.core.client.RenderHelper;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Elec332 on 19-5-2016.
 */
public class TESRAreaMover extends TileEntitySpecialRenderer<TileEntityAreaMover> {

    @Override
    public void render(TileEntityAreaMover te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(0.0F, 0.0F, 0.0F, 0.6F);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.translate(x, y, z);
        AxisAlignedBB aabb = te.getUnpositionedAreaBounds();
        RenderHelper.drawExpandedSelectionBoundingBox(aabb);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

}
