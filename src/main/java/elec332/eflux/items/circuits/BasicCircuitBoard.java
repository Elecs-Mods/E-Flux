package elec332.eflux.items.circuits;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.helper.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.EnumCircuit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class BasicCircuitBoard extends CircuitBase {
    protected BasicCircuitBoard(int i) {
        super("BasicCircuitBoard");
        this.types = i;
        this.setHasSubtypes(true);
        RegisterHelper.registerItem(this, "BasicCircuitBoard");
    }

    private int types;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+ EFlux.ModID+".BasicCircuitBoard."+"UNNAMED";
    }

    @Override
    public int boardSize(ItemStack stack) {
        return CircuitHandler.get(this, stack.getItemDamage()).getComponents().size();
    }

    @Override
    public ItemStack getRequiredComponent(int i, ItemStack stack) {
        return CircuitHandler.get(this, stack.getItemDamage()).getComponents().get(i);
    }

    @Override
    public EnumCircuit getDifficulty() {
        return EnumCircuit.SMALL;
    }

    @Override
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list){
        for (int i = 0; i < types; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }
}
