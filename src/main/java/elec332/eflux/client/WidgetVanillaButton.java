package elec332.eflux.client;

import elec332.core.client.RenderHelper;
import elec332.core.inventory.widget.WidgetButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by Elec332 on 14-5-2016.
 */
public class WidgetVanillaButton extends WidgetButton {

    public WidgetVanillaButton(int x, int y, int width, int height) {
        super(x, y, 0, 0, width, height);
    }

    public WidgetVanillaButton(int x, int y, int width, int height, IButtonEvent... events) {
        super(x, y, 0, 0, width, height, events);
    }

    @Override
    public void draw(Gui gui, int guiX, int guiY, int mouseX, int mouseY) {
        if (!this.isHidden()) {
            FontRenderer fontrenderer = RenderHelper.getMCFontrenderer();
            bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            boolean hovered = isMouseOver(guiX, guiY);
            int i = this.getHoverState(hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            gui.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            gui.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            int j = 14737632;
            if (!this.isActive()) {
                j = 10526880;
            } else if (hovered) {
                j = 16777120;
            }
            gui.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }

    }
}

