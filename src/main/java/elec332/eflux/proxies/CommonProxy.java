package elec332.eflux.proxies;

import elec332.core.inventory.ContainerMachine;
import elec332.core.tile.IInventoryTile;
import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import elec332.eflux.inventory.ContainerEnderContainer;
import elec332.eflux.inventory.ContainerEnderInventory;
import elec332.eflux.tileentity.BreakableMachineTile;
import elec332.eflux.util.capability.RedstoneCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class CommonProxy implements IGuiHandler {

    @Override
    public Container getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        final TileEntity tile = WorldHelper.getTileAt(world, new BlockPos(x, y, z));
        ICapabilityProvider capabilityProvider = tile;
        switch (ID){
            case 1:
                if (tile instanceof BreakableMachineTile) {
                    return (Container) ((BreakableMachineTile) tile).getBreakableMachineInventory().brokenGui(Side.SERVER, player);
                }
                return null;
            case 4:
                IEnderNetworkComponent component1 = EnderConnectionHelper.getComponent(tile, PlayerHelper.getPosPlayerIsLookingAt(player, 5).sideHit);
                if (component1 != null){
                    return new ContainerEnderContainer(player, component1);
                }
                return null;
            case 2:
                if (tile != null && tile.hasCapability(RedstoneCapability.CAPABILITY, null)){
                    return new ContainerMachine(tile.getCapability(RedstoneCapability.CAPABILITY, null), player, 0);
                }
                return null;
            case 5:
                if (capabilityProvider == null && x == z && z == -3){
                    capabilityProvider = player.getHeldItem(y == -3 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                }
                if (capabilityProvider != null && capabilityProvider.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                    @SuppressWarnings("unchecked")
                    IEnderNetworkComponent<IItemHandler> component2 = capabilityProvider.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                    if (component2 != null){
                        IEnderConnection<IItemHandler> connection = component2.getCurrentConnection();
                        if (connection != null && connection.get() != null){
                            return new ContainerEnderInventory(player, connection.get());
                        }
                    }
                }
                return null;
            default:
                if (tile instanceof IInventoryTile) {
                    return ((IInventoryTile) tile).getGuiServer(player);
                }
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void initRenderStuff(){
    }

    public World getClientWorld(){
        return null;
    }

}
