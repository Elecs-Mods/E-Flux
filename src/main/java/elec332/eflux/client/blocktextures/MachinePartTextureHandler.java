package elec332.eflux.client.blocktextures;

import elec332.core.client.render.SidedBlockRenderingCache;
import elec332.core.util.BlockSide;
import elec332.eflux.blocks.BlockMachinePart;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 1-9-2015.
 */
public final class MachinePartTextureHandler implements SidedBlockRenderingCache.ITextureHandler{

    @Override
    public IIcon getIconForWorldRendering(IBlockAccess iba, int x, int y, int z, int side, int meta, SidedBlockRenderingCache renderingCache){
        switch (meta){
            case 3:
                ForgeDirection facing = ((BlockMachinePart.TileEntityBlockMachine)iba.getTileEntity(x, y, z)).getTileFacing();
                if (facing.ordinal() == side)
                    return renderingCache.getIconForNormalRendering(meta, 2);
                return renderingCache.getIconForNormalRendering(meta, 0);
            case 4:
                return getSidedIcon(iba, x, y, z, side, meta, renderingCache);
            case 5:
                return getSidedIcon(iba, x, y, z, side, meta, renderingCache);
            case 6:
                BlockMachinePart.TileEntityBlockMachine tileM = (BlockMachinePart.TileEntityBlockMachine)iba.getTileEntity(x, y, z);
                ForgeDirection facingM = tileM.getTileFacing();
                if (facingM.ordinal() == side)
                    return renderingCache.getIconDirectly(meta, 1, tileM.monitorSide);
                return getSidedIcon(iba, x, y, z, side, meta, renderingCache);
            case 7:
                return getSidedIcon(iba, x, y, z, side, meta, renderingCache);
            default:
                return renderingCache.getIconForNormalRendering(meta, side);
        }
    }

    @Override
     public String getTextureForSide(BlockSide side, int meta, int state) {
        switch (meta){
            case 0: //Basic Machine Casing
                return getTextureLocation("normal");
            case 1: //Normal Machine Casing
                return getTextureLocation("normal");
            case 2: //Advanced Machine Casing
                return getTextureLocation("normal");
            case 3: //BItem Outlet
                if (side == BlockSide.FRONT)
                    return getTextureLocation("itemOutletFront");
                return getTextureLocation("normal");
            case 4: //LaserCore
                if (side == BlockSide.FRONT)
                    return getTextureLocation("laserCoreFront");
                return getTextureLocation("normal");
            case 5: //Heater
                if (side == BlockSide.FRONT)
                    return getTextureLocation("heaterFront");
                return getTextureLocation("normal");
            case 6: //Monitor
                if (state == 1){
                    switch (side.getDefaultSide()){
                        case 0:
                            return getTextureLocation("monitorFull");
                        case 1:
                            return getTextureLocation("monitorRightSide");
                        case 2:
                            return getTextureLocation("monitorLeftSide");
                    }
                } else if (side == BlockSide.FRONT){
                    return getTextureLocation("monitorFull");
                }
                return getTextureLocation("normal");
            case 7: //Radiator
                if (side == BlockSide.FRONT)
                    return getTextureLocation("radiator");
                return getTextureLocation("normal");
            case 8: //Motor
                return getTextureLocation("normal");
            case 9: //Precision Motor
                return getTextureLocation("normal");
            case 10: //DustStorage
                return getTextureLocation("normal");
            default:
                return getTextureLocation("null");
        }
    }

    private IIcon getSidedIcon(IBlockAccess iba, int x, int y, int z, int side, int meta, SidedBlockRenderingCache renderingCache){
        return renderingCache.getIconForBlockFacing(side, ((BlockMachinePart.TileEntityBlockMachine)iba.getTileEntity(x, y, z)).getTileFacing(), meta);
    }

    private String getTextureLocation(String s){
        return "eflux:machinepart/"+s;
    }

}
