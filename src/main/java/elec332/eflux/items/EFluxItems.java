package elec332.eflux.items;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import elec332.eflux.EFlux;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 17-5-2015.
 */
public class EFluxItems extends Item {

    public EFluxItems(){
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
    }

    //private IIcon[] textures;
    private String[] components = {
            "coalDust", "carbonPlate", "blueprint", "unrefinedCircuitBoard", "emptyCircuitBoard", "groundMesh"
    };

    protected String getName(){
        return "genericItems";
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+EFlux.ModID+"."+getName()+"."+components[stack.getItemDamage()];
    }

   // @SideOnly(Side.CLIENT)
   // public void registerIcons(IIconRegister iconRegister){
   //     textures = new IIcon[components.length];
   //     for(int i = 0; i < components.length; i++){
   //         textures[i] = iconRegister.registerIcon(EFlux.ModID+":"+getName()+"."+components[i]);
   //     }
   // }

    @Override
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list){
        for (int i = 0; i < components.length; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

   // @Override
   // @SideOnly(Side.CLIENT)
   // public IIcon getIconFromDamage(int meta) {
   //     return textures[meta];
   // }
}
