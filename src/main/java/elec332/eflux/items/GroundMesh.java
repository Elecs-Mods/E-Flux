package elec332.eflux.items;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.util.DustPile;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class GroundMesh extends EFluxItem implements INoJsonItem {

    public GroundMesh() {
        super("GroundMesh");
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedToolTips) {
        super.addInformation(stack, player, tooltip, advancedToolTips);
        if (!(stack == null || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null)) {
            DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
            if (isValidMesh(stack)) {
                for (GrinderRecipes.OreDictStack dustPart : dustPile.getContent()) {
                    tooltip.add(dustPart.name + ": " + dustPart.amount);
                }
            } else {
                tooltip.add("Total: " + dustPile.getSize());
            }
        }
    }

    public static boolean isValidMesh(ItemStack stack){
        return !(stack == null || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null) && stack.getTagCompound().getBoolean("dusts_scanned");
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
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/groundMesh"));
    }

}
