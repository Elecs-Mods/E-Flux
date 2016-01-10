package elec332.eflux.compat.waila;

import elec332.core.util.AbstractCompatHandler;
import elec332.eflux.blocks.BlockRenderItemInWorld;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 6-9-2015.
 */
public class WailaCompatHandler extends AbstractCompatHandler.ICompatHandler {

    @Override
    public String getName() {
        return "Waila";
    }

    @Override
    public void init() {
    }
}
