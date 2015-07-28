package elec332.eflux.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.helper.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.IElectricComponent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class Components extends Item implements IElectricComponent{

    public static void init(){
        component = new Components();
        brokenComponent = new BrokenComponents();
        RegisterHelper.registerItem(component, component.getName());
        RegisterHelper.registerItem(brokenComponent, brokenComponent.getName());
    }

    public static Components component, brokenComponent;

    private Components(){
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
    }

    private IIcon[] textures;
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

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister){
        textures = new IIcon[components.length];
        for(int i = 0; i < components.length; i++){
            textures[i] = iconRegister.registerIcon(EFlux.ModID+":"+getName()+"."+"Component_" + components[i]);
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

    @Override
    public ItemStack getBroken(ItemStack stack) {
        return new ItemStack(brokenComponent, stack.stackSize, stack.getItemDamage());
    }

    @Override
    public boolean isBroken() {
        return false;
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
