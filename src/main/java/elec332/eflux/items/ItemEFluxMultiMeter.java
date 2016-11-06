package elec332.eflux.items;

import elec332.core.api.util.IRightClickCancel;
import elec332.core.world.DimensionCoordinate;
import elec332.eflux.EFlux;
import elec332.eflux.api.energy.IEnergyGridInformation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
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

    @Override
    @Nonnull
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            IEnergyGridInformation info = EFlux.gridHandler.getInformationFor(new DimensionCoordinate(world, pos));
            if (info != null){
                player.addChatComponentMessage(new TextComponentString("Current RP: "+info.getCurrentRP(facing)));
                player.addChatComponentMessage(new TextComponentString("Provided EF: "+info.getLastProcessedEF(facing)));
            }
        }
        return EnumActionResult.SUCCESS;
    }

}
