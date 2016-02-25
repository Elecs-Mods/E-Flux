package elec332.eflux.items.circuits;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 19-5-2015.
 */
public class UnrefinedBoard extends Item implements INoJsonItem {

    public UnrefinedBoard() {
        super();
        this.setCreativeTab(EFlux.creativeTab);
        types = EnumCircuit.values().length;
        setHasSubtypes(true);
    }

    private final int types;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+ EFlux.ModID + ".unrefinedcircuitboard."+stack.getItemDamage();
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (int i = 0; i < types; i++) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    private IItemModel model;

    @Override
    @SideOnly(Side.CLIENT)
    public IItemModel getItemModel(Item item, int meta) {
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
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/emptyBoard"));
    }

}
