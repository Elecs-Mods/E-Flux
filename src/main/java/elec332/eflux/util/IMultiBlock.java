package elec332.eflux.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 7-4-2015.
 */
public interface IMultiBlock {
    public World world();

    public int x();

    public int y();

    public int z();

    public boolean isMaster();

    public boolean setMaster();

    public void setSlave(int masterX, int masterY, int masterZ);

    public void invalidateMultiBlock();

    public boolean isSlave();

    public TileEntity getMaster();
}
