package elec332.eflux.client.inventory;

import com.google.common.collect.Lists;
import elec332.core.client.RenderHelper;
import elec332.core.client.inventory.BaseGuiContainer;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.inventory.ContainerEnderContainer;
import elec332.eflux.multiblock.machine.MultiBlockEnderContainer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Elec332 on 9-5-2016.
 */
public class GuiEnderContainer extends BaseGuiContainer {

    public GuiEnderContainer(ContainerEnderContainer container) {
        super(container);
        this.container = container;
    }

    public static final Color GREEN = new Color(0, 203, 0); //The GREEN in Color itself is too bright.

    private final ContainerEnderContainer container;

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        int here = container.getFrequency();
        boolean b = false;//Arrays.binarySearch(container.getValidFrequencies(), here) != -1;
        for (int i : container.getValidFrequencies()){
            if (i == here){
                b = true;
                break;
            }
        }
        RenderHelper.getMCFontrenderer().drawString("" + here, 27, 37, b ? GREEN.getRGB() : Color.RED.getRGB());
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new EFluxResourceLocation("gui/GuiNull.png");
    }

}
