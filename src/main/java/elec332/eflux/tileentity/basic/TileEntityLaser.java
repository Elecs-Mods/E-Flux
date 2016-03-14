package elec332.eflux.tileentity.basic;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.util.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Elec332 on 16-1-2016.
 */
@RegisterTile(name = "TileEntityEFluxLaser")
public class TileEntityLaser extends TileEntityBlockMachine {

    private BlockPos pos = BlockPos.ORIGIN;
    private boolean active;

    public BlockPos getRenderPos(){
        return pos;
    }

    public boolean active(){
        return active;
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        if (id == 2){
            active = tag.getBoolean("a");
        } else if (id == 3){
            pos = new NBTHelper(tag).getPos();
        }
    }

}
