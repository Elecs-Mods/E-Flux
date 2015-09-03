package elec332.eflux.client.blocktextures;

import elec332.core.client.render.SidedBlockRenderingCache;
import elec332.core.util.BlockSide;
import elec332.eflux.blocks.BlockMachinePart;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Elec332 on 3-9-2015.
 */
public final class MachineGlassTextureHandler implements SidedBlockRenderingCache.ITextureHandler {

    @Override
    public String getTextureForSide(BlockSide side, int type, int state) {
        switch (type){
            case 0:
                return getTextureLocation("heatGlass");
            case 1:
                if (side == BlockSide.FRONT || side == BlockSide.BACK)
                    return getTextureLocation("laserLensFront");
                return getTextureLocation("heatGlass");
            default:
                return getTextureLocation("null");
        }
    }

    @Override
    public IIcon getIconForWorldRendering(IBlockAccess iba, int x, int y, int z, int side, int meta, SidedBlockRenderingCache renderingCache) {
        switch (meta){
            case 1:
                return getSidedIcon(iba, x, y, z, side, meta, renderingCache);
            default:
                return renderingCache.getIconForNormalRendering(meta, side);
        }
    }

    private IIcon getSidedIcon(IBlockAccess iba, int x, int y, int z, int side, int meta, SidedBlockRenderingCache renderingCache){
        return renderingCache.getIconForBlockFacing(side, ((BlockMachinePart.TileEntityBlockMachine)iba.getTileEntity(x, y, z)).getTileFacing(), meta);
    }

    private String getTextureLocation(String s){
        return "eflux:machinepart/"+s;
    }
}
