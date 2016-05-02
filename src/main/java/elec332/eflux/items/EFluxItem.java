package elec332.eflux.items;

import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Elec332 on 10-9-2015.
 */
public abstract class EFluxItem extends Item {

    public EFluxItem(String name){
        this.name = name;
        setRegistryName(new EFluxResourceLocation(name));
        setUnlocalizedName(EFlux.ModID+"."+name);
        setCreativeTab(EFlux.creativeTab);
    }

    private final String name;

    public EFluxItem register(){
        GameRegistry.register(this);
        return this;
    }

}
