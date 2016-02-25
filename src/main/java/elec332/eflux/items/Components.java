package elec332.eflux.items;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.core.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class Components extends Item implements IElectricComponent, INoJsonItem {

    public static void init(){
        component = new Components();
        brokenComponent = new BrokenComponents();
        GameRegistry.registerItem(component, component.getName());
        GameRegistry.registerItem(brokenComponent, brokenComponent.getName());
    }

    public static Components component, brokenComponent;

    private Components(){
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;
    @SideOnly(Side.CLIENT)
    private IBakedModelMetaMap<IItemModel> models;
    private String[] components = {
            "resistor", "capacitor", "transistor", "coil", "diode"
    };

    protected String getName(){
        return "Components";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+EFlux.ModID+"."+getName()+"."+components[stack.getItemDamage()];
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
    public ItemStack getBroken(ItemStack stack) {
        return new ItemStack(brokenComponent, stack.stackSize, stack.getItemDamage());
    }

    @Override
    public boolean isBroken() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemModel getItemModel(Item item, int meta) {
        return models.forMeta(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        models = new BakedModelMetaMap<IItemModel>();
        for (int i = 0; i < components.length; i++) {
            models.setModelForMeta(i, modelBakery.itemModelForTextures(textures[i]));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[components.length];
        for(int i = 0; i < components.length; i++){
            textures[i] = iconRegistrar.registerSprite(new EFluxResourceLocation("items/" + getName() + "_" + components[i]));
        }
    }

    private static class BrokenComponents extends Components{

        @Override
        protected String getName() {
            return "BrokenComponents";
        }

        @Override
        public ItemStack getBroken(ItemStack stack) {
            return null;
        }

        @Override
        public boolean isBroken() {
            return true;
        }
    }
}
