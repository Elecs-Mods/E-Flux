package elec332.eflux.tileentity.ender;

import elec332.core.tile.TileBase;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkTile;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import elec332.eflux.endernetwork.EnderNetwork;
import elec332.eflux.endernetwork.util.DefaultEnderConnectableTile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import java.util.UUID;

import static elec332.eflux.endernetwork.EnderNetworkManager.get;

/**
 * Created by Elec332 on 9-5-2016.
 */
public abstract class AbstractEnderTileEntity<T> extends TileBase implements DefaultEnderConnectableTile.IConnectionListener {

    public AbstractEnderTileEntity(Capability<T> capability){
        this.capability = capability;
        enderHandler = new DefaultEnderConnectableTile<T>(capability, this).addListener(this);
    }

    private final Capability<T> capability;
    private final IEnderNetworkTile<T> enderHandler;
    private boolean connected;

    protected IEnderNetworkTile<T> getEnderHandler() {
        return this.enderHandler;
    }

    @Override
    public void onConnect() {
        connected = true;
    }

    @Override
    public void onDisconnect(DisconnectReason reason) {
        if (reason != DisconnectReason.SAVE){
            connected = false;
        }
    }

    @Override
    public void onTileLoaded() {
        if (connected){
            UUID id = enderHandler.getUuid();
            EnderNetwork net = get(worldObj).get(id);
            net.connect(enderHandler);
        }
    }

    @Override
    public void onTileUnloaded() {
        IStableEnderConnection<T> connection = enderHandler.getCurrentConnection();
        if (connection != null){
            connection.terminateConnection(DisconnectReason.SAVE);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("connected", connected);
        return tagCompound;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        connected = tagCompound.getBoolean("connected");
        System.out.println("ReadAET");
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        tagCompound.setTag("ender", enderHandler.serializeNBT());
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        enderHandler.deserializeNBT(tagCompound.getCompoundTag("ender"));
    }

    @Override
    public void onBlockRemoved() {
        IStableEnderConnection<T> connection = enderHandler.getCurrentConnection();
        if (connection != null){
            connection.terminateConnection(DisconnectReason.RECEIVER_DISCONNECT);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == this.capability && enderHandler.getCurrentConnection() != null || capability == EFluxAPI.ENDER_COMPONENT_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TYPE> TYPE getCapability(Capability<TYPE> capability, EnumFacing facing) {
        if (capability == this.capability){
            IEnderConnection<T> connection = enderHandler.getCurrentConnection();
            if (connection != null){
                return (TYPE) connection.get();
            }
            return null;
        }
        if (capability == EFluxAPI.ENDER_COMPONENT_CAPABILITY){
            return (TYPE) enderHandler;
        }
        return super.getCapability(capability, facing);
    }

}
