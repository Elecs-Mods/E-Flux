package elec332.eflux.client.inventory;

import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;

/**
 * Created by Elec332 on 9-5-2015.
 */
public abstract class GuiMachine extends BaseGuiContainer {

    public GuiMachine(BaseContainer container) {
        super(container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i_, int i1) {
        super.drawGuiContainerBackgroundLayer(f, i_, i1);
        /*IHasProgressBar tileWithProgressBar = ((ContainerMachine) container).getTileWithProgressBar();
        if (tileWithProgressBar != null && tileWithProgressBar.isWorking()){
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            i1 = (int)(tileWithProgressBar.getProgressScaled()*24);
            this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
        }*/
    }
}
