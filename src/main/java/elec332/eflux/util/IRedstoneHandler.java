package elec332.eflux.util;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 6-4-2015.
 */
public interface IRedstoneHandler {

    public boolean hasComparatorInputOverride();

    public int getComparatorInputOverride(World world, int x, int y, int z, int side);

    public int isProvidingWeakPower(IBlockAccess world, int i, int j, int k, int side);

    public boolean canConnectRedstone(IBlockAccess world, int i, int j, int k, int direction);
}
