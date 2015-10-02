package elec332.eflux.client.render;

import com.google.common.collect.Lists;
import elec332.core.client.render.AbstractBlockRenderer;
import elec332.core.client.render.RenderHelper;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.IEnergySource;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.tileentity.energy.cable.AbstractCable;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class CableRenderer extends AbstractBlockRenderer {

    public CableRenderer() {
        super(BlockRegister.cable);
    }

    @Override
    public boolean renderBlockAt(IBlockAccess world, double x, double y, double z, RenderBlocks renderer, float partialTicks, boolean tesr) {
        TileEntity tile = world.getTileEntity((int) x, (int) y, (int) z);
        if (tile instanceof AbstractCable) {
            BlockLoc tileLoc = new BlockLoc(tile);
            List<ForgeDirection> connections = Lists.newArrayList();
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                TileEntity tile2 = WorldHelper.getTileAt(world, tileLoc.atSide(direction));
                if (tile2 instanceof AbstractCable && ((AbstractCable) tile2).getUniqueIdentifier().equals(((AbstractCable) tile).getUniqueIdentifier()) || tile instanceof IEnergySource || tile instanceof IEnergyReceiver) {
                    connections.add(direction);
                }
            }

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            Tessellator.instance.setColorRGBA_F(1, 1, 1, 1);

            RenderHelper.bindBlockTextures();
            float thickness = 6 * RenderHelper.renderUnit;
            float heightStuff = (1 - thickness) / 2;
            float f1 = thickness + heightStuff;

            IIcon icon = block.getIcon(0, 0);
            if (!connections.isEmpty()) {
                boolean flip = renderer.flipTexture;
                for (ForgeDirection direction : connections) {
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
                    renderer.renderFaceYNeg(block, x, y, z, icon);
                    renderer.renderFaceYPos(block, x, y, z, icon);
                    renderer.renderFaceZPos(block, x, y, z, icon);
                    renderer.renderFaceXNeg(block, x, y, z, icon);
                    renderer.flipTexture = true;
                    renderer.renderFaceZNeg(block, x, y, z, icon);
                    renderer.renderFaceXPos(block, x, y, z, icon);
                    renderer.flipTexture = false;
                }
                renderer.flipTexture = flip;
            } else {
                renderer.setRenderBounds(heightStuff, heightStuff, heightStuff, f1, f1, f1);
                renderer.renderFaceYNeg(block, x, y, z, icon);
                renderer.renderFaceYPos(block, x, y, z, icon);
                renderer.renderFaceZNeg(block, x, y, z, icon);
                renderer.renderFaceZPos(block, x, y, z, icon);
                renderer.renderFaceXNeg(block, x, y, z, icon);
                renderer.renderFaceXPos(block, x, y, z, icon);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void renderItem(ItemStack stack, ItemRenderType renderType, RenderBlocks renderer) {

    }

    @Override
    protected boolean isISBRH() {
        return true;
    }

    @Override
    protected boolean isItemRenderer() {
        return true;
    }

    @Override
    protected boolean isTESR() {
        return false;
    }

}
