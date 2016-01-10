package elec332.eflux.items.circuits;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import elec332.core.util.RegisterHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import net.minecraft.crash.CrashReport;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraftforge.common.util.Constants;

import java.util.List;

/**
 * Created by Elec332 on 19-5-2015.
 */
public abstract class AbstractCircuit extends Item implements ICircuit {

    public AbstractCircuit(String txt, int types) {
        super();
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
        this.types = types;
        //setTextureName(EFlux.ModID+":"+txt);
        RegisterHelper.registerItem(this, txt);
    }

    private int types;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+ EFlux.ModID+".BasicCircuitBoard."+"UNNAMED";
    }

    @Override
    public int boardSize(ItemStack stack) {
        return CircuitHandler.get(this, stack.getItemDamage()).getComponents().size();
    }

    @Override
    public ItemStack getRequiredComponent(int i, ItemStack stack) {
        return CircuitHandler.get(this, stack.getItemDamage()).getComponents().get(i);
    }

    @Override
    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list){
        for (int i = 0; i < types; i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    /*@Override
    public void onCreated(ItemStack stack, World world, EntityPlayer player) {
        super.onCreated(stack, world, player);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < boardSize(stack); i++){
            list.appendTag(new NBTTagCompound());
        }
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("items", list);
        stack.setTagCompound(compound);
    }*/

    private boolean breakComponent(int i, ItemStack stack) {
        if (isBoard(stack)){
            ItemStack component = ItemStack.loadItemStackFromNBT(stack.getTagCompound().getTagList("Items", Constants.NBT.TAG_LIST).getCompoundTagAt(i));
            if (component != null && component.getItem() instanceof IElectricComponent && !((IElectricComponent) component.getItem()).isBroken()) {
                NBTTagCompound tag = new NBTTagCompound();
                ((IElectricComponent) component.getItem()).getBroken(component).writeToNBT(tag);
                stack.getTagCompound().getTagList("Items", 10).set(i, tag);
            }
        }
        return false;
    }

    @Override
    public void breakRandomComponent(ItemStack stack) {
        try {
            if (isBoard(stack) && isValid(stack))
                if (!breakComponent(EFlux.random.nextInt(boardSize(stack) - 1), stack)) {
                    breakRandomComponent(stack);
                    stack.getTagCompound().setBoolean("valid", false);
                }
        } catch (StackOverflowError e){
            throw new ReportedException(new CrashReport("Cannot break components on an empty board!", e));
        }
    }

    @Override
    public boolean isValid(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().getBoolean("valid");
    }

    @Override
    public boolean isCircuit(ItemStack stack) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
        list.add(isValid(stack)?"Valid":"Invalid");
        super.addInformation(stack, player, list, b);
    }

    private boolean isBoard(ItemStack stack){
        return stack.getItem() == this;
    }
}
