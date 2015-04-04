package elec332.eflux.client.inventory;

import elec332.eflux.EFlux;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Elec332 on 4-4-2015.
 */
public abstract class BaseGuiContainer extends GuiContainer {
    public BaseGuiContainer(Container container) {
        super(container);
    }

    public abstract String getBackgroundImageLocation();

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(EFlux.ModID.toLowerCase(), getBackgroundImageLocation()));
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
