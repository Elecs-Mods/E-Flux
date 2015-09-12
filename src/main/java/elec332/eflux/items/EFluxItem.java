package elec332.eflux.items;

import elec332.core.helper.RegisterHelper;
import net.minecraft.item.Item;

/**
 * Created by Elec332 on 10-9-2015.
 */
public abstract class EFluxItem extends Item {

    public EFluxItem(String name){
        this.name = name;
        setUnlocalizedName(name);
    }

    private final String name;

    public EFluxItem register(){
        RegisterHelper.registerItem(this, name);
        return this;
    }

}
