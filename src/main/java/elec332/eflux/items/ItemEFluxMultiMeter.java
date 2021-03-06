package elec332.eflux.items;

import elec332.core.api.util.IRightClickCancel;
import elec332.core.util.PlayerHelper;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyGridInformation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 5-4-2015.
 */
public class ItemEFluxMultiMeter extends AbstractTexturedEFluxItem implements IRightClickCancel {

    public ItemEFluxMultiMeter() {
        super("multimeter");
        setContainerItem(this);
        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseC(EntityPlayer player, EnumHand hand, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (EFlux.gridHandler == null){
                return EnumActionResult.FAIL;
            }
            IEnergyGridInformation info = EFlux.gridHandler.getInformationFor(new DimensionCoordinate(world, pos));
            if (info != null){
                PlayerHelper.sendMessageToPlayer(player, "Current EF: "+info.getCurrentEF(facing));
                PlayerHelper.sendMessageToPlayer(player, "Provided RP: "+info.getLastProcessedRP(facing));
            }
        }
        return EnumActionResult.SUCCESS;
    }

}
