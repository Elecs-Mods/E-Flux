package elec332.eflux.items;

import elec332.core.baseclasses.item.BaseItem;
import elec332.eflux.EFlux;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class EFluxItemBase extends BaseItem{
    public EFluxItemBase(String name) {
        super(name, EFlux.CreativeTab, EFlux.ModID);
    }
}
