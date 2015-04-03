package elec332.eflux.blocks;

import elec332.core.api.wrench.IRotatable;
import elec332.core.api.wrench.IWrenchable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class BaseBlockWrenchable extends BaseBlockWithSidedFacing implements ITileEntityProvider, IWrenchable{
    public BaseBlockWrenchable(Material material, String name) {
        super(material, name);
    }


    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     *
     * @param p_149915_1_
     * @param p_149915_2_
     */
    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }

    @Override
    public ItemStack ItemDropped() {
        return null;
    }

    @Override
    public void onWrenched(World world, int i, int i1, int i2, ForgeDirection forgeDirection) {
        rotateBlock(world, i, i1, i2, forgeDirection);
    }
}
