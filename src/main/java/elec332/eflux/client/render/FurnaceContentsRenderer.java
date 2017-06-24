package elec332.eflux.client.render;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.ITextureLoader;
import elec332.core.client.RenderHelper;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.tesselator.ElecTessellator;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.client.FurnaceRenderTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.opengl.GL11;

/**
 * Created by Elec332 on 5-9-2015.
 */
public class FurnaceContentsRenderer extends TileEntitySpecialRenderer<FurnaceRenderTile> implements ITextureLoader{

    public FurnaceContentsRenderer(){
        this.renderDummy = new EntityItem(null);
        RenderingRegistry.instance().registerLoader(this);
    }

    private final EntityItem renderDummy;
    private TextureAtlasSprite texture;
    //private static final double unit = 0.25D/8;//(1/16.0F)/2;
    //private static final double specialY = (1.0D/16)+(1.0D/32);

    @Override
    public void render(FurnaceRenderTile tile, double x, double y, double z, float f, int what, float alpha) {
        IItemHandler inventory = tile.getInv();
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (ItemStackHelper.isStackValid(inventory.getStackInSlot(i))) {
                renderItemStack(inventory.getStackInSlot(i), x, y, z, i, i < 4 ? 0 : 1);
            }
        }
        ((ElecTessellator)RenderHelper.getTessellator()).startDrawingWorldBlock();

        RenderHelper.getBlockRenderer().renderFaceYPos(tile.getPos().getX() + .5, tile.getPos().getY() + .5 + 4, tile.getPos().getZ(), texture);
        RenderHelper.getBlockRenderer().renderFaceYNeg(tile.getPos().getX() + .5, tile.getPos().getY() + .5 + 4, tile.getPos().getZ(), texture);

        RenderHelper.getBlockRenderer().renderFaceYPos(x + .5, y + .5 + 4, z, texture);
        RenderHelper.getBlockRenderer().renderFaceYNeg(x + .5, y + .5 + 4, z, texture);

        RenderHelper.getTessellator().getMCTessellator().draw();
    }

    private void renderItemStack(ItemStack stack, double x, double y, double z, int slot, int i){
        //i++;
       // boolean b = false;
        slot -= 4 * i;
        y += i * .5;
        x += 0.25D;
        if (slot > 1){
            x += 0.5D;
        }
        if (slot == 1 || slot == 3){
            z += 0.5D;
        }
        z += 0.25D;
        if (stack.getItem() instanceof ItemBlock){
            y -= .2D;
        } else {
             y -= .1D;
        }

    /*        b = true;
            if (i > 2)
                i = 2;
        } else {
            z += unit;
            y += -specialY;
        }
        if (b){
            y += 0.5D * i;
        } else {
            y += (1.0D/16D)*i;
        }*/
        renderItem(stack, x, y, z, true);
    }

    private void renderItem(ItemStack stack, double x, double y, double z, boolean b){
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glPushMatrix();
        if (!b)
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        renderDummy.setItem(stack);
        re.doRender(renderDummy, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    private final RenderEntityItem re = new RenderEntityItem(Minecraft.getMinecraft().getRenderManager(), Minecraft.getMinecraft().renderItem){
        @Override
        protected int getModelCount(ItemStack stack) {
            return 1;
        }
    };

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/default_side"));
    }

}
