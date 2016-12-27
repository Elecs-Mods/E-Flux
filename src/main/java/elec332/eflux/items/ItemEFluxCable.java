package elec332.eflux.items;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.api.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.loading.INoJsonItem;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 12-2-2016.
 */
public class ItemEFluxCable extends AbstractEFluxMultiPartItem implements INoJsonItem {

    public ItemEFluxCable() {
        super(null);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    private IBakedModelMetaMap<IBakedModel> models;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;

    @Override
    @SideOnly(Side.CLIENT) //TODO: Bug
    public void getSubItems(@Nonnull Item item, @Nonnull List subItems, CreativeTabs creativeTab) {
        for (int i = 0; i < 3; i++) {
            subItems.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + "." + stack.getItemDamage();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return models.forMeta(stack.getItemDamage());
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        models = new BakedModelMetaMap<IBakedModel>();
        for (int i = 0; i < textures.length; i++) {
            models.setModelForMeta(i, modelBakery.itemModelForTextures(textures[i]));
        }
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[3];
        textures[0] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/basicCable"));
        textures[1] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/normalCable"));
        textures[2] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/advancedCable"));
    }

}
