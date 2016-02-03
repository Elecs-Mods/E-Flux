package elec332.eflux.client.manual;

import elec332.eflux.client.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 30-1-2016.
 */
public abstract class ManualPage {

    public ManualPage(){

    }

    private ManualCategory category;

    void setCategory(ManualCategory category) {
        if (this.category != null){
            throw new IllegalStateException();
        }
        this.category = category;
        onAddedToCategory(category);
    }

    protected void onAddedToCategory(ManualCategory category){
    }

    @Nonnull
    public ManualCategory getCategory() {
        if (category == null){
            throw new IllegalStateException();
        }
        return category;
    }

    public void onPageOpened(GuiManual manual){
    }

    public void onPageFlip(GuiManual manual){
    }

    public void onButtonPress(GuiButton button){
    }

    @SideOnly(Side.CLIENT)
    public abstract void renderPage(int width, int height, int mouseX, int mouseY, GuiManual manual);

}
