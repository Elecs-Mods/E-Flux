package elec332.eflux.inventory;

import elec332.eflux.tileentity.BaseMachineTEWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * Created by Elec332 on 29-4-2015.
 */
public class ContainerCoalGenerator extends BaseContainer {
    public ContainerCoalGenerator(BaseMachineTEWithInventory te, EntityPlayer player){
        super(te, player, 68);  //The last in is the visual offset (down)
        addSlotToContainer(new Slot(te, 0, 8 * 18, 130));
        addPlayerInventoryToContainer();
    }
}
