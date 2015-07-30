package elec332.eflux.client.inventory;

import elec332.core.client.inventory.BaseGuiContainer;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.inventory.TEGrinderContainer;
import elec332.eflux.tileentity.energy.machine.TEGrinder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 4-4-2015.
 */
public class GuiInventoryGrinder extends BaseGuiContainer {

    public GuiInventoryGrinder(TEGrinder grinder, EntityPlayer player) {
        super(new TEGrinderContainer(grinder, player));
        this.ySize = 234;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new EFluxResourceLocation("gui/stolenimagefromCTIV.png");
    }

}
