package elec332.eflux.client.manual.gui;

import elec332.core.inventory.tooltip.ToolTip;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.manual.ManualCategory;
import elec332.eflux.client.manual.ManualHandler;
import elec332.eflux.client.manual.ManualPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.List;

/**
 * Created by Elec332 on 30-1-2016.
 */
public class GuiManual extends GuiScreen {

    public GuiManual(){
        this(ManualHandler.instance.getMainCategory());
    }

    public GuiManual(ManualCategory category){
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

    public int xSize = 222;
    public int ySize = 194;
    public int guiLeft;
    public int guiTop;
    private final RenderItem itemRender;
    private final FontRenderer fontRendererObj;

    private final ManualCategory currentCategory;
    @SuppressWarnings("all")
    private List<ManualCategory> categories;
    private int page;

    private GuiButton mainPage, previous, next;

    @Override
    public void initGui() {
        super.initGui();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        categories = ManualHandler.instance.getCategoriesForPlayer(Minecraft.getMinecraft().thePlayer, currentCategory);

        for (int i = 0; i < categories.size(); i++) {
            ManualCategory category = categories.get(i);
            buttonList.add(new TabButton(i, guiLeft - 30, guiTop + 10 + (i * 40), category.getLocalisedName(), category.getDisplayStack()));
        }

        page = 0;

        int buttonWidth = 30;
        buttonList.add(mainPage = new GuiButton(-1, guiLeft + (xSize/2 - buttonWidth/2), guiTop + ySize - 10 - 19, buttonWidth, 20, "Main"));
        buttonList.add(previous = new MerchantButton(-1, guiLeft + 20, guiTop + ySize - 10 - 19, false));
        buttonList.add(next = new MerchantButton(-1, guiLeft + xSize - 20 - 12, guiTop + ySize - 10 - 19, true));

        mainPage.enabled = currentCategory != ManualHandler.instance.getMainCategory();
        previous.enabled = false;
        next.enabled = currentCategory.getPages().size() > 1;

        //buttonList.add(new TabButton(0, guiLeft - 30, guiTop + 5, "testText", new ItemStack(Items.apple)));
       // buttonList.add(new TabButton(1, guiLeft - 30, guiTop + 45, "button2", BlockRegister.powerInlet.toItemStack()));
       // buttonList.add(new TabButton(2, guiLeft - 30, guiTop + 85, "txt4", ItemRegister.compressedIngot));
      //  buttonList.add(new TabButton(3, guiLeft - 30, guiTop + 125, "txt4", new ItemStack(Items.nether_star)));
    }

    public List<GuiButton> getButtonList(){
        return buttonList;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)guiLeft + 3, (float)guiTop + 3, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        drawPage(mouseX - (guiLeft + 3), mouseY - (guiTop + 3));
        GlStateManager.popMatrix();
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
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id >= 0 && button.id < categories.size()){
            switchTab(button.id);
        }
        if (button == previous){
            currentCategory.getPages().get(page).onPageFlip(this);
            page--;
            if (page == 0){
                previous.enabled = false;
            }
            if (!next.enabled){
                next.enabled = true;
            }
            currentCategory.getPages().get(page).onPageOpened(this);
        } else if (button == next){
            currentCategory.getPages().get(page).onPageFlip(this);
            page++;
            if (page == currentCategory.getPages().size() - 1){
                next.enabled = false;
            }
            if (!previous.enabled){
                previous.enabled = true;
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
        Minecraft.getMinecraft().displayGuiScreen(new GuiManual(category));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private class TabButton extends GuiButton {

        public TabButton(int buttonId, int x, int y, String buttonText, ItemStack renderStack) {
            super(buttonId, x, y, 34, 32, buttonText);
            this.toolTip = new ToolTip(new ToolTip.ColouredString(buttonText));
            this.renderStack = renderStack;
        }

        private final ToolTip toolTip;
        private final ItemStack renderStack;

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(BACKGROUND);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.blendFunc(770, 771);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 222, 0, this.width, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
                int i = xPosition + (width/2) - 8;
                int j = yPosition + (height/2) - 8;
                if (renderStack != null && renderStack.getItem() != null) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    RenderHelper.enableGUIStandardItemLighting();
                    this.zLevel = 200.0F;
                    itemRender.zLevel = 200.0F;
                    FontRenderer font = renderStack.getItem().getFontRenderer(renderStack);
                    if (font == null) {
                        font = fontRendererObj;
                    }
                    itemRender.renderItemAndEffectIntoGUI(renderStack, i, j);
                    itemRender.renderItemOverlayIntoGUI(font, renderStack, i, j, null);
                    this.zLevel = 0.0F;
                    itemRender.zLevel = 0.0F;
                    GlStateManager.popMatrix();
                }
                if (hovered){
                    toolTip.renderTooltip(xPosition + mouseX, yPosition + mouseY, xPosition, yPosition);
                }
            }
        }
    }

    private static class MerchantButton extends GuiButton {

        public MerchantButton(int buttonID, int x, int y, boolean right) {
            super(buttonID, x, y, 12, 19, "");
            this.left = right;
        }

        private final boolean left;

        private static final ResourceLocation MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");

        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = 0;
                int j = 176;

                if (!this.enabled) {
                    j += this.width * 2;
                } else if (flag) {
                    j += this.width;
                }

                if (!this.left) {
                    i += this.height;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
            }
        }
    }

    @Override
    public void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
        super.drawHoveringText(textLines, x, y, font);
    }

    static {
        BACKGROUND = new EFluxResourceLocation("gui/emptyGui.png");
    }

}
