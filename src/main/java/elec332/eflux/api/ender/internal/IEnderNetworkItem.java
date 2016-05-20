package elec332.eflux.api.ender.internal;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Elec332 on 16-5-2016.
 *
 * INTERNAL, Do not use!
 */
public interface IEnderNetworkItem {

    public void setNetworkID(UUID uuid, ItemStack stack);

    @Nullable
    public UUID getNetworkID(ItemStack stack);

}
