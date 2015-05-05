package elec332.eflux.inventory;

import elec332.eflux.tileentity.energy.machine.TEGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;

/**
 * Created by Elec332 on 4-4-2015.
 */
public class TEGrinderContainer extends BaseContainer {

    public TEGrinderContainer(TEGrinder theGrinder, EntityPlayer player){
        super(theGrinder, player, 68);  //The last in is the visual offset (down)
        this.theGrinder = theGrinder;

        for(int a = 0; a < 2; a++) {
            for(int i = 0; i < 9; i++) {
                addSlotToContainer(new Slot(theGrinder, i + (a * 9), 8 + i * 18, 112 + (18 * a)));
            }
        }
        addPlayerInventoryToContainer();
    }

    private TEGrinder theGrinder;

    @Override
    protected int hotBarFactor() {
        return 1;
    }
}
