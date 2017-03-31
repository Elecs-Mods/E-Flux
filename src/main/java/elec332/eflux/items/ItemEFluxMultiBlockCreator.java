package elec332.eflux.items;

import elec332.eflux.EFlux;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemEFluxMultiBlockCreator extends AbstractTexturedEFluxItem {

    public ItemEFluxMultiBlockCreator() {
        super("multiBlockCreator");
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseC(EntityPlayer player, EnumHand hand, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EFlux.multiBlockRegistry.getStructureRegistry().attemptCreate(player, world, pos, facing);
        return EnumActionResult.PASS;
    }

}
