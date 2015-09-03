package elec332.eflux.blocks;

import elec332.core.client.render.SidedBlockRenderingCache;
import elec332.eflux.client.blocktextures.MachineGlassTextureHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * Created by Elec332 on 2-9-2015.
 */
public class BlockHeatGlass extends BlockMachinePart{

    public BlockHeatGlass(int types) {
        super(types, "BlockMachineGlass", new SidedBlockRenderingCache(new MachineGlassTextureHandler(), types));
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.glass." + stack.getItemDamage());
    }

}
