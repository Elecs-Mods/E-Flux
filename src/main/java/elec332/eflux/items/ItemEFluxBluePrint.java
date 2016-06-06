package elec332.eflux.items;

import elec332.core.main.ElecCore;
import elec332.core.util.StatCollector;
import elec332.eflux.EFlux;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.AbstractTexturedEFluxItem;
import elec332.eflux.items.circuits.ICircuitDataProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 19-5-2015.
 */
public class ItemEFluxBluePrint extends AbstractTexturedEFluxItem {

    public ItemEFluxBluePrint() {
        super("blueprint");
    }

    @SuppressWarnings("all")
    @Nullable
    public ICircuitDataProvider getBlueprintData(ItemStack stack){
        if (stack != null && stack.getItem() == this && stack.hasTagCompound() && stack.getTagCompound().hasKey("bp")){
            return EFlux.circuitRegistry.getObject(new ResourceLocation(stack.getTagCompound().getString("bp")));
        }
        return null;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        if (EFlux.circuitRegistry.getKeys().size() > 0 && ElecCore.developmentEnvironment) {
            for (ICircuitDataProvider circuitData : EFlux.circuitRegistry) {
                subItems.add(createBlueprint(circuitData));
            }
        } else {
            super.getSubItems(itemIn, tab, subItems);
        }
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        ICircuitDataProvider circuitDataProvider = getBlueprintData(stack);
        if (circuitDataProvider != null){
            tooltip.add(StatCollector.translateToLocal("circuit."+circuitDataProvider.getRegistryName().toString().replace(":", ".")));
        } else {
            tooltip.add(TextFormatting.RED + "INVALID");
        }
    }

    public static ItemStack createBlueprint(ICircuitDataProvider data){
        if (data == null){
            return null;
        }
        ItemStack ret = new ItemStack(ItemRegister.nullBlueprint);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("bp", data.getRegistryName().toString());
        ret.setTagCompound(tag);
        return ret;
    }

}
