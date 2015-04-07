package elec332.eflux.blocks;

import elec332.core.api.wrench.IWrenchable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 3-4-2015.
 */
public abstract class BaseBlockMachine extends BaseBlockWithSidedFacing implements ITileEntityProvider, IWrenchable{
    public BaseBlockMachine(Material material, String name) {
        super(material, name);
    }

    @Override
    public void onWrenched(World world, int i, int i1, int i2, ForgeDirection forgeDirection) {
        switch (forgeDirection){
            case NORTH:
                world.markBlockForUpdate(i, i1, i2);
                world.setBlockMetadataWithNotify(i, i1, i2, 0, 2);
                break;
            case EAST:
                world.markBlockForUpdate(i, i1, i2);
                world.setBlockMetadataWithNotify(i, i1, i2, 1, 2);
                break;
            case SOUTH:
                world.markBlockForUpdate(i, i1, i2);
                world.setBlockMetadataWithNotify(i, i1, i2, 2, 2);
                break;
            case WEST:
                world.markBlockForUpdate(i, i1, i2);
                world.setBlockMetadataWithNotify(i, i1, i2, 3, 2);
                break;
            default:
                break;

        }
    }
}
