package elec332.eflux.items;

import elec332.eflux.EFlux;
import net.minecraft.item.Item;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class EFluxItemBase extends Item {

    public EFluxItemBase(String name) {
        setUnlocalizedName(EFlux.ModID + "." + name);
        setCreativeTab(EFlux.creativeTab);
        //super(name, EFlux.creativeTab, EFlux.ModID);
    }

}
