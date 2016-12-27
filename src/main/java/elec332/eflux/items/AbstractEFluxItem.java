package elec332.eflux.items;

import elec332.core.item.AbstractItem;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;

/**
 * Created by Elec332 on 10-9-2015.
 */
public abstract class AbstractEFluxItem extends AbstractItem {

    public AbstractEFluxItem(String name){
        super(name == null ? null : new EFluxResourceLocation(name));
        setCreativeTab(EFlux.creativeTab);
    }

}
