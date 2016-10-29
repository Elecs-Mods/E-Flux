package elec332.eflux.items;

import com.google.common.collect.Lists;
import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.api.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.loading.INoJsonItem;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.eflux.EFlux;
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

import java.util.List;

/**
 * Created by Elec332 on 17-5-2015.
 */
public class ItemEFluxGenerics extends Item implements INoJsonItem {

    public ItemEFluxGenerics(){
        this.setHasSubtypes(true);
        setRegistryName(new EFluxResourceLocation(getName()));
        setUnlocalizedName(getRegistryName().toString().replace(":", ".").toLowerCase());
        setCreativeTab(EFlux.creativeTab);
    }

    protected String[] components = {
            "carbonPlate", "scrap", "carbonMesh", "plantMesh"
    };

    public List<String> getComponents(){
        return Lists.newArrayList(components);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;
    @SideOnly(Side.CLIENT)
    private IBakedModelMetaMap<IBakedModel> models;

    protected String getName(){
        return "genericItems";
    }

    protected String getTextureName(int meta){
        return components[meta];
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack)+"."+components[stack.getItemDamage()].toLowerCase();
    }

    @Override
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list){
        for (int i = 0; i < components.length; i++){
            list.add(new ItemStack(item, 1, i));
        }
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
        models = new BakedModelMetaMap<>();
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
        textures = new TextureAtlasSprite[components.length];
        for(int i = 0; i < components.length; i++){
            textures[i] = iconRegistrar.registerSprite(new EFluxResourceLocation("items/"+getTextureName(i)));
        }
    }

}
