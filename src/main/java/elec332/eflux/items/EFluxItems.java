package elec332.eflux.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.eflux.EFlux;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by Elec332 on 17-5-2015.
 */
public class EFluxItems extends Item {

    public EFluxItems(){
        this.setCreativeTab(EFlux.CreativeTab);
        this.setHasSubtypes(true);
    }

    private IIcon[] textures;
    private String[] components = {
            "coal dust", "carbon plate", "blueprint", "unrefined circuit board", "empty circuit board"
    };

    protected String getName(){
        return "GenericItems";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+EFlux.ModID+"."+getName()+"."+components[stack.getItemDamage()];
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister){
        textures = new IIcon[components.length];
        for(int i = 0; i < components.length; i++){
            textures[i] = iconRegister.registerIcon(EFlux.ModID+":"+getName()+"."+components[i]);
        }
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
    public IIcon getIconFromDamage(int meta) {
        return textures[meta];
    }
}
