package elec332.eflux.client.inventory;

import elec332.eflux.inventory.ContainerMachine;
import elec332.eflux.inventory.ITileWithSlots;
import net.minecraft.inventory.Container;

/**
 * Created by Elec332 on 9-5-2015.
 */
public abstract class GuiMachine extends BaseGuiContainer {

    public GuiMachine(Container container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i_, int i1) {
        super.drawGuiContainerBackgroundLayer(f, i_, i1);
        ITileWithSlots tileWithSlots = ((ContainerMachine) container).getTileWithSlots();
        if (tileWithSlots.isWorking()){
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            i1 = (int)(tileWithSlots.getProgressScaled()*24);
            this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
        }
    }
}
