package elec332.eflux.items;

import elec332.eflux.EFlux;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemEFluxMultiBlockCreator extends AbstractTexturedEFluxItem {

    public ItemEFluxMultiBlockCreator() {
        super("multiBlockCreator");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        EFlux.multiBlockRegistry.getStructureRegistry().attemptCreate(player, world, pos, side);
        return EnumActionResult.PASS;
    }

}
