package elec332.eflux.util;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.Window;
import elec332.core.main.ElecCore;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.InventoryHelper;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class BreakableMachineInventory extends BasicItemHandler {

    public BreakableMachineInventory(IBreakableMachine tile, ItemStack s){
        super(1);
        this.i = tile;
        this.repairItem = s;
    }

    private IBreakableMachine i;
    private ItemStack repairItem;

    public ItemStack getRepairItem() {
        return repairItem;
    }

    public Window brokenGui() {
        return new Window() {

            IInventory fake = new InventoryBasic("<FAKE>", false, 1);

            @Override
            protected void initWindow() {
                addWidget(new WidgetSlot(BreakableMachineInventory.this, 0, 66, 53));
                addPlayerInventoryToContainer();
            }

            @Nonnull
            @Override
            public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
                if (!player.getEntityWorld().isRemote) {
                    ElecCore.tickHandler.registerCall(new Runnable() {
                        @Override
                        public void run() {
                            if (canFix()) {
                                player.closeScreen();
                            }
                        }
                    }, player.getEntityWorld());
                }
                return super.slotClick(slotId, dragType, clickTypeIn, player);
            }

            @Override
            @SideOnly(Side.CLIENT)
            protected void drawScreen(int mouseX, int mouseY, float partialTicks) {
                fake.setInventorySlotContents(0, getRepairItem());
                Slot fS = new Slot(fake, 0, 66, 53);
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.pushMatrix();
                int i = this.guiLeft;
                int j = this.guiTop;
                GlStateManager.translate((float) i, (float) j, 0.0F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.enableRescaleNormal();
                int k = 240;
                int l = 240;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k / 1.0F, (float) l / 1.0F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                drawSlot(fS, 66, 53);
                GlStateManager.popMatrix();
            }

            @SideOnly(Side.CLIENT)
            private void drawSlot(Slot slotIn, int i, int j) {
                ItemStack itemstack = slotIn.getStack();
                if (!ItemStackHelper.isStackValid(itemstack)){
                    return;
                }
                String s = "";

                RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

                itemRender.zLevel = 100.0F;

                GlStateManager.enableDepth();
                itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
                itemRender.renderItemOverlayIntoGUI(MoreObjects.firstNonNull(itemstack.getItem().getFontRenderer(itemstack), elec332.core.client.RenderHelper.getMCFontrenderer()), itemstack, i, j, s);

                itemRender.zLevel = 0.0F;
            }

            @Override
            public void onWindowClosed(EntityPlayer playerIn) {
                super.onWindowClosed(playerIn);
                canFix();
            }

            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new EFluxResourceLocation("gui/GuiNull.png");
            }

        };
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack ret = super.insertItem(slot, stack, simulate);
        canFix();
        return ret;
    }

    @Override
    public boolean canInsert(int slot, @Nonnull ItemStack stack) {
        return InventoryHelper.areEqualNoSize(stack, repairItem);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack ret = super.extractItem(slot, amount, simulate);
        canFix();
        return ret;
    }

    @SuppressWarnings("all")
    public boolean canFix(){
        if (InventoryHelper.areEqualNoSize(getStackInSlot(0), repairItem) && getStackInSlot(0).stackSize == repairItem.stackSize) {
            i.setBroken(false);
            setStackInSlot(0, ItemStackHelper.NULL_STACK);
            return true;
        }
        return false;
    }

}
