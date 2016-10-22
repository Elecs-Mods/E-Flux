package elec332.eflux.items;

import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import mcmultipart.item.ItemMultiPart;

/**
 * Created by Elec332 on 10-2-2016.
 */
public abstract class AbstractEFluxMultiPartItem extends ItemMultiPart {

    public AbstractEFluxMultiPartItem(String name){
        this.setCreativeTab(EFlux.creativeTab);
        setRegistryName(new EFluxResourceLocation(name));
        setUnlocalizedName(getRegistryName().toString().replace(":", ".").toLowerCase());
    }

}
