package elec332.eflux.tileentity.energy.cable;

import elec332.core.main.ElecCore;
import elec332.core.tile.TileBase;
import elec332.core.util.NBTHelper;
import elec332.eflux.api.energy.EnergyAPIHelper;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.tileentity.EnergyTileBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * Created by Elec332 on 20-9-2015.
 */
public abstract class AbstractCable extends EnergyTileBase implements IEnergyTransmitter {

    public void setGridIdentifier(UUID uuid){
        this.gridIdentifier = uuid;
        sendPacket(9, new NBTHelper().addToTag(uuid.toString(), "uuid").toNBT());
    }

    private UUID gridIdentifier;

    public UUID getGridIdentifier(){
        return this.gridIdentifier;
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        if (id == 9){
            this.gridIdentifier = UUID.fromString(tag.getString("uuid"));
            ElecCore.tickHandler.registerCall(new Runnable() {
                @Override
                public void run() {
                    reRenderBlock();
                }
            }, worldObj);
            return;
        }
        super.onDataPacket(id, tag);
    }

}
