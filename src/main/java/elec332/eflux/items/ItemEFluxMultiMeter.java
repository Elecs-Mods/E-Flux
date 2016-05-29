package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.util.IMultiMeterDataProvider;
import elec332.eflux.api.util.IMultiMeterDataProviderMultiLine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 5-4-2015.
 */
public class ItemEFluxMultiMeter extends AbstractTexturedEFluxItem implements IRightClickCancel{

    public ItemEFluxMultiMeter() {
        super("multimeter");
        setContainerItem(this);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = WorldHelper.getTileAt(world, pos);
        if (!world.isRemote) {
            if (tileEntity instanceof IMultiMeterDataProvider)
                player.addChatComponentMessage(new TextComponentString(((IMultiMeterDataProvider) tileEntity).getProvidedData()));
            if (tileEntity instanceof IMultiMeterDataProviderMultiLine)
                for (String s : ((IMultiMeterDataProviderMultiLine) tileEntity).getProvidedData())
                    player.addChatComponentMessage(new TextComponentString(s));
            //TODO: more provided info
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }

}
