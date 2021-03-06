package elec332.eflux.tileentity.misc;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.api.util.Area;
import elec332.core.main.ElecCore;
import elec332.core.world.WorldHelper;
import elec332.core.world.schematic.Schematic;
import elec332.core.world.schematic.SchematicHelper;
import elec332.eflux.util.Config;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 17-1-2016.
 */
@RegisteredTileEntity("TileEntityEFluxAreaMover")
public class TileEntityAreaMover extends TileEntity {

    private int tier = 0;

    public int getRange(){
        return 2 + tier;
    }

    public void setRange(int range){
        this.tier = getRange(range);
    }

    @Override
    public void validate() {
        super.validate();
        ElecCore.tickHandler.registerCall(new Runnable() {
            @Override
            public void run() {
                if (WorldHelper.chunkLoaded(getWorld(), pos)){
                    WorldHelper.markBlockForUpdate(getWorld(), pos);
                }
            }
        }, getWorld());
    }

    public static int getRange(int range){
        int max = Config.Misc.areaMoverRangeMax;
        if (range > max){
            return max;
        }
        if (range < 0){
            return 0;
        }
        return range;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("amTier", tier);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.tier = compound.getInteger("amTier");
    }

    public NBTTagCompound removeArea(){
        Area area = new Area(pos.add(getRange(), 0, getRange()), pos.add(-getRange(), 2 * getRange(), -getRange()));
        Schematic schematic = SchematicHelper.INSTANCE.createModSchematic(getWorld(), area, (short)0);
        NBTTagCompound tag = SchematicHelper.INSTANCE.writeSchematic(schematic);

        getWorld().restoringBlockSnapshots = true;
        for (int i = -getRange(); i < getRange() + 1; i++) {
            for (int j = -getRange(); j < getRange() + 1; j++) {
                for (int k = 0; k < getRange() * 2 + 1; k++) {
                    getWorld().setBlockToAir(pos.add(i, k, j));
                }
            }
        }
        getWorld().restoringBlockSnapshots = false;

        return tag;
    }

    public AxisAlignedBB getUnpositionedAreaBounds(){
        return new AxisAlignedBB(getRange() + 1, 0, getRange() + 1, -getRange(), 2 * getRange() + 1, -getRange());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return getUnpositionedAreaBounds().offset(getPos());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

}
