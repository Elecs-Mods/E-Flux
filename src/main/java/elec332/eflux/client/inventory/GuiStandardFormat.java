package elec332.eflux.client.inventory;

import elec332.core.client.inventory.BaseGuiContainer;
import elec332.core.inventory.BaseContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class GuiStandardFormat extends BaseGuiContainer {

    public GuiStandardFormat(BaseContainer container, ResourceLocation txtName) {
        super(container);
        this.txtName = txtName;
    }

    private ResourceLocation txtName;

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return txtName;
    }
}
