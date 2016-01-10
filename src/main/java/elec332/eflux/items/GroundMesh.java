package elec332.eflux.items;

import elec332.eflux.init.ItemRegister;
import elec332.eflux.util.DustPile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class GroundMesh extends EFluxItem {

    public GroundMesh() {
        super("GroundMesh");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advancedToolTips) {
        super.addInformation(stack, player, tooltip, advancedToolTips);
        if (isValidMesh(stack)) {
            DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
            for (DustPile.DustPart dustPart : dustPile.getContent()) {
                tooltip.add(dustPart.getContent() + ": " + dustPart.getNuggetAmount());
            }
        }
    }

    public static boolean isValidMesh(ItemStack stack){
        return !(stack == null || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null) && stack.getTagCompound().getBoolean("dusts_scanned");
    }

}
