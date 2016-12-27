package elec332.eflux.inventory;

import elec332.core.client.RenderHelper;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.core.inventory.widget.slot.WidgetScrollableSlot;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.Window;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.endernetwork.EnderNetwork;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.List;

/**
 * Created by Elec332 on 4-12-2016.
 */
public class WindowEnderMultiBlock extends Window implements WidgetButton.IButtonEventListener {

    public WindowEnderMultiBlock(EnderNetwork network) {
        super();
        this.network = network;
        this.max = network.getMaxID() - slotCount;
    }

    private static final int slotCount = 3;
    private WidgetScrollableSlot[] slots;
    private WidgetButtonArrow up, down;
    private final EnderNetwork network;
    private int start = 0;
    private final int max;

    @Override
    protected void initWindow() {
        addPlayerInventoryToContainer();
        up = addWidget(new WidgetButtonArrow(140, 10, WidgetButtonArrow.Direction.UP).addButtonEvent(this));
        up.setActive(false);
        down = addWidget(new WidgetButtonArrow(140, 60, WidgetButtonArrow.Direction.DOWN).addButtonEvent(this));
        slots = new WidgetScrollableSlot[slotCount];
        slots[0] = addWidget(new WidgetScrollableSlot(network.getUpgradeInventory(), 0, 120, 16));
        slots[1] = addWidget(new WidgetScrollableSlot(network.getUpgradeInventory(), 1, 120, 33));
        slots[2] = addWidget(new WidgetScrollableSlot(network.getUpgradeInventory(), 2, 120, 50));
        if (!getPlayer().getEntityWorld().isRemote){
            network.syncToClient((EntityPlayerMP) getPlayer());
        }
    }

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

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        int here = start;
        for (int i = 0; i < slotCount; i++) {
            RenderHelper.getMCFontrenderer().drawString(""+here, 30, 20 + i * 17, Color.BLACK.getRGB());
            String s = network == null ? "" : network.getCapabilityInformation(here);
            int w = RenderHelper.getMCFontrenderer().getStringWidth(s) / 2;
            RenderHelper.getMCFontrenderer().drawString(s, 80 - w, 20 + i * 17, Color.BLACK.getRGB());
            here++;
        }
    }

    @Override
    public void modifyTooltip(List<String> tooltip, WidgetSlot slot, ItemStack stack, int x, int y) {
        if (network != null && slot instanceof WidgetScrollableSlot){
            network.getStackTooltip(tooltip, slot.getSlotIndex());
        }
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new EFluxResourceLocation("gui/GuiNull.png");
    }


    private void checkSlotIndex(){
        for (int i = 0; i < slotCount; i++) {
            slots[i].setSlotIndex(start + i);
        }
    }

}
