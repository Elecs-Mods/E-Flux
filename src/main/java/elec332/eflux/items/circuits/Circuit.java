package elec332.eflux.items.circuits;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 19-5-2015.
 */
public class Circuit extends Item implements ICircuit, INoJsonItem {

    public Circuit(String txt, EnumCircuit circuit) {
        super();
        this.setCreativeTab(EFlux.creativeTab);
        this.setHasSubtypes(true);
        GameRegistry.registerItem(this, circuit+"."+txt);
        this.name = txt;
        this.circuit = circuit;
    }

    private final EnumCircuit circuit;
    private final String name;

    @SideOnly(Side.CLIENT)
    private IItemModel model;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item."+EFlux.ModID+".CircuitBoard."+getDifficulty(stack)+"."+name;
    }

    @Override
    public int boardSize(ItemStack stack) {
        return CircuitHandler.get(getDifficulty(stack)).fromName(name).getComponents().length;
    }

    @Override
    public ItemStack getRequiredComponent(ItemStack stack, int slot) {
        return CircuitHandler.get(getDifficulty(stack)).fromName(name).getComponents()[slot];
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
    public EnumCircuit getDifficulty(ItemStack stack) {
        return circuit;
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

    @Override
    public IItemModel getItemModel(Item item, int meta) {
        return model;
    }

    @Override
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.itemModelForTextures(texture);
    }

    @Override
    public void registerTextures(IIconRegistrar iconRegistrar) {
        texture = iconRegistrar.registerSprite(getTexture());
    }

    protected ResourceLocation getTexture(){
        return new EFluxResourceLocation("items/board");
    }

}
