package elec332.eflux.client.inventory;

import elec332.eflux.inventory.TEGrinderContainer;
import elec332.eflux.tileentity.TEGrinder;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 4-4-2015.
 */
public class GuiInventoryGrinder extends BaseGuiContainer {
    public GuiInventoryGrinder(TEGrinder grinder, EntityPlayer player) {
        super(new TEGrinderContainer(grinder, player));
        this.ySize = 234;
    }

    @Override
    public String getBackgroundImageLocation() {
        return "gui/stolenimagefromCTIV.png";
    }

    /*@Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {


        /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        bindTexture(beaconGuiTextures);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        int left = this.guiLeft;
        int top = this.guiTop;

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(left, top, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240 / 1.0F, 240 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
        GL11.glPopAttrib();

        bindTexture(beaconGuiTextures);

        /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(beaconGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //int k = mc.renderEngine.getTexture();
        mc.renderEngine.bindTexture(new ResourceLocation(EFlux.ModID.toLowerCase(), "gui/stolenimagefromCTIV.png"));
        int l = guiLeft;
        int i1 = guiTop;
        //Background
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        int j1 = l + 155;
        int k1 = i1 + 17;
        int l1 = k1 + 88 + 2;
        //Scrolly bar
        drawTexturedModalRect(l + 154, i1 + 17 + (int)((float)(l1 - k1 - 17) * 0), 0, 240, 16, 16);
        /*GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(EFlux.ModID.toLowerCase(), "gui/stolenimagefromCTIV.png"));
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);*/
/*
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(EFlux.ModID.toLowerCase(), "gui/stolenimagefromCTIV.png"));
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    protected void bindTexture(ResourceLocation texturePath) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texturePath);
    }*/
}
