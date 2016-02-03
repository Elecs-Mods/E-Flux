package elec332.eflux.client.manual.pages;

import com.google.common.base.Strings;
import elec332.core.client.RenderHelper;
import elec332.eflux.client.ClientHelper;
import elec332.eflux.client.manual.ManualCategory;
import elec332.eflux.client.manual.gui.GuiManual;

/**
 * Created by Elec332 on 31-1-2016.
 */
public class PageText extends AbstractManualPage {

    public static PageText newPageWithText(String unLocalisedText){
        return new PageText().setText(unLocalisedText);
    }

    public PageText(){
        this(null);
        setUseCategoryTitle(true);
    }

    public PageText(String unLocalisedTitle){
        super();
        setUnLocalisedTitle(unLocalisedTitle);
    }

    private String text;
    private boolean unLocalised, catTitle;

    @Override
    protected void onAddedToCategory(ManualCategory category) {
        if (Strings.isNullOrEmpty(getUnLocalisedTitle()) && catTitle){
            setUnLocalisedTitle(category.getUnLocalisedName());
        }
    }

    public PageText setUseCategoryTitle(boolean b){
        catTitle = b;
        return this;
    }

    public PageText setText(String unLocalisedText){
        return setText(unLocalisedText, true);
    }

    public PageText setText(String text, boolean unLocalised){
        this.text = text;
        this.unLocalised = unLocalised;
        return this;
    }

    @Override
    public void renderBody(int width, int height, int mouseX, int mouseY, GuiManual manual) {
        if (!Strings.isNullOrEmpty(text)) {
            String text = unLocalised ? ClientHelper.translateToLocal(this.text) : this.text;
            RenderHelper.getMCFontrenderer().drawSplitString(text, 8, 20, width - 8, 0);
        }
    }

}
