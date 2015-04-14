package elec332.eflux.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

/**
 * Created by Elec332 on 14-4-2015.
 */
public class ContainerNull extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }
}
