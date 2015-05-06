package elec332.eflux.inventory;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by Elec332 on 5-5-2015.
 */
public class ContainerMachine extends BaseContainer {
    public ContainerMachine(ITileWithSlots tileWithSlots, EntityPlayer player, int offset) {
        super(player, offset);
        tileWithSlots.addSlots(this);
    }
}
