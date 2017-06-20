package elec332.eflux.items;

import elec332.core.util.ItemStackHelper;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.util.DustPile;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class ItemEFluxGroundMesh extends AbstractTexturedEFluxItem {

    public ItemEFluxGroundMesh() {
        super("groundMesh");
    }

    @Override
    public void addInformationC(ItemStack stack, World player, List<String> tooltip, boolean advancedToolTips) {
        super.addInformationC(stack, player, tooltip, advancedToolTips);
        if (!(!ItemStackHelper.isStackValid(stack) || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null)) {
            DustPile dustPile = DustPile.fromNBT(stack.getTagCompound());
            if (isValidMesh(stack)) {
                for (GrinderRecipes.OreDictStack dustPart : dustPile.getContent()) {
                    tooltip.add(dustPart.name + ": " + dustPart.amount);
                }
            } else {
                tooltip.add("Total: " + dustPile.getSize());
            }
        }
    }

    public static boolean isValidMesh(ItemStack stack){
        return !(!ItemStackHelper.isStackValid(stack) || stack.getItem() != ItemRegister.groundMesh || stack.getTagCompound() == null) && stack.getTagCompound().getBoolean("dusts_scanned");
    }

}
