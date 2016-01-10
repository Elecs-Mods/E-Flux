package elec332.eflux.items.circuits;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import elec332.core.util.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.EnumCircuit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 19-5-2015.
 */
public class BluePrint extends Item {

    public BluePrint(String txt, int types, EnumCircuit circuit) {
        super();
        this.circuit = circuit;
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
        this.types = types;
        //setTextureName(EFlux.ModID+":"+txt);
        RegisterHelper.registerItem(this, txt);
    }

    private int types;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+ EFlux.ModID+".BluePrint."+"UNNAMED";
    }

    public EnumCircuit circuit;

    @Override
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list){
        for (int i = 0; i < types; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }
}
