package elec332.eflux.items;

import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Elec332 on 10-9-2015.
 */
public abstract class AbstractEFluxItem extends Item {

    public AbstractEFluxItem(String name){
        this.name = name;
        setRegistryName(new EFluxResourceLocation(name));
        setUnlocalizedName(getRegistryName().toString().replace(":", ".").toLowerCase());
        setCreativeTab(EFlux.creativeTab);
    }

    protected final String name;

    public AbstractEFluxItem register(){
        GameRegistry.register(this);
        return this;
    }

}
