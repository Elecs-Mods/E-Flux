package elec332.eflux.items;

import elec332.core.client.model.loading.IBlockModelItemLink;
import elec332.core.item.AbstractItemBlock;
import net.minecraft.block.Block;

/**
 * Created by Elec332 on 18-3-2017.
 */
public class AbstractTexturedItemBlock extends AbstractItemBlock implements IBlockModelItemLink {

    public AbstractTexturedItemBlock(Block block) {
        super(block);
    }

}
