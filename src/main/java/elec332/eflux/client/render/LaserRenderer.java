package elec332.eflux.client.render;

import elec332.core.client.ITessellator;
import elec332.core.client.RenderBlocks;
import elec332.core.client.render.AbstractBlockRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Elec332 on 3-10-2015.
 */
public class LaserRenderer extends AbstractBlockRenderer{
    @Override
    public void renderBlock(IBlockAccess iba, IBlockState state, BlockPos pos, RenderBlocks renderBlocks, ITessellator tessellator, WorldRenderer renderer) {

    }

    @Override
    public boolean shouldRenderBlock(IBlockAccess iba, IBlockState state, BlockPos pos) {
        return false;
    }
}/* {

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
        */