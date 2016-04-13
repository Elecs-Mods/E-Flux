package elec332.eflux.util;

import elec332.core.inventory.BaseContainer;
import elec332.core.main.ElecCore;
import elec332.core.util.BasicInventory;
import elec332.core.util.InventoryHelper;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.inventory.GuiStandardFormat;
import elec332.eflux.inventory.ContainerSingleSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class BreakableMachineInventory implements IInventory{

    public BreakableMachineInventory(IBreakableMachine tile, ItemStack s){
        this.i = tile;
        this.repairItem = s;
    }

    private ItemStack[] inventoryContent = new ItemStack[1];
    private IBreakableMachine i;
    private ItemStack repairItem;

    public ItemStack getRepairItem() {
        return repairItem;
    }

    public Object brokenGui(final Side side, EntityPlayer player){
        BaseContainer container = new ContainerSingleSlot(this, player){

            @Override
            public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, final EntityPlayer player) {
                if (side.isServer()) {
                    ElecCore.tickHandler.registerCall(new Runnable() {
                        @Override
                        public void run() {
                            if (canFix()) {
                                player.closeScreen();
                            }
                        }
                    }, player.worldObj);
                }
                return super.slotClick(slotId, dragType, clickTypeIn, player);
            }



        };
        if (side==Side.CLIENT)
            return new GuiStandardFormat(container, new EFluxResourceLocation("gui/GuiNull.png")){

                IInventory fake = new BasicInventory("", 1);

                @Override
                public void drawScreen(int mouseX, int mouseY, float f) {
                    fake.setInventorySlotContents(0, getRepairItem());
                    Slot slot = container.getSlot(0);
                    Slot fS = new Slot(fake, 0, slot.xDisplayPosition, slot.yDisplayPosition);
                    RenderHelper.enableGUIStandardItemLighting();
                    GlStateManager.pushMatrix();
                    int i = this.guiLeft;
                    int j = this.guiTop;
                    GlStateManager.translate((float)i, (float)j, 0.0F);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.enableRescaleNormal();
                    this.theSlot = null;
                    int k = 240;
                    int l = 240;
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k / 1.0F, (float) l / 1.0F);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    drawSlot(fS);
                    GlStateManager.popMatrix();
                    super.drawScreen(mouseX, mouseY, f);
                }

                private void drawSlot(Slot slotIn)
                {
                    int i = slotIn.xDisplayPosition;
                    int j = slotIn.yDisplayPosition;
                    ItemStack itemstack = slotIn.getStack();
                    boolean flag = false;
                    boolean flag1 = slotIn == this.clickedSlot && this.draggedStack != null && !this.isRightMouseClick;
                    ItemStack itemstack1 = this.mc.thePlayer.inventory.getItemStack();
                    String s = null;

                    if (slotIn == this.clickedSlot && this.draggedStack != null && this.isRightMouseClick && itemstack != null)
                    {
                        itemstack = itemstack.copy();
                        itemstack.stackSize /= 2;
                    }
                    else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && itemstack1 != null)
                    {
                        if (this.dragSplittingSlots.size() == 1)
                        {
                            return;
                        }

                        if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn))
                        {
                            itemstack = itemstack1.copy();
                            flag = true;
                            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack() == null ? 0 : slotIn.getStack().stackSize);

                            if (itemstack.stackSize > itemstack.getMaxStackSize())
                            {
                                s = TextFormatting.YELLOW + "" + itemstack.getMaxStackSize();
                                itemstack.stackSize = itemstack.getMaxStackSize();
                            }

                            if (itemstack.stackSize > slotIn.getItemStackLimit(itemstack))
                            {
                                s = TextFormatting.YELLOW + "" + slotIn.getItemStackLimit(itemstack);
                                itemstack.stackSize = slotIn.getItemStackLimit(itemstack);
                            }
                        }
                        else
                        {
                            this.dragSplittingSlots.remove(slotIn);
                            this.updateDragSplitting();
                        }
                    }

                    this.zLevel = 100.0F;
                    this.itemRender.zLevel = 100.0F;

                    if (itemstack == null)
                    {
                        TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

                        if (textureatlassprite != null)
                        {
                            GlStateManager.disableLighting();
                            this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                            this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                            GlStateManager.enableLighting();
                            flag1 = true;
                        }
                    }

                    if (!flag1)
                    {
                        if (flag)
                        {
                            drawRect(i, j, i + 16, j + 16, -2130706433);
                        }

                        GlStateManager.enableDepth();
                        this.itemRender.renderItemAndEffectIntoGUI(itemstack, i, j);
                        this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, itemstack, i, j, s);
                    }

                    this.itemRender.zLevel = 0.0F;
                    this.zLevel = 0.0F;
                }

                private void updateDragSplitting()
                {
                    ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

                    if (itemstack != null && this.dragSplitting)
                    {
                        this.dragSplittingRemnant = itemstack.stackSize;

                        for (Slot slot : this.dragSplittingSlots)
                        {
                            ItemStack itemstack1 = itemstack.copy();
                            int i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
                            Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);

                            if (itemstack1.stackSize > itemstack1.getMaxStackSize())
                            {
                                itemstack1.stackSize = itemstack1.getMaxStackSize();
                            }

                            if (itemstack1.stackSize > slot.getItemStackLimit(itemstack1))
                            {
                                itemstack1.stackSize = slot.getItemStackLimit(itemstack1);
                            }

                            this.dragSplittingRemnant -= itemstack1.stackSize - i;
                        }
                    }
                }

            };
        return container;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventoryContent[slot];
    }

    @Override
    public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
        if(this.inventoryContent[p_70298_1_] != null) {
            ItemStack itemstack;
            if(this.inventoryContent[p_70298_1_].stackSize <= p_70298_2_) {
                itemstack = this.inventoryContent[p_70298_1_];
                this.inventoryContent[p_70298_1_] = null;
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.inventoryContent[p_70298_1_].splitStack(p_70298_2_);
                if(this.inventoryContent[p_70298_1_].stackSize == 0) {
                    this.inventoryContent[p_70298_1_] = null;
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int p_70304_1_) {
        if(this.inventoryContent[p_70304_1_] != null) {
            ItemStack itemstack = this.inventoryContent[p_70304_1_];
            this.inventoryContent[p_70304_1_] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_) {
        this.inventoryContent[p_70299_1_] = p_70299_2_;
        if(p_70299_2_ != null && p_70299_2_.stackSize > this.getInventoryStackLimit()) {
            p_70299_2_.stackSize = this.getInventoryStackLimit();
        }
        this.markDirty();
    }

    @Override
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
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return i.isBroken();
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {
        canFix();
    }

    public boolean canFix(){
        if (InventoryHelper.areEqualNoSize(inventoryContent[0], repairItem) && inventoryContent[0].stackSize == repairItem.stackSize) {
            i.setBroken(false);
            inventoryContent[0] = null;
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
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
