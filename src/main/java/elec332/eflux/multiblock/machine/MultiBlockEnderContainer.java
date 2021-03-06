package elec332.eflux.multiblock.machine;

import elec332.core.inventory.window.Window;
import elec332.core.multiblock.AbstractMultiBlock;
import elec332.core.tile.TileBase;
import elec332.core.util.ItemStackHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyReceiver;
import elec332.eflux.api.energy.container.EnergyContainer;
import elec332.eflux.api.energy.container.IEFluxPowerHandler;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.endernetwork.EnderNetwork;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.inventory.WindowEnderMultiBlock;
import elec332.eflux.items.ItemEFluxInfusedEnder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Created by Elec332 on 3-5-2016.
 */
public class MultiBlockEnderContainer extends AbstractMultiBlock implements IBreakableMachine {

    public MultiBlockEnderContainer(){
        energyContainer = null;//new EnergyContainer(1200, this, this);
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
    public boolean onAnyBlockActivated(EntityPlayer player, EnumHand hand, BlockPos pos, IBlockState state) {
        if (network != null && !ItemStackHelper.isStackValid(player.getHeldItem(hand)) && !getWorldObj().isRemote && network.isPowered()){
            openWindow(player, EFlux.proxy);
        }
        return true;
    }



    public void setUUID(EnderNetwork tile, TileBase dropper){
        if (((network != null && !network.getNetworkId().equals(tile.getNetworkId())) || network == null)){
            if (network != null) {
                ItemStack stack = ItemEFluxInfusedEnder.createStack(network.getNetworkId(), EnderNetworkManager.get(getWorldObj()).removeNetwork(network.getNetworkId()));
                WorldHelper.dropStack(dropper.getWorld(), dropper.getPos().offset(dropper.getTileFacing()), stack);
            }
            network = tile;
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
        return energyContainer;//network == null ? energyContainer : network;
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

    @Override
    public int getEFForOptimalRP() {
        return 90;
    }



    @Override
    public int getOptimalRP() {
        return ENDER_RP_REQ;
    }

    @Override
    public float getAcceptance() {
        return 0.02f;
    }

    @Override
    public void markObjectDirty() {
        markDirty();
    }
*/
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
    public Window createWindow(Object... args) {
        return new WindowEnderMultiBlock(network);
    }

    /*
    private final class ServerContainer extends ContainerMachine implements WidgetButton.IButtonEventListener {

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
*
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

    }*/

}
