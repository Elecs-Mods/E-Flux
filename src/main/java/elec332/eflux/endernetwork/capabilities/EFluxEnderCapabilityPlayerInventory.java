package elec332.eflux.endernetwork.capabilities;

import elec332.core.server.ServerHelper;
import elec332.core.util.BasicInventory;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elec332 on 7-5-2016.
 */
public class EFluxEnderCapabilityPlayerInventory extends AbstractEnderCapability<IItemHandler> {

    public EFluxEnderCapabilityPlayerInventory(Side side, IEnderNetwork network) {
        super(side, network);
    }

    private UUID player;
    private static final String NBT_KEY = "enderPlayer";
    private static final IItemHandler NULL_INVENTORY;

    @Override
    @Nonnull
    public Capability<IItemHandler> getCapability() {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Override
    @Nullable
    public IItemHandler get() {
        if (player == null){
            return NULL_INVENTORY;
        }
        EntityPlayer player;
        if (side.isServer()) {
            EntityPlayerMP playerMP = ServerHelper.instance.getRealPlayer(this.player);
            if (playerMP != null){
                player = playerMP;
            } else {
                return NULL_INVENTORY;
            }
        } else {
            if (Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.player) != null) { //TODO: I do not trust this...
                return new InvWrapper(new BasicInventory("s", 36));
            } else {
                return NULL_INVENTORY;
            }
            /*if (Minecraft.getMinecraft().getSession().getProfile().getId().equals(this.player)){
                player = Minecraft.getMinecraft().thePlayer;
            } else {
                return null;
            }*/
        }
        return new PlayerMainInvWrapper(player.inventory);
    }

    @Override
    public void addInformation(List<String> list) {
        list.add("");
        //EntityPlayerMP player = ServerHelper.instance.getRealPlayer(this.player);
        NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.player);
        list.add(info == null ? null : info.getGameProfile().getName());
    }

    @Override
    public int getPowerConsumption() {
        return 200;
    }

    @Override
    public void addConnection(IStableEnderConnection<IItemHandler> connection) {
    }

    @Override
    public void removeConnection(IStableEnderConnection<IItemHandler> connection, DisconnectReason reason) {
    }

    @Override
    @Nonnull
    public NBTTagCompound serializeNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        if (player != null){
            ret.setString(NBT_KEY, player.toString());
        }
        return ret;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(NBT_KEY)){
            player = UUID.fromString(nbt.getString(NBT_KEY));
        }
    }

    static {
        NULL_INVENTORY = new IItemHandlerModifiable() {

            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
            }

            @Override
            public int getSlots() {
                return 0;
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return null;
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                return null;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return null;
            }

        };
    }

}
