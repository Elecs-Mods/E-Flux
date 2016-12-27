package elec332.eflux.items;

import elec332.core.item.AbstractMultipartItem;
import elec332.eflux.EFlux;
import mcmultipart.api.multipart.IMultipart;
import net.minecraft.block.Block;

/**
 * Created by Elec332 on 10-2-2016.
 */
public abstract class AbstractEFluxMultiPartItem<T extends Block & IMultipart> extends AbstractMultipartItem<T> {

    public AbstractEFluxMultiPartItem(T t){
        super(t);
        this.setCreativeTab(EFlux.creativeTab);
    }

}
