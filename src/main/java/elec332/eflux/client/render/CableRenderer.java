package elec332.eflux.client.render;

import com.google.common.collect.Lists;
import elec332.core.client.ITessellator;
import elec332.core.client.RenderBlocks;
import elec332.core.client.RenderHelper;
import elec332.core.client.render.AbstractBlockRenderer;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.blocks.BlockCable;
import elec332.eflux.tileentity.energy.cable.AbstractCable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import java.util.List;

/**
 * Created by Elec332 on 10-12-2015.
 */
public class CableRenderer extends AbstractBlockRenderer {

    @Override
    public void renderBlock(IBlockAccess iba, IBlockState state, BlockPos pos, RenderBlocks renderer, ITessellator tessellator, VertexBuffer worldRenderer) {

        TileEntity tile = WorldHelper.getTileAt(iba, pos);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        List<EnumFacing> connections = Lists.newArrayList();

        for (EnumFacing direction : EnumFacing.VALUES) {
            TileEntity tile2 = WorldHelper.getTileAt(iba, pos.offset(direction));
            if ((tile2 instanceof AbstractCable && ((AbstractCable) tile2).getUniqueIdentifier().equals(((AbstractCable) tile).getUniqueIdentifier())) || EnergyAPIHelper.isProvider(tile, direction.getOpposite()) || EnergyAPIHelper.isReceiver(tile, direction.getOpposite())){
                connections.add(direction);
            }
        }

        float thickness = 6 * RenderHelper.renderUnit;
        float heightStuff = (1 - thickness) / 2;
        float f1 = thickness + heightStuff;
        TextureAtlasSprite icon = ((BlockCable) state.getBlock()).getTextureFor(state);

        if (!connections.isEmpty()) {
            for (EnumFacing direction : connections) {
                switch (direction) {
                    case UP:
                        renderer.setRenderBounds(heightStuff, heightStuff, heightStuff, f1, 1, f1);
                        break;
                    case DOWN:
                        renderer.setRenderBounds(heightStuff, 0, heightStuff, f1, f1, f1);
                        break;
                    case NORTH:
                        renderer.setRenderBounds(heightStuff, heightStuff, 0, f1, f1, f1);
                        break;
                    case EAST:
                        renderer.setRenderBounds(heightStuff, heightStuff, heightStuff, 1, f1, f1);
                        break;
                    case SOUTH:
                        renderer.setRenderBounds(heightStuff, heightStuff, heightStuff, f1, f1, 1);
                        break;
                    case WEST:
                        renderer.setRenderBounds(0, heightStuff, heightStuff, f1, f1, f1);
                        break;
                    default:
                        break;
                }
                renderer.renderFaceYNeg(x, y, z, icon);
                renderer.renderFaceYPos(x, y, z, icon);
                renderer.renderFaceZPos(x, y, z, icon);
                renderer.renderFaceXNeg(x, y, z, icon);
                renderer.flipTexture = true;
                renderer.renderFaceZNeg(x, y, z, icon);
                renderer.renderFaceXPos(x, y, z, icon);
                renderer.flipTexture = false;
            }
        } else {
            renderer.setRenderBounds(heightStuff, heightStuff, heightStuff, f1, f1, f1);
            renderer.renderFaceYNeg(x, y, z, icon);
            renderer.renderFaceYPos(x, y, z, icon);
            renderer.renderFaceZNeg(x, y, z, icon);
            renderer.renderFaceZPos(x, y, z, icon);
            renderer.renderFaceXNeg(x, y, z, icon);
            renderer.renderFaceXPos(x, y, z, icon);
        }
    }

    @Override
    public boolean shouldRenderBlock(IBlockAccess iba, IBlockState state, BlockPos pos) {
        return WorldHelper.getTileAt(iba, pos) instanceof AbstractCable && state.getBlock() instanceof BlockCable;
    }

}
