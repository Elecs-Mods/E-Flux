package elec332.eflux.handler;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import elec332.core.main.ElecCore;
import elec332.core.util.InventoryHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.ItemEFluxInfusedEnder;
import elec332.eflux.util.capability.IRedstoneUpgradable;
import elec332.eflux.util.capability.RedstoneCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by Elec332 on 27-4-2016.
 */
public class WorldEventHandler {

    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Detonate event){
        if (!event.getWorld().isRemote) {
            int ender = 0, blaze = 0, gold = 0;
            boolean enderman = false;
            final List<EntityItem> list = Lists.newArrayList();
            for (Entity entity : event.getAffectedEntities()) {
                if (entity instanceof EntityEnderman) {
                    enderman = true;
                }
                if (entity instanceof EntityItem) {
                    ItemStack stack = ((EntityItem) entity).getEntityItem();
                    if (stack != null && stack.getItem() != null) {
                        Item item = stack.getItem();
                        if (item == Items.ENDER_PEARL) {
                            ender += stack.stackSize;
                        } else if (item == Items.BLAZE_ROD) {
                            blaze += stack.stackSize;
                        } else if (InventoryHelper.areEqualNoSizeNoNBT(stack, ItemRegister.dustGold)) {
                            gold += stack.stackSize;
                        } else if (item == ItemRegister.enderLink){
                            list.add((EntityItem) entity);
                        }
                    }
                }
            }
            if (enderman) {
                int pairs = 0;
                while (ender >= 2 && blaze >= 1 && gold >= 2) {
                    pairs++;
                    ender -= 2;
                    gold -= 2;
                    blaze--;
                }
                final int pairs2 = pairs;
                ElecCore.tickHandler.registerCall(new Runnable() {
                    @Override
                    public void run() {
                        Vec3d pos = event.getExplosion().getPosition();
                        for (int i = 0; i < pairs2; i++) {
                            ItemStack stack = ItemEFluxInfusedEnder.createStack(EnderNetworkManager.get(event.getWorld()).generateNewKey(), null);
                            //stack.stackSize = 2;
                            WorldHelper.dropStack(event.getWorld(), (int) pos.xCoord, (int) pos.yCoord, (int) pos.zCoord, stack);
                        }
                        for (EntityItem item : list){
                            if (item.getEntityItem().getItemDamage() > 0) {
                                item.getEntityItem().setItemDamage(0);
                            }
                        }
                    }
                }, event.getWorld());
            }
            event.getAffectedEntities().removeAll(list);
        }
    }

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
