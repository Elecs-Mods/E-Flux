package elec332.eflux.util;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 7-4-2015.
 */
public interface IMultiBlock {

    public boolean isMaster();

    public boolean setMaster();

    public void setSlave(int masterX, int masterY, int masterZ);

    public void invalidateMultiBlock();

    public boolean isSlave();

    public TileEntity getMaster();

}
