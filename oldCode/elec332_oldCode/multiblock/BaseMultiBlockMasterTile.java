package elec332_oldCode.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Elec332 on 14-5-2015.
 */
public abstract class BaseMultiBlockMasterTile extends BaseMultiBlockTile implements IMultiBlockMainTile{

    @Override
    public boolean onBlockActivated(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!getController().isValid())
            getController().validate();
        else return getController().mainTileActivated(player);
        return super.onBlockActivated(player, side, hitX, hitY, hitZ);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (getController() != null)
            getController().writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (getController() != null)
            getController().readFromNBT(tagCompound);
    }
}
