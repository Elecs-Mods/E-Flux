package elec332.eflux.client.manual.pages;

import com.google.common.collect.Lists;
import elec332.core.multiblock.BlockStructure;
import elec332.core.world.location.BlockStateWrapper;
import elec332.eflux.client.manual.gui.GuiManual;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Elec332 on 31-1-2016.
 */
public class PageMultiBlockStructure extends AbstractManualPage {

    public PageMultiBlockStructure(String unLocalisedTitle, BlockStructure structure, int startX, int startY){
        this(unLocalisedTitle, structure, startX, startY, new IMultiBlockStructureManualHelper.DefaultImpl());
    }

    public PageMultiBlockStructure(String unLocalisedTitle, BlockStructure structure, int startX, int startY, IMultiBlockStructureManualHelper manualData){
        super();
        setUnLocalisedTitle(unLocalisedTitle);
        this.multiBlockStructure = structure;
        this.itemRender = Minecraft.getMinecraft().getRenderItem();
        this.fontRendererObj = elec332.core.client.RenderHelper.getMCFontrenderer();
        this.max = structure.getHeight() * structure.getWidth() * structure.getLength();
        this.startX = startX;
        this.startY = startY;
        this.manualData = manualData;
        this.blocks = this.max;
    }

    private final BlockStructure multiBlockStructure;
    private final RenderItem itemRender;
    private final FontRenderer fontRendererObj;
    private final int max;
    private static final BlockStateWrapper air;

    private long time = 0L;
    private int blocks = 0;

    private final int startX, startY;

    private final IMultiBlockStructureManualHelper manualData;

    @Override
    public void renderBody(int width, int height, int mouseX, int mouseY, GuiManual manual) {
        if (time == 0L){
            time = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - time > 800){
            time = 0;
            blocks++;
            if (blocks > max){
                blocks = 0;
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        int lengthFactorX = 12, lengthFactorY = 7;
        int widthFactorX = 9, widthFactorY = 9;
        int heightFactor = 12;

        ItemStack mouseOver = null;
        List<String> note = null;

        //int startX = (width/2) + ((((lengthFactorX * (multiBlockStructure.getLength()- 1))) + widthFactorX * (multiBlockStructure.getWidth() - 1)) / 2);
        //int startY = (height/2) + (((lengthFactorY * (multiBlockStructure.getLength() - 1)) + (widthFactorY * (multiBlockStructure.getWidth() - 1) + (heightFactor * (multiBlockStructure.getHeight() - 1)))) / 2);

        int pass = 0;
        render:
        for (int k = 0; k < multiBlockStructure.getHeight(); k++) {
            for (int i = multiBlockStructure.getLength() - 1; i > -1 ; i--) {
                for (int j = multiBlockStructure.getWidth() - 1; j > -1 ; j--) {
                    BlockStateWrapper block = multiBlockStructure.getStructure()[i][j][k];
                    if (!block.blocksEqual(air)){
                        pass++;
                        if (pass < blocks) {
                            ItemStack renderStack = manualData.getRenderingStack(i, j, k, block);
                            if (renderStack != null) {

                                int x = startX + (lengthFactorX * i) - (widthFactorX * j);
                                int y = startY - (heightFactor * k) - (widthFactorY * j) - (lengthFactorY * i);

                                FontRenderer font = renderStack.getItem().getFontRenderer(renderStack);
                                if (font == null) {
                                    font = fontRendererObj;
                                }
                                itemRender.renderItemAndEffectIntoGUI(renderStack, x, y);
                                itemRender.renderItemOverlayIntoGUI(font, renderStack, x, y, null);
                                if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                                    mouseOver = renderStack;
                                    note = Lists.newArrayList();
                                    manualData.getNote(i, j, k, note);
                                }
                            }
                        } else {
                            break render;
                        }
                    }
                }
            }
        }

        if (mouseOver != null){
            renderToolTip(mouseOver, mouseX, mouseY, manual, note);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableLighting();
        RenderHelper.disableStandardItemLighting();
    }

    public interface IMultiBlockStructureManualHelper{

        public ItemStack getRenderingStack(int length, int width, int height, BlockStateWrapper original);

        public void getNote(int length, int width, int height, List<String> noteList);

        public static class DefaultImpl implements IMultiBlockStructureManualHelper {

            @Override
            public ItemStack getRenderingStack(int length, int width, int height, BlockStateWrapper original) {
                ItemStack renderStack = original.toItemStack();
                if (renderStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
                    renderStack.setItemDamage(0);
                }
                return renderStack;
            }

            @Override
            public void getNote(int length, int width, int height, List<String> list) {
            }

        }

    }

    protected void renderToolTip(ItemStack stack, int x, int y, GuiManual manual, List<String> note) {
        List<String> list = stack.getTooltip(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            } else {
                list.set(i, TextFormatting.GRAY + list.get(i));
            }
        }
        if (note != null && !note.isEmpty()) {
            list.add("");
            list.add("Note"+(note.size() == 1 ? "" : "s")+":");
            list.addAll(note);
        }
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        manual.drawHoveringText(list, x, y, (font == null ? fontRendererObj : font));
    }

    static {
        air = new BlockStateWrapper((Block)null);
    }

}
