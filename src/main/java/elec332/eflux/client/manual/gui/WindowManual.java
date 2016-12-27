package elec332.eflux.client.manual.gui;

import elec332.core.client.util.GuiDraw;
import elec332.core.inventory.tooltip.ToolTip;
import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.core.inventory.window.Window;
import elec332.core.inventory.window.WindowManager;
import elec332.core.main.ElecCore;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.manual.ManualCategory;
import elec332.eflux.client.manual.ManualHandler;
import elec332.eflux.client.manual.ManualPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

/**
 * Created by Elec332 on 25-12-2016.
 */
public class WindowManual extends Window implements WidgetButton.IButtonEventListener {

    public WindowManual(){
        this(ManualHandler.instance.getMainCategory());
    }

    public WindowManual(ManualCategory category){
        super(222, 194);
        itemRender = Minecraft.getMinecraft().getRenderItem();
        fontRendererObj = elec332.core.client.RenderHelper.getMCFontrenderer();

        //DEBUG!
        if (category == ManualHandler.instance.getMainCategory()){
            category = null;
        }
        ManualHandler.instance.debug();

        currentCategory = category == null ? ManualHandler.instance.getMainCategory() : category;
    }

    public static ResourceLocation BACKGROUND;

    private final RenderItem itemRender;
    private final FontRenderer fontRendererObj;

    private final ManualCategory currentCategory;
    @SuppressWarnings("all")
    private List<ManualCategory> categories;
    private int page;

    private WidgetButton mainPage, previous, next;

    @Override
    protected void initWindow() {
        super.initWindow();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        categories = ManualHandler.instance.getCategoriesForPlayer(ElecCore.proxy.getClientPlayer(), currentCategory);

        for (int i = 0; i < categories.size(); i++) {
            ManualCategory category = categories.get(i);
            addWidget(new TabButton(i,  -30, 10 + (i * 40), category.getLocalisedName(), category.getDisplayStack()).addButtonEvent(this));
        }

        page = 0;

        int buttonWidth = 30;
        mainPage = addWidget(new WidgetButton((xSize/2 - buttonWidth/2), ySize - 10 - 19, buttonWidth, 20, this).setDisplayString("Main"));
        previous = addWidget(new WidgetButtonArrow(20, ySize - 10 - 19, WidgetButtonArrow.Direction.LEFT).addButtonEvent(this));
        next = addWidget(new WidgetButtonArrow(xSize - 20 - 12, ySize - 10 - 19, WidgetButtonArrow.Direction.RIGHT).addButtonEvent(this));

        mainPage.setActive(currentCategory != ManualHandler.instance.getMainCategory());
        previous.setActive(false);
        next.setActive(currentCategory.getPages().size() > 1);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //Todo? this.drawDefaultBackground();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)guiLeft + 3, (float)guiTop + 3, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        drawPage(mouseX - (guiLeft + 3), mouseY - (guiTop + 3));
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        elec332.core.client.RenderHelper.bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        //todo GuiDraw.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPage(int mouseX, int mouseY){
        List<ManualPage> pages = currentCategory.getPages();
        if (!(page < pages.size() && page > 0)){
            page = 0;
        }
        ManualPage page = pages.get(this.page);
        page.renderPage(xSize - 6, ySize - 30, mouseX, mouseY, this);
    }

    @Override
    public void onButtonClicked(WidgetButton button) {
        if (button instanceof TabButton && ((TabButton) button).buttonId >= 0 && ((TabButton) button).buttonId < categories.size()){
            switchTab(((TabButton) button).buttonId);
            return;
        }
        if (button == previous){
            currentCategory.getPages().get(page).onPageFlip(this);
            page--;
            if (page == 0){
                previous.setActive(false);
            }
            if (!next.isActive()){
                next.setActive(true);
            }
            currentCategory.getPages().get(page).onPageOpened(this);
        } else if (button == next){
            currentCategory.getPages().get(page).onPageFlip(this);
            page++;
            if (page == currentCategory.getPages().size() - 1){
                next.setActive(false);
            }
            if (!previous.isActive()){
                previous.setActive(true);
            }
            currentCategory.getPages().get(page).onPageOpened(this);
        } else if (button == mainPage){
            showCategory(ManualHandler.instance.getMainCategory());
        } else {
            currentCategory.getPages().get(page).onButtonPress(button);
        }
    }

    private void switchTab(int newTab){
        showCategory(categories.get(newTab));
    }

    private void showCategory(ManualCategory category){
        WindowManager.openClientWindow(new WindowManual(category));
    }

    @Override
    public boolean doesWindowPauseGame() {
        return false;
    }

    private class TabButton extends WidgetButton {

        public TabButton(int buttonId, int x, int y, String buttonText, ItemStack renderStack) {
            super(x, y, 34, 32);
            this.toolTip = new ToolTip(new ToolTip.ColouredString(buttonText)).setWidth(140);
            this.renderStack = renderStack;
            this.buttonId = buttonId;
        }

        private int buttonId;

        private final ToolTip toolTip;
        private final ItemStack renderStack;

        @Override
        public void draw(Window gui, int guiX, int guiY, int mouseX, int mouseY) {
            if (!this.isHidden()) {
                elec332.core.client.RenderHelper.bindTexture(BACKGROUND);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                boolean hovered = isMouseOver(mouseX, mouseY);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                GuiDraw.drawTexturedModalRect(guiX + this.x, guiY + this.y, 222, 0, this.width, this.height);
                int i = x + (width / 2) - 8;
                int j = y + (height / 2) - 8;
                if (ItemStackHelper.isStackValid(renderStack)) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableGUIStandardItemLighting();
                    itemRender.zLevel = 200.0F;
                    FontRenderer font = renderStack.getItem().getFontRenderer(renderStack);
                    if (font == null) {
                        font = fontRendererObj;
                    }
                    itemRender.renderItemAndEffectIntoGUI(renderStack, guiX + i, guiY + j);
                    itemRender.renderItemOverlayIntoGUI(font, renderStack, guiX + i, guiY + j, null);
                    itemRender.zLevel = 0.0F;
                    GlStateManager.popMatrix();
                }
                if (hovered){
                    //toolTip.renderTooltip(x + mouseX, y + mouseY, x, y);
                }
            }
        }

        @Override
        public ToolTip getToolTip() {
            return toolTip;
        }

    }

    static {
        BACKGROUND = new EFluxResourceLocation("gui/emptyGui.png");
    }

}
