package elec332.eflux.client.inventory;

import elec332.eflux.inventory.ContainerCoalGenerator;
import elec332.eflux.tileentity.BaseMachineTEWithInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 29-4-2015.
 */
public class GuiCoalGenerator extends BaseGuiContainer{
    public GuiCoalGenerator(BaseMachineTEWithInventory te, EntityPlayer player) {
        super(new ContainerCoalGenerator(te, player));
        this.ySize = 234;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new ResourceLocation("gui/stolenimagefromCTIV.png");
    }
}
