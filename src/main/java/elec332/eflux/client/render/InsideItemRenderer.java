package elec332.eflux.client.render;

import elec332.eflux.util.RIWInventory;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

/**
 * Created by Elec332 on 5-9-2015.
 */
public class InsideItemRenderer extends TileEntitySpecialRenderer{

    public InsideItemRenderer(){
        //RenderItem.getInstance().setRenderManager(RenderManager.instance);
        this.renderDummy = new EntityItem(null);
    }

    private final EntityItem renderDummy;

    private static final double unit = 0.25D/8;//(1/16.0F)/2;

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f, int what) {
        RIWInventory inventory = null;//((TileEntityInsideItemRenderer)tile).getInventory();
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) != null)
                renderItemStack(inventory.getStackInSlot(i), x, y, z, iFromS(i), sFromS(i));
        }
        /*renderItemStack(new ItemStack(Items.speckled_melon, 12), x, y, z, 1);
        renderItemStack(new ItemStack(Items.blaze_powder, 4), x, y, z, 2);
        renderItemStack(new ItemStack(Items.cooked_chicken, 16), x, y, z, 3);
        renderItemStack(new ItemStack(Blocks.iron_block, 2), x, y, z, 4);

        for (int i = 0; i < 16; i++) {
            GL11.glPushMatrix();GL11.glTranslatef(0, 0, 0.5F + (float) unit);
            renderItemStack(new ItemStack(Items.stick), x + 0.25D, y + i * (1.0D / 16D), z, i);

            GL11.glPopMatrix();
        }
        for (int i = 0; i < 16; i++) {
            GL11.glPushMatrix();GL11.glTranslatef(0, 0, (float)unit);
            renderItemStack(new ItemStack(Items.stick), x + 0.25D, y + i * (1.0D/16D), z, i);

            GL11.glPopMatrix();
        }
        for (int i = 0; i < 16; i++) {
            renderItemStack(new ItemStack(Items.stick), x + 0.75D, y + i * (1.0D/16D), z + 0.5D + unit, i);
        }
        for (int i = 0; i < 16; i++) {
            renderItemStack(new ItemStack(Items.stick), x + 0.75D, y + i * (1.0D/16D), z + unit, i);
        }
/*
        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.25D, y + 2D + quarterBlock, z + 0.25D);
        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.25D, y + 2.5D + quarterBlock, z + 0.25D);

        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.25D, y + 2D + quarterBlock, z + 0.75D);
        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.25D, y + 2.5D + quarterBlock, z + 0.75D);

        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.75D, y + 2D + quarterBlock, z + 0.25D);
        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.75D, y + 2.5D + quarterBlock, z + 0.25D);

        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.75D, y + 2D + quarterBlock, z + 0.75D);
        renderItem(new ItemStack(BlockRegister.frameAdvanced.block), x + 0.75D, y + 2.5D + quarterBlock, z + 0.75D);*/
    }

    private int iFromS(int q){
        if (q < 16){
            return 1;
        } else if (q < 32){
            return 2;
        } else if (q < 48){
            return 3;
        } else {
            return 4;
        }
    }

    private int sFromS(int q){
        return q - ((iFromS(q)-1) * 16);
    }

    private void renderItemStack(ItemStack stack, double x, double y, double z, int slot, int i){
        //GL11.glPushMatrix();
        i++;
        boolean b = false;
        final ItemStack stack1 = new ItemStack(stack.getItem(), 1);
        //int i = stack.stackSize;
        x += 0.25D;
        if (slot > 2){
            x += 0.5D;
        }
        if (slot == 1 || slot == 3){
            z += 0.5D;
        }
        if (stack.getItem() instanceof ItemBlock){
            z += 0.25D;
            b = true;
            if (i > 2)
                i = 2;
        } else {
            z += unit;
            y += -(1.0D/16)+(1.0D/32);
        }
        //for (int j = 0; j < i; j++) {
            if (b){
                y += 0.5D * i;
            } else {
                y += (1.0D/16D)*i;
            }
            renderItem(stack1, x, y, z, b);
        //}

        /*GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);

        renderDummy.setEntityItemStack(stack);
        RenderItem.renderInFrame = true;
        RenderManager.instance.renderEntityWithPosYaw(renderDummy, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;
        GL11.glPopMatrix();
        GL11.glPopMatrix();*/
    }

    private void renderItem(ItemStack stack, double x, double y, double z, boolean b){
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glPushMatrix();
        if (!b)
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        //if (b)
        //    GL11.glRotatef(90F, -1, 0F, 0F);
        renderDummy.setEntityItemStack(stack);
        //RenderItem.renderInFrame = true;
        //Minecraft.getMinecraft().entityRenderer.itemRenderer.itemRenderer..renderEntityWithPosYaw(renderDummy, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        //RenderItem.renderInFrame = false;
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

}
