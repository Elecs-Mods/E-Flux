package elec332.eflux.endernetwork.capabilities;

import elec332.core.server.ServerHelper;
import elec332.core.util.BasicInventory;
import elec332.core.util.PlayerHelper;
import elec332.core.util.SafeWrappedIItemHandler;
import elec332.eflux.api.ender.internal.DisconnectReason;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import elec332.eflux.api.ender.internal.IStableEnderConnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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
        if (side.isServer()) {
            eventHandler = new Object() {

                @SubscribeEvent
                public void playerLogin(PlayerEvent.PlayerLoggedInEvent event) {
                    if (event.player instanceof EntityPlayerMP && PlayerHelper.getPlayerUUID(event.player).equals(EFluxEnderCapabilityPlayerInventory.this.player)) {
                        sendPoke(0);
                        checkInv();
                    }
                }

                @SubscribeEvent
                public void playerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
                    if (event.player instanceof EntityPlayerMP && PlayerHelper.getPlayerUUID(event.player).equals(EFluxEnderCapabilityPlayerInventory.this.player)) {
                        sendPoke(0);
                        checkInv();
                    }
                }

            };
            MinecraftForge.EVENT_BUS.register(eventHandler);
        }
    }

    private Object eventHandler;
    private UUID player;
    private static final String NBT_KEY = "enderPlayer";
    private static final IItemHandlerModifiable NULL_INVENTORY;
    private SafeWrappedIItemHandler inv;

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
        return inv;
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound data) {
        if (id == 0){
            checkInv();
        }
    }

    @Override
    public void validate() {
        checkInv();
    }

    private void checkInv(){
        if (inv != null){
            inv.clear();
        }
        IItemHandlerModifiable itemHandler;
        if (side.isServer()){
            EntityPlayerMP playerMP = ServerHelper.instance.getRealPlayer(this.player);
            if (playerMP != null){
                itemHandler = new PlayerMainInvWrapper(playerMP.inventory);
            } else {
                itemHandler = NULL_INVENTORY;
            }
        } else {
            if (Minecraft.getMinecraft().getConnection().getPlayerInfo(this.player) != null){
                itemHandler = new InvWrapper(new BasicInventory("", 36));
            } else {
                itemHandler = NULL_INVENTORY;
            }
        }
        inv = SafeWrappedIItemHandler.of(itemHandler);
    }

    @Override
    public void addInformation(List<String> list) {
        list.add("");
        //EntityPlayerMP player = ServerHelper.instance.getRealPlayer(this.player);
        NetworkPlayerInfo info = Minecraft.getMinecraft().getConnection().getPlayerInfo(this.player);
        list.add(info == null ? null : info.getGameProfile().getName());
    }

    @Override
    public int getEndergyConsumption() {
        return 50;
    }

    @Override
    public void addConnection(IStableEnderConnection<IItemHandler> connection) {
    }

    @Override
    public void removeConnection(IStableEnderConnection<IItemHandler> connection, DisconnectReason reason) {
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (eventHandler != null) {
            MinecraftForge.EVENT_BUS.unregister(eventHandler);
        }
        if (inv != null) {
            inv.clear();
            inv = null;
        }
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
                return stack;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                return null;
            }

            //1.10 <-> 1.11 @Override
            public int getSlotLimit(int slot) {
                return 0;
            }

        };
    }

}
