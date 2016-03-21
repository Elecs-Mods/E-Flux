package elec332.eflux.items;

import elec332.eflux.EFlux;
import mcmultipart.item.ItemMultiPart;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Elec332 on 10-2-2016.
 */
public abstract class ItemEFluxMultiPart extends ItemMultiPart {

    public ItemEFluxMultiPart(String name){
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
        setUnlocalizedName(EFlux.ModID + "." + name);
        this.name = name;
    }

    private final String name;

    public ItemEFluxMultiPart register(){
        GameRegistry.registerItem(this, name);
        return this;
    }

}
