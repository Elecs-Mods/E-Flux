package elec332.eflux.tileentity.multiblock;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.inventory.window.IWindowFactory;
import elec332.core.inventory.window.Window;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.multiblock.IMultiBlock;
import elec332.eflux.EFlux;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.inventory.WindowEnderReader;
import elec332.eflux.items.ItemEFluxInfusedEnder;
import elec332.eflux.multiblock.machine.MultiBlockEnderContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;

import java.util.UUID;

/**
 * Created by Elec332 on 4-5-2016.
 */
@RegisteredTileEntity("TileEntityEFluxMultiBlockEnderReader")
public class TileEntityMultiBlockEnderReader extends AbstractTileEntityMultiBlock implements IWindowFactory {

    public TileEntityMultiBlockEnderReader(){
    }

    private UUID networkID;

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (hand == EnumHand.OFF_HAND){
            return false;
        }
        int current = player.inventory.currentItem;
        IInventory playerInv = player.inventory;
        ItemStack stack = player.getHeldItem(hand);
        if (ItemEFluxInfusedEnder.isActive(stack)){
            if (!getWorld().isRemote) {
                this.networkID = ItemEFluxInfusedEnder.getUUID(stack);
                if (getMultiBlock() != null) {
                    ((MultiBlockEnderContainer) getMultiBlock()).setUUID(EnderNetworkManager.get(getWorld()).createNetwork(this.networkID, ItemEFluxInfusedEnder.getNetworkData(stack)), this);
                }
                playerInv.decrStackSize(current, 1);
                NBTTagCompound send = new NBTTagCompound();
                if (networkID != null){
                    send.setString("u", networkID.toString());
                }
                sendPacket(2, send);
            }
            return true;
        } else if (getMultiBlock() != null && networkID != null && EnderNetworkManager.get(getWorld()).get(networkID) != null){
            if (!getWorld().isRemote){
                openWindow(player, EFlux.proxy, 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setMultiBlock(IMultiBlock multiBlock, EnumFacing facing, String structure) {
        super.setMultiBlock(multiBlock, facing, structure);
        if (!getWorld().isRemote) {
            ((MultiBlockEnderContainer) multiBlock).setUUID(EnderNetworkManager.get(getWorld()).get(getNetworkID()), this);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
    }

    @Override
    public void writeToItemStack(NBTTagCompound tagCompound) {
        super.writeToItemStack(tagCompound);
        if (networkID != null) {
            tagCompound.setString("nUUIDt", networkID.toString());
        }
    }

    @Override
    public void readItemStackNBT(NBTTagCompound tagCompound) {
        super.readItemStackNBT(tagCompound);
        if (tagCompound.hasKey("nUUIDt")){
            networkID = UUID.fromString(tagCompound.getString("nUUIDt"));
        }
    }

    public UUID getNetworkID() {
        return networkID;
    }

    @Override
    public void onDataPacket(int id, NBTTagCompound tag) {
        if (id == 2){
            networkID = null;
            if (tag.hasKey("u")){
                networkID = UUID.fromString(tag.getString("u"));
                AbstractMultiBlock mb = getMultiBlock();
                if (mb != null) {
                    ((MultiBlockEnderContainer) mb).setUUID(EnderNetworkManager.get(getWorld()).get(getNetworkID()), this);
                }
            }
            return;
        }
        super.onDataPacket(id, tag);
    }

    @Override
    public Window createWindow(Object... args) {
        return new WindowEnderReader(this);
    }
/*
    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new InternalContainer(player);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new InternalGui((InternalContainer) getGuiServer(player));
    }

    private class InternalGui extends BaseGuiContainer {

        private InternalGui(InternalContainer container) {
            super(container);
            this.container = container;
        }

        private final InternalContainer container;


        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
            super.drawGuiContainerForegroundLayer(mouseX, mouseY);
            int here = container.freq;
            boolean b = false;//Arrays.binarySearch(container.getValidFrequencies(), here) != -1;
            for (int i : container.valid){
                if (i == here){
                    b = true;
                    break;
                }
            }
            RenderHelper.getMCFontrenderer().drawString("" + here, 27, 37, b ? GuiEnderContainer.GREEN.getRGB() : Color.RED.getRGB());
        }

        @Override
        public ResourceLocation getBackgroundImageLocation() {
            return new EFluxResourceLocation("gui/GuiNull.png");
        }

    }*/

}
