package elec332.eflux.tileentity.energy.cable;

import elec332.core.main.ElecCore;
import elec332.core.util.NBTHelper;
import elec332.eflux.api.energy.IEnergyTransmitter;
import elec332.eflux.tileentity.EnergyTileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;

/**
 * Created by Elec332 on 20-9-2015.
 */
public abstract class AbstractCable extends EnergyTileBase implements IEnergyTransmitter {

    public void setGridIdentifier(UUID uuid){
        this.gridIdentifier = uuid;
        sendPacket(9, new NBTHelper().addToTag(uuid.toString(), "uuid").serializeNBT());
    }

    private UUID gridIdentifier;

    public UUID getGridIdentifier(){
        return this.gridIdentifier;
    }

    public abstract String getUniqueIdentifier();

    @Override
    public boolean canConnectTo(IEnergyTransmitter otherTransmitter) {
        return !(otherTransmitter instanceof AbstractCable) || getUniqueIdentifier().equals(((AbstractCable) otherTransmitter).getUniqueIdentifier());
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

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return /*capability == EFluxAPI.TRANSMITTER_CAPABILITY || */super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return /*capability == EFluxAPI.TRANSMITTER_CAPABILITY ? (T)this : */super.getCapability(capability, facing);
    }

}
