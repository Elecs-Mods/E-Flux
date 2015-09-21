package elec332.eflux.client.render;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import elec332.core.client.render.RenderHelper;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.tileentity.energy.cable.AbstractCable;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class CableRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderer.renderBlockAsItem(block, modelId, 0.5f);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId == getRenderId()){
            TileEntity tile = world.getTileEntity(x, y, z);
            if (tile instanceof AbstractCable) {
                BlockLoc tileLoc = new BlockLoc(tile);
                List<ForgeDirection> connections = Lists.newArrayList();
                for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS){
                    TileEntity tile2 = WorldHelper.getTileAt(world, tileLoc.atSide(direction));
                    if (tile2 instanceof AbstractCable && ((AbstractCable) tile2).getUniqueIdentifier().equals(((AbstractCable) tile).getUniqueIdentifier())){
                        connections.add(direction);
                    }
                }

                net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

                RenderHelper.bindBlockTextures();
                float thickness = 6 * RenderHelper.renderUnit;
                float heightStuff = (1 - thickness) / 2;
                float f1 = thickness + heightStuff;

                IIcon icon = EnumMachines.ADVANCEDCABLE.getBlock().getIcon(0, 5);
                if (!connections.isEmpty()) {
                    boolean flip = renderer.flipTexture;
                    for (ForgeDirection direction : connections) {
                        switch (direction){
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
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return modelId == getRenderId();
    }

    @Override
    public int getRenderId() {
        return RenderHandler.cable;
    }

}
