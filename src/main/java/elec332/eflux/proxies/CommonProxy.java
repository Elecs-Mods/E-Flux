package elec332.eflux.proxies;

import com.google.common.base.Preconditions;
import elec332.core.inventory.window.IWindowFactory;
import elec332.core.inventory.window.IWindowHandler;
import elec332.core.inventory.window.Window;
import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderConnection;
import elec332.eflux.endernetwork.EnderConnectionHelper;
import elec332.eflux.inventory.WindowEnderContainer;
import elec332.eflux.inventory.WindowEnderInventory;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import elec332.eflux.util.capability.RedstoneCapability;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class CommonProxy implements IWindowHandler {

    public void initRenderStuff(){
    }

    public World getClientWorld(){
        return null;
    }

    @Override
    public Window createWindow(byte ID, EntityPlayer player, World world, int x, int y, int z) {
        final TileEntity tile = WorldHelper.getTileAt(world, new BlockPos(x, y, z));
        ICapabilityProvider capabilityProvider = tile;
        switch (ID){
            case 1:
                if (tile instanceof TileEntityBreakableMachine) {
                    return ((TileEntityBreakableMachine) tile).getBreakableMachineInventory().brokenGui();
                }
                return null;
            case 4:
                IEnderNetworkComponent component1 = EnderConnectionHelper.getComponent(tile, PlayerHelper.getPosPlayerIsLookingAt(player, 5).sideHit);
                if (component1 != null){
                    return new WindowEnderContainer(component1);
                }
                return null;
            case 2:
                if (tile != null && tile.hasCapability(RedstoneCapability.CAPABILITY, null)){
                    return Preconditions.checkNotNull(tile.getCapability(RedstoneCapability.CAPABILITY, null)).createWindow();
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
                            return new WindowEnderInventory(connection.get());
                        }
                    }
                }
                return null;
            default:
                if (tile instanceof IWindowFactory) {
                    return ((IWindowFactory) tile).createWindow();
                }
                return null;
        }
    }

}
