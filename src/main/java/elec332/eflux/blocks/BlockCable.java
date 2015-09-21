package elec332.eflux.blocks;

import elec332.core.baseclasses.tileentity.BlockTileBase;
import elec332.core.client.render.RenderHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.render.RenderHandler;
import elec332.eflux.tileentity.energy.cable.BasicCable;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class BlockCable extends BlockTileBase {

    public BlockCable(String blockName) {
        super(Material.snow, BasicCable.class, blockName, EFlux.ModID);
    }

    @Override
    public int getRenderType() {
        return RenderHandler.cable;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
        float thickness = 6 * RenderHelper.renderUnit;
        float heightStuff = (1 - thickness)/2;
        float f1 = thickness + heightStuff;
        setBlockBounds(heightStuff, heightStuff, heightStuff, f1, f1, f1);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
}
