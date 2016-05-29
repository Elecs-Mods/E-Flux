package elec332.eflux.items;

import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.init.ItemRegister;
import net.minecraft.item.ItemStack;

/**
 * Created by Elec332 on 29-5-2016.
 */
public class ItemEFluxElectricComponents extends ItemEFluxGenerics implements IElectricComponent {

    public ItemEFluxElectricComponents(){
        components = new String[]{
                "resistor", "capacitor", "transistor", "coil", "diode"
        };
    }

    @Override
    protected String getName() {
        return "components";
    }

    @Override
    public ItemStack getBroken(ItemStack stack) {
        return new ItemStack(ItemRegister.brokenComponents, stack.stackSize, stack.getItemDamage());
    }

    @Override
    protected String getTextureName(int meta) {
        return super.getTextureName(meta) + (isBroken(new ItemStack(this, 1, meta)) ? "Broken" : "");
    }

    @Override
    public boolean isBroken(ItemStack stack) {
        return false;
    }

    public static class BrokenComponents extends ItemEFluxElectricComponents {

        @Override
        protected String getName() {
            return "brokenComponents";
        }

        @Override
        public ItemStack getBroken(ItemStack stack) {
            return null;
        }

        @Override
        public boolean isBroken(ItemStack stack) {
            return true;
        }

    }

}
