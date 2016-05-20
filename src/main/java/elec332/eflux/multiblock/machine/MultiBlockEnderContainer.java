package elec332.eflux.multiblock.machine;

import elec332.core.client.RenderHelper;
import elec332.core.inventory.BaseContainer;
import elec332.core.inventory.ContainerMachine;
import elec332.core.inventory.ITileWithSlots;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiMachine;
import elec332.eflux.endernetwork.EnderNetwork;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.inventory.slot.SlotScrollable;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockEnderReader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Elec332 on 3-5-2016.
 */
public class MultiBlockEnderContainer extends AbstractMultiBlock implements IEFluxPowerHandler, IBreakableMachine, ITileWithSlots {

    public MultiBlockEnderContainer(){
        energyContainer = new EnergyContainer(1200, this, this);
    }

    /**
     * Initialise your multiblock here, all fields provided by @link IMultiblock have already been given a value
     */
    @Override
    public void init() {
    }

    private EnderNetwork network;
    private final EnergyContainer energyContainer;
    public static final int ENDER_RP_REQ = 5;

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        if (network != null) {
            tagCompound.setString("uuid", network.getNetworkId().toString());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if (tagCompound.hasKey("uuid")){
            network = EnderNetworkManager.get(getWorldObj()).get(UUID.fromString(tagCompound.getString("uuid")));
        }
    }

    @Override
    public boolean onAnyBlockActivated(EntityPlayer player, EnumHand hand, ItemStack stack, BlockPos pos, IBlockState state) {
        if (network != null && stack == null && !getWorldObj().isRemote && network.isPowered()){
            openGui(player, EFlux.instance);
        }
        return true;
    }

    @Override
    public Object getGui(EntityPlayer player, boolean client) {
        ServerContainer container = new ServerContainer(player);
        if (client){
            return new ClientGui(container);
        }
        return container;
    }

    public void setUUID(TileEntityMultiBlockEnderReader tile){
        if (((network != null && !network.getNetworkId().equals(tile.getNetworkID())) || network == null)){
            if (network != null) {
                EnderNetworkManager.get(getWorldObj()).removeNetwork(network.getNetworkId());
            }
            network = EnderNetworkManager.get(getWorldObj()).get(tile.getNetworkID());
            markDirty();
        }
    }

    @Override
    public void onTick() {
        if (isServer() && network == null){
            energyContainer.drainPower(420);
        }
    }

    private IEnergyReceiver getReceiver(){
        return network == null ? energyContainer : network.getEnergyContainer();
    }

    /**
     * Invalidate your multiblock here
     */
    @Override
    public void invalidate() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getSpecialCapability(Capability<T> capability, EnumFacing facing, @Nonnull BlockPos pos) {
        return capability == EFluxAPI.RECEIVER_CAPABILITY ? (T) getReceiver() : super.getCapability(capability, facing, pos);
    }

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    @Override
    public int getEFForOptimalRP() {
        return 90;
    }

    @Override
    public float getAcceptance() {
        return 0.02f;
    }

    @Override
    public int getOptimalRP() {
        return ENDER_RP_REQ;
    }

    @Override
    public void markObjectDirty() {
        markDirty();
    }

    @Override
    public boolean isBroken() {
        return false;
    }

    @Override
    public void setBroken(boolean broken) {
    }

    @Override
    public void onBroken() {
        if (isServer()) {
            getWorldObj().setBlockToAir(getBlockLocAtTranslatedPos(1, 0, 1));
            BlockPos pos = getBlockLocAtTranslatedPos(1, 1, 1);
            getWorldObj().newExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 4, true, false);
        }
    }

    @Override
    public void addSlots(BaseContainer container) {
    }

    private final class ServerContainer extends ContainerMachine implements WidgetButton.IButtonEvent {

        private ServerContainer(EntityPlayer player) {
            super(MultiBlockEnderContainer.this, player, 0);
            addPlayerInventoryToContainer();
            up = addWidget(new WidgetButtonArrow(140, 10, WidgetButtonArrow.Direction.UP).addButtonEvent(this));
            up.setActive(false);
            down = addWidget(new WidgetButtonArrow(140, 60, WidgetButtonArrow.Direction.DOWN).addButtonEvent(this));
            slots = new SlotScrollable[slotCount];
            slots[0] = (SlotScrollable) addSlotToContainer(new SlotScrollable(network.getUpgradeInventory(), 0, 120, 16));
            slots[1] = (SlotScrollable) addSlotToContainer(new SlotScrollable(network.getUpgradeInventory(), 1, 120, 33));
            slots[2] = (SlotScrollable) addSlotToContainer(new SlotScrollable(network.getUpgradeInventory(), 2, 120, 50));
            if (!player.worldObj.isRemote){
                network.syncToClient((EntityPlayerMP) player);
            }
        }

        private static final int slotCount = 3;
        private final SlotScrollable[] slots;
        private final WidgetButtonArrow up, down;
        private int start = 0;
        private final int max = network.getMaxID() - slotCount;

        @Override
        public void onButtonClicked(WidgetButton button) {
            if (button == up) {
                start--;
                if (!down.isActive()){
                    down.setActive(true);
                }
                if (start == 0){
                    up.setActive(false);
                }
                checkSlotIndex();
            }
            if (button == down) {
                start++;
                if (!up.isActive()){
                    up.setActive(true);
                }
                if (start == max){
                    down.setActive(false);
                }
                checkSlotIndex();
            }
        }

        private void checkSlotIndex(){
            for (int i = 0; i < slotCount; i++) {
                slots[i].setIndex(start + i);
            }
        }

    }

    @SideOnly(Side.CLIENT)
    private final class ClientGui extends GuiMachine {

        private ClientGui(ServerContainer container) {
            super(container);
            this.container = container;
        }

        private ServerContainer container;

        @Override
        protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
            super.drawGuiContainerForegroundLayer(mouseX, mouseY);
            int here = container.start;
            for (int i = 0; i < ServerContainer.slotCount; i++) {
                RenderHelper.getMCFontrenderer().drawString(""+here, 30, 20 + i * 17, Color.BLACK.getRGB());
                String s = network == null ? "" : network.getCapabilityInformation(here);
                int w = RenderHelper.getMCFontrenderer().getStringWidth(s) / 2;
                RenderHelper.getMCFontrenderer().drawString(s, 80 - w, 20 + i * 17, Color.BLACK.getRGB());
                here++;
            }
        }

        @Override //Mostly copied from vanilla
        protected void renderToolTip(ItemStack stack, int x, int y) {
            List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
            if (network != null && theSlot instanceof SlotScrollable){
                network.getStackTooltip(list, theSlot.getSlotIndex());
            }
            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, stack.getRarity().rarityColor + list.get(i));
                } else {
                    list.set(i, TextFormatting.GRAY + list.get(i));
                }
            }

            FontRenderer font = stack.getItem().getFontRenderer(stack);
            this.drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
        }

        @Override
        public ResourceLocation getBackgroundImageLocation() {
            return new EFluxResourceLocation("gui/GuiNull.png");
        }

    }

}
