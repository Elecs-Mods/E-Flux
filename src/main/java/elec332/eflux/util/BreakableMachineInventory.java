package elec332.eflux.util;

import com.google.common.base.Objects;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.Window;
import elec332.core.main.ElecCore;
import elec332.core.util.BasicInventory;
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
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class BreakableMachineInventory implements IInventory {

    public BreakableMachineInventory(IBreakableMachine tile, ItemStack s){
        this.i = tile;
        this.repairItem = s;
        this.inventoryContent = new ItemStack[]{
                ItemStackHelper.NULL_STACK
        };
    }

    private ItemStack[] inventoryContent;
    private IBreakableMachine i;
    private ItemStack repairItem;

    public ItemStack getRepairItem() {
        return repairItem;
    }

    public Window brokenGui() {
        return new Window() {

            IInventory fake = new BasicInventory("", 1);

            @Override
            protected void initWindow() {
                addWidget(new WidgetSlot(new InvWrapper(BreakableMachineInventory.this), 0, 66, 53));
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
                drawSlot(fS);
                GlStateManager.popMatrix();
            }

            @SideOnly(Side.CLIENT)
            private void drawSlot(Slot slotIn) {
                int i = slotIn.xPos;
                int j = slotIn.yPos;
                ItemStack itemstack = slotIn.getStack();
                String s = "";

                RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();

                itemRender.zLevel = 100.0F;

                GlStateManager.enableDepth();
                itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
                itemRender.renderItemOverlayIntoGUI(Objects.firstNonNull(itemstack.getItem().getFontRenderer(itemstack), elec332.core.client.RenderHelper.getMCFontrenderer()), itemstack, i, j, s);

                itemRender.zLevel = 0.0F;
            }


            @Override
            public ResourceLocation getBackgroundImageLocation() {
                return new EFluxResourceLocation("gui/GuiNull.png");
            }

        };
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return inventoryContent[slot];
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if(this.inventoryContent[p_70298_1_] != null) {
            ItemStack itemstack;
            if(this.inventoryContent[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.inventoryContent[p_70298_1_];
                this.inventoryContent[p_70298_1_] = ItemStackHelper.NULL_STACK;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.inventoryContent[p_70298_1_].splitStack(p_70298_2_);
                if(this.inventoryContent[p_70298_1_].stackSize == 0) {
                    this.inventoryContent[p_70298_1_] = ItemStackHelper.NULL_STACK;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return ItemStackHelper.NULL_STACK;
        }
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int p_70304_1_) {
        if(this.inventoryContent[p_70304_1_] != null) {
            ItemStack itemstack = this.inventoryContent[p_70304_1_];
            this.inventoryContent[p_70304_1_] = ItemStackHelper.NULL_STACK;
            return itemstack;
        } else {
            return ItemStackHelper.NULL_STACK;
        }
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, @Nonnull ItemStack p_70299_2_) {
        this.inventoryContent[p_70299_1_] = p_70299_2_;
        if(ItemStackHelper.isStackValid(p_70299_2_) && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
    @Nonnull
    public String getName() {
        return "BrokenMachine";
    }

    @Override
    public boolean hasCustomName() {
        return true;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    @Override
    @Nonnull
    @SuppressWarnings("all")
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer p_70300_1_) {
        return i.isBroken();
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        canFix();
    }

    @SuppressWarnings("all")
    public boolean canFix(){
        if (InventoryHelper.areEqualNoSize(inventoryContent[0], repairItem) && inventoryContent[0].stackSize == repairItem.stackSize) {
            i.setBroken(false);
            inventoryContent[0] = ItemStackHelper.NULL_STACK;
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, @Nonnull ItemStack p_94041_2_) {
        return InventoryHelper.areEqualNoSizeNoNBT(p_94041_2_, repairItem);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
}
