package elec332.eflux.tileentity.misc;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.world.schematic.Area;
import elec332.core.world.schematic.Schematic;
import elec332.core.world.schematic.SchematicHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 17-1-2016.
 */
@RegisterTile(name = "TileEntityEFluxAreaMover")
public class TileEntityAreaMover extends TileEntity {

    public int getRange(){
        return 2 + getBlockMetadata();
    }

    public NBTTagCompound removeArea(){
        Area area = new Area(pos.add(getRange(), 0, getRange()), pos.add(-getRange(), 2 * getRange(), -getRange()));
        Schematic schematic = SchematicHelper.INSTANCE.createModSchematic(worldObj, area, (short)0);
        NBTTagCompound tag = SchematicHelper.INSTANCE.writeSchematic(schematic);
        worldObj.restoringBlockSnapshots = true;
        for (int i = -getRange(); i < getRange() + 1; i++) {
            for (int j = -getRange(); j < getRange() + 1; j++) {
                for (int k = 0; k < getRange() * 2 + 1; k++) {
                    worldObj.setBlockToAir(pos.add(i, k, j));
                }
            }
        }
        worldObj.restoringBlockSnapshots = false;

        return tag;
    }

}
