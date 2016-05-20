package elec332.eflux.inventory;

import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.eflux.EFlux;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import elec332.eflux.endernetwork.EnderNetwork;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.network.PacketSyncEnderContainerGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class ContainerEnderContainer extends BaseContainer implements WidgetButton.IButtonEvent {

    public ContainerEnderContainer(EntityPlayer player, IEnderNetworkComponent component) {
        super(player);
        addPlayerInventoryToContainer();
        up = addWidget(new WidgetButtonArrow(20, 15, WidgetButtonArrow.Direction.UP).addButtonEvent(this));
        down = addWidget(new WidgetButtonArrow(20, 55, WidgetButtonArrow.Direction.DOWN).addButtonEvent(this));
        connect = addWidget(new WidgetButton(60, 24, 0, 0, 60, 15, this)).setDisplayString("connect");
        disconnect = addWidget(new WidgetButton(60, 44, 0, 0, 60, 15, this)).setDisplayString("disconnect");
        down.setActive(false);
        up.setActive(false);
        connect.setActive(false);
        disconnect.setActive(false);
        validFreqs = new int[0];
        this.component = component;
        if (!player.worldObj.isRemote) {
            NBTTagCompound send = new NBTTagCompound();
            this.uuid = component.getUuid();
            validFreqs = EnderNetworkManager.get(player.worldObj).get(uuid).getFrequencies(component.getRequiredCapability());
            send.setIntArray("f", validFreqs);
            freqIdx = component.getFrequency();
            int max = getMaxID();
            if (freqIdx >= max){
                freqIdx = max - 1;
            }
            if (freqIdx < 0){
                freqIdx = 0;
            }
            send.setInteger("i", freqIdx);
            send.setString("u", uuid.toString());
            connection = component.getCurrentConnection() != null;
            send.setBoolean("b", connection);
            sendPacket(send);
            setConnectBtn();
        } else {
            init = false;
        }
    }

    private boolean init;
    private WidgetButtonArrow up, down;
    private WidgetButton connect, disconnect;

    private IEnderNetworkComponent component;
    private boolean connection;

    private int[] validFreqs;
    private int freqIdx;
    private UUID uuid;

    public int[] getValidFrequencies() {
        return validFreqs;
    }

    public int getMaxID(){
        return ((EnderNetwork)getNetwork()).getMaxID();
    }

    private IEnderNetwork getNetwork(){
        return EnderNetworkManager.get(thePlayer.worldObj).get(uuid);
    }

    public int getFrequency() {
        return freqIdx;
    }

    @SideOnly(Side.CLIENT)
    public void readPacket(NBTTagCompound tag){
        if (tag.hasKey("f")){
            validFreqs = tag.getIntArray("f");
            Arrays.sort(validFreqs);
        }
        if (tag.hasKey("i")){
            freqIdx = tag.getInteger("i");
        }
        if (tag.hasKey("u")){
            uuid = UUID.fromString(tag.getString("u"));
        }
        if (tag.hasKey("b")){
            connection = tag.getBoolean("b");
        }
        if (!init){
            init = true;
            setConnectBtn();
        }
    }

    private void checkInitialBtn(){
        int max = getMaxID();
        if (freqIdx >= (max - 1)){
            up.setActive(false);
        } else {
            up.setActive(true);
        }
        if (freqIdx <= 0){
            down.setActive(false);
        } else {
            down.setActive(true);
        }
    }

    private void sendPacket(NBTTagCompound data){
        EFlux.networkHandler.getNetworkWrapper().sendTo(new PacketSyncEnderContainerGui(data), (EntityPlayerMP) thePlayer);
    }

    @Override
    public void onButtonClicked(WidgetButton button) {
        int max = getMaxID();
        if (button == connect){
            connection = getNetwork().connect(component);
            setConnectBtn();
        } else if (button == disconnect){
            connection = false;
            EnderConnectionHelper.clearComponent(component);
            setConnectBtn();
        } else if (button == up){
            freqIdx++;
            if (freqIdx >= (max - 1)){
                up.setActive(false);
                freqIdx = max - 1;
            }
            if (!down.isActive()){
                down.setActive(true);
            }
            component.setFrequency(freqIdx);
        } else if (button == down){
            freqIdx--;
            if (freqIdx <= 0){
                down.setActive(false);
                freqIdx = 0;
            }
            if (!up.isActive()){
                up.setActive(true);
            }
            component.setFrequency(freqIdx);
        }
    }

    private void setConnectBtn(){
        connect.setActive(!connection);
        if (!connection) {
            checkInitialBtn();
        } else {
            up.setActive(false);
            down.setActive(false);
        }
        disconnect.setActive(connection);
    }

}
