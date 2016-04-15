package elec332.eflux.items;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 30-1-2016.
 */
public class ItemManual extends EFluxItem implements INoJsonItem {

    public ItemManual() {
        super("manual");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.openGui(EFlux.instance, 3, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    private IItemModel model;

    @Override
    @SideOnly(Side.CLIENT)
    public IItemModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.itemModelForTextures(texture);
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/manual"));
    }

}
