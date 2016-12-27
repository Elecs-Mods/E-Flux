package elec332.eflux.inventory;

import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.ISimpleWindowFactory;
import elec332.core.inventory.window.Window;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Elec332 on 2-12-2016.
 */
public enum WindowFactories implements ISimpleWindowFactory {

    SINGLE_SLOT {

        @Override
        public void modifyWindow(Window window, Object... args) {
            window.addPlayerInventoryToContainer().addWidget(new WidgetSlot((IItemHandler) args[0], 0, 66, 53));
        }

    };



}
