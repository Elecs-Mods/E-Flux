package elec332.eflux.client.manual.pages;

import elec332.core.client.RenderHelper;
import elec332.eflux.client.ClientHelper;
import elec332.eflux.client.manual.ManualPage;
import elec332.eflux.client.manual.gui.WindowManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by Elec332 on 31-1-2016.
 */
public abstract class AbstractManualPage extends ManualPage {

    public AbstractManualPage(){
    }

    private String unLocalisedName;

    public AbstractManualPage setUnLocalisedTitle(String s){
        this.unLocalisedName = s;
        return this;
    }

    public String getUnLocalisedTitle(){
        return unLocalisedName;
    }

    @Override
    public final void renderPage(int width, int height, int mouseX, int mouseY, WindowManual manual) {
        FontRenderer fr = RenderHelper.getMCFontrenderer();
        int oldHeight = fr.FONT_HEIGHT;
        fr.FONT_HEIGHT = 32;
        String text = ClientHelper.translateToLocal(unLocalisedName);
        fr.drawString(TextFormatting.BOLD + text, (width/2) - (fr.getStringWidth(text) / 2), 8, 0);
        fr.FONT_HEIGHT = oldHeight;
        renderBody(width, height, mouseX, mouseY, manual);
    }

    public abstract void renderBody(int width, int height, int mouseX, int mouseY, WindowManual manual);

}
