package elec332.eflux.tileentity.multiblock;

import elec332.core.api.annotations.RegisterTile;
import elec332.core.client.RenderHelper;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.multiblock.IMultiBlock;
import elec332.core.tile.IInventoryTile;
import elec332.core.util.BasicInventory;
import elec332.core.util.InventoryHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiEnderContainer;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.ItemEntangledEnder;
import elec332.eflux.multiblock.machine.MultiBlockEnderContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.UUID;

/**
 * Created by Elec332 on 4-5-2016.
 */
@RegisterTile(name = "TileEntityEFluxMultiBlockEnderReader")
public class TileEntityMultiBlockEnderReader extends AbstractTileEntityMultiBlock implements IInventoryTile {

    public TileEntityMultiBlockEnderReader(){
    }

    private UUID networkID;

    @Override
    public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (InventoryHelper.areEqualNoSizeNoNBT(stack, ItemRegister.entangledEnder)){
            if (!worldObj.isRemote) {
                //if (this.networkID != null) {
                //    EnderNetworkManager.removeNetwork(networkID);
                //}
                this.networkID = ItemEntangledEnder.getUUID(stack);
                if (getMultiBlock() != null) {
                    ((MultiBlockEnderContainer) getMultiBlock()).setUUID(this);
                }
                //player.inventory.decrStackSize(player.inventory.getSlotFor(stack), 1);
                NBTTagCompound send = new NBTTagCompound();
                if (networkID != null){
                    send.setString("u", networkID.toString());
                }
                sendPacket(2, send);
            }
            return true;
        } else if (getMultiBlock() != null && networkID != null && EnderNetworkManager.get(worldObj).get(networkID) != null){
            if (!worldObj.isRemote){
                openGui(player, EFlux.instance, 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setMultiBlock(IMultiBlock multiBlock, EnumFacing facing, String structure) {
        super.setMultiBlock(multiBlock, facing, structure);
        if (!worldObj.isRemote) {
            ((MultiBlockEnderContainer) multiBlock).setUUID(this);
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
                    ((MultiBlockEnderContainer) mb).setUUID(this);
                }
            }
            return;
        }
        super.onDataPacket(id, tag);
    }

    @Override
    public Container getGuiServer(EntityPlayer player) {
        return new InternalContainer(player);
    }

    @Override
    public Object getGuiClient(EntityPlayer player) {
        return new InternalGui((InternalContainer) getGuiServer(player));
    }

    private class InternalContainer extends BaseContainer implements WidgetButton.IButtonEvent{

        private InternalContainer(EntityPlayer player) {
            super(player);
            valid = new int[0];
            inventory = new BasicInventory("", 1){

                @Override
                public void setInventorySlotContents(int slotID, ItemStack stack) {
                    super.setInventorySlotContents(slotID, stack);
                    checkInitialBtn();
                }

                @Override
                public boolean isItemValidForSlot(int id, ItemStack stack) {
                    return stack == null || (stack.getItem() != null && stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null));
                }

            };
            addPlayerInventoryToContainer();
            addSlotToContainer(new Slot(inventory, 0, 50, 32));
            up = addWidget(new WidgetButtonArrow(20, 15, WidgetButtonArrow.Direction.UP).addButtonEvent(this));
            down = addWidget(new WidgetButtonArrow(20, 55, WidgetButtonArrow.Direction.DOWN).addButtonEvent(this));
            set = addWidget(new WidgetButton(80, 24, 0, 0, 30, 15, this)).setDisplayString("set");
            clear = addWidget(new WidgetButton(80, 44, 0, 0, 30, 15, this)).setDisplayString("clear");

            maxID = EnderNetworkManager.get(worldObj).get(networkID).getMaxID();
            checkInitialBtn();
        }

        private WidgetButtonArrow up, down;
        private WidgetButton set, clear;
        private IInventory inventory;

        private int freq;
        private final int maxID;
        private int[] valid;

        private void checkInitialBtn(){
            if (inventory.getStackInSlot(0) == null){
                deactivateAll();
                valid = new int[0];
                freq = 0;
            } else {
                checkUpDown();
                boolean b = false;
                ItemStack stack = inventory.getStackInSlot(0);
                if (stack != null && stack.getItem() != null) {
                    if (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                        IEnderNetworkComponent component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                        if (component != null) {
                            valid = EnderNetworkManager.get(worldObj).get(networkID).getFrequencies(component.getRequiredCapability());
                            freq = Math.max(component.getFrequency(), 0);
                            b = true;
                            if (!clear.isActive()){
                                clear.setActive(true);
                            }
                            checkFreq();
                        }
                    }
                }
                if (!b){
                    deactivateAll();
                }
            }
        }

        private void deactivateAll(){
            if (up.isActive()){
                up.setActive(false);
            }
            if (down.isActive()){
                down.setActive(false);
            }
            if (set.isActive()){
                set.setActive(false);
            }
            if (clear.isActive()){
                clear.setActive(false);
            }
        }

        private void checkFreq(){
            if (!set.isActive()){
                set.setActive(true);
            }
            if (!isValidFreq(freq)&& set.isActive()){
                set.setActive(false);
            }
        }

        private void checkUpDown(){
            if (freq >= (maxID - 1)) {
                up.setActive(false);
            } else {
                up.setActive(true);
            }
            if (freq <= 0) {
                down.setActive(false);
            } else {
                down.setActive(true);
            }
        }

        @Override
        public void onButtonClicked(WidgetButton button) {
            if (button == up){
                freq++;
                if (freq >= (maxID - 1)){
                    up.setActive(false);
                    freq = maxID - 1;
                }
                if (!down.isActive()){
                    down.setActive(true);
                }
                checkFreq();
            } else if (button == down){
                freq--;
                if (freq <= 0){
                    down.setActive(false);
                    freq = 0;
                }
                if (!up.isActive()){
                    up.setActive(true);
                }
                checkFreq();
            } else if (button == set){
                ItemStack stack = inventory.getStackInSlot(0);
                if (stack != null && stack.getItem() != null){
                    if (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                        IEnderNetworkComponent component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                        if (component != null) {
                            if (isValidFreq(freq)){
                                component.setUUID(networkID);
                                component.setFrequency(freq);
                            }
                        }
                    }
                }
            } else if (button == clear){
                ItemStack stack = inventory.getStackInSlot(0);
                if (stack != null && stack.getItem() != null){
                    if (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                        IEnderNetworkComponent component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                        if (component != null) {
                            component.setUUID(null);
                            component.setFrequency(-1);
                        }
                    }
                    freq = 0;
                    checkUpDown();
                }
            }
        }

        boolean isValidFreq(int freq){
            for (int i : valid){
                if (i == freq){
                    return true;
                }
            }
            return false;
        }

        @Override
        public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
            ItemStack ret = super.slotClick(slotId, dragType, clickTypeIn, player);
            detectAndSendChanges();
            return ret;
        }

        @Override
        public void onContainerClosed(EntityPlayer player) {
            if (!player.worldObj.isRemote && inventory.getStackInSlot(0) != null){
                player.entityDropItem(inventory.getStackInSlot(0), 1);
                inventory.clear();
            }
            super.onContainerClosed(player);
        }

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

    }

}
