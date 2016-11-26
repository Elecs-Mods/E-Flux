package elec332.eflux.items;

import elec332.core.item.AbstractMultipartItem;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import mcmultipart.item.ItemMultiPart;

/**
 * Created by Elec332 on 10-2-2016.
 */
public abstract class AbstractEFluxMultiPartItem extends AbstractMultipartItem {

    public AbstractEFluxMultiPartItem(String name){
        super(new EFluxResourceLocation(name));
        this.setCreativeTab(EFlux.creativeTab);
    }

}
