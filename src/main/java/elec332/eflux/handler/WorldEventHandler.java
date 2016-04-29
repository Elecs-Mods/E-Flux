package elec332.eflux.handler;

import com.google.common.base.Predicate;
import elec332.core.world.WorldHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.util.IRedstoneUpgradable;
import elec332.eflux.util.RedstoneCapability;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Elec332 on 27-4-2016.
 */
public class WorldEventHandler {

    @SubscribeEvent
    public void onBlockActivated(PlayerInteractEvent.RightClickBlock event){
        TileEntity tile = WorldHelper.getTileAt(event.getWorld(), event.getPos());
        if (tile != null && tile.hasCapability(RedstoneCapability.CAPABILITY, null)){
            tile.getCapability(RedstoneCapability.CAPABILITY, null).onActivated(event.getEntityPlayer(), event.getItemStack());
        }
    }

    @SubscribeEvent
    public void onBlockChanged(BlockEvent.NeighborNotifyEvent event){
        for (EnumFacing facing : event.getNotifiedSides()){
            onNeighborChanged(event.getWorld(), event.getPos().offset(facing));
        }
    }

    private void onNeighborChanged(World world, BlockPos pos){
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile != null && tile.hasCapability(RedstoneCapability.CAPABILITY, null)){
            tile.getCapability(RedstoneCapability.CAPABILITY, null).onNeighborChanged(world, pos);
        }
    }

    @SubscribeEvent
    public void checkCapabilities(AttachCapabilitiesEvent.TileEntity event){
        final TileEntity tile = event.getTileEntity();
        if (tile instanceof IRedstoneUpgradable){
            event.addCapability(new EFluxResourceLocation("redstone"), new RCP(((IRedstoneUpgradable) tile).getModePredicate()));
        }
    }

    private class RCP implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

        private RCP(Predicate<IRedstoneUpgradable.Mode> predicate){
            capability = new RedstoneCapability(predicate);
        }

        private final RedstoneCapability capability;

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == RedstoneCapability.CAPABILITY;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == RedstoneCapability.CAPABILITY ? (T) this.capability : null ;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return capability.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            capability.deserializeNBT(nbt);
        }

    }

}
