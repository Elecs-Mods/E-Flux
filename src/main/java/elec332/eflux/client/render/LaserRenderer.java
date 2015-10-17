package elec332.eflux.client.render;

import elec332.core.client.render.AbstractBlockRenderer;
import elec332.core.client.render.RenderHelper;
import elec332.eflux.blocks.BlockHeatGlass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Elec332 on 3-10-2015.
 */
public class LaserRenderer extends AbstractBlockRenderer {

    public LaserRenderer() {
        super(BlockHeatGlass.LaserIndicator.class);
    }

    @Override
    protected boolean renderBlockAt(IBlockAccess world, double x, double y, double z, TileEntity tile, RenderBlocks renderer, float partialTicks, boolean tesr) {
        RenderHelper.drawLine(Vec3.createVectorHelper(x, y, z), Vec3.createVectorHelper(x + 4, y, z), null, 0.5f);
        return true;
    }

    @Override
    protected void renderItem(ItemStack stack, ItemRenderType renderType, RenderBlocks renderer) {
    }

    @Override
    protected boolean isISBRH() {
        return false;
    }

    @Override
    protected boolean isItemRenderer() {
        return false;
    }

    @Override
    protected boolean isTESR() {
        return true;
    }
}
