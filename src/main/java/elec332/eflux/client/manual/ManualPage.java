package elec332.eflux.client.manual;

import elec332.core.inventory.widget.WidgetButton;
import elec332.eflux.client.manual.gui.WindowManual;
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

    public void onPageOpened(WindowManual manual){
    }

    public void onPageFlip(WindowManual manual){
    }

    public void onButtonPress(WidgetButton button){
    }

    @SideOnly(Side.CLIENT)
    public abstract void renderPage(int width, int height, int mouseX, int mouseY, WindowManual manual);

}
