package elec332.eflux.endernetwork;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 23-2-2016.
 */
public interface ILinkableItem {

    @Nullable
    public UUID getLinkID(ItemStack stack);

    public boolean hasLink(ItemStack stack);

    public void setLinkID(ItemStack stack, @Nullable UUID newID);

}
