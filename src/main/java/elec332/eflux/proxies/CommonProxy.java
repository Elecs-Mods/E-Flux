package elec332.eflux.proxies;

import cpw.mods.fml.common.network.IGuiHandler;
import elec332.eflux.inventory.TEGrinderContainer;
import elec332.eflux.tileentity.TEGrinder;
import elec332.eflux.util.GuiID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 24-2-2015.
 */
public abstract class CommonProxy implements IGuiHandler{

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (GuiID.values()[ID]){
            case GRINDER:
                return new TEGrinderContainer((TEGrinder)world.getTileEntity(x, y, z), player);
            default:
                return null;
        }
    }
}
