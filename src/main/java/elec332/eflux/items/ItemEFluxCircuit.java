package elec332.eflux.items;

import elec332.core.util.InventoryHelper;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.api.circuit.IElectricComponent;
import elec332.eflux.circuit.ICircuitDataProvider;
import elec332.eflux.circuit.IEFluxCircuit;
import elec332.eflux.util.CapabilityWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 29-5-2016.
 */
public class ItemEFluxCircuit extends AbstractTexturedEFluxItem {

    public ItemEFluxCircuit() {
        super("circuit");
    }

    @Override
    @Nonnull
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return CapabilityWrapper.getProviderFor(EFluxAPI.EFLUX_CIRCUIT_CAPABILITY, new CircuitCapabilityImpl(nbt));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItemsC(@Nonnull Item item, List<ItemStack> subItems, CreativeTabs creativeTab) {
        for (EnumCircuit type : EnumCircuit.VALUES){
            subItems.add(createNewEmptyCircuit(type));
        }
    }

    @Override
    public void addInformationC(ItemStack stack, World world, List<String> tooltip, boolean advanced) {
        super.addInformationC(stack, world, tooltip, advanced);
        ICircuit circuit = CircuitHelper.getCircuit(stack);
        if (circuit != null) {
            tooltip.add("Etched: " + circuit.isEtchedCircuit());
            tooltip.add("Type: " + circuit.getCircuitName());
            tooltip.add("Valid: " + circuit.isValidCircuit());
            tooltip.add("Board: " + circuit.getDifficulty().name());
        } else {
            tooltip.add("ERROR");
        }
    }

    public ItemStack createNewEmptyCircuit(EnumCircuit circuit){
        if (circuit == null) {
            circuit = EnumCircuit.SMALL;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("lvlB", circuit.getCircuitLevel());
        return new ItemStack(this, 1, 0, tag);
    }

    private static class CircuitCapabilityImpl implements IEFluxCircuit, INBTSerializable<NBTTagCompound> {

        private CircuitCapabilityImpl(NBTTagCompound tag){
            if (tag != null && tag.hasKey("lvlB")){
                this.diff = EnumCircuit.fromLevel(tag.getByte("lvlB"));
            } else {
                this.diff = EnumCircuit.SMALL;
            }
            getSolderedComponents();
        }

        private EnumCircuit diff;
        private boolean etched, valid;
        private ICircuitDataProvider circuit;
        private NonNullList<ItemStack> components;

        @Override
        public int boardSize() {
            if (circuit == null){
                return 0;
            }
            return circuit.getComponents().length;
        }

        @Override
        public ItemStack getRequiredComponent(int slot) {
            if (circuit == null){
                return null;
            }
            return ItemStackHelper.copyItemStack(circuit.getComponents()[slot]);
        }

        @Override
        public void breakRandomComponent() {
            if (isEtchedCircuit()){
                if (components != null && components.size() > 0 && !breakComponent(EFlux.random.nextInt(components.size() - 1))){
                    breakRandomComponent();
                }
            }
        }

        private boolean breakComponent(int i){
            ItemStack stack = components.get(i);
            if (ItemStackHelper.isStackValid(stack) && stack.getItem() instanceof IElectricComponent){
                IElectricComponent component = (IElectricComponent) stack.getItem();
                if (!component.isBroken(stack)){
                    components.set(i, component.getBroken(stack));
                    valid = false;
                    return true;
                }
            }
            return false;
        }

        @Override
        public void validate() {
            if (components != null && components.size() == boardSize()){
                for (int i = 0; i < components.size(); i++) {
                    if (!InventoryHelper.areEqualNoSizeNoNBT(components.get(i), getRequiredComponent(i))){
                        valid = false;
                        return;
                    }
                }
                valid = true;
            } else {
                valid = false;
            }
        }

        @Override
        public boolean isValidCircuit() {
            return valid;
        }

        @Override
        public EnumCircuit getDifficulty() {
            return diff;
        }

        @Override
        public void etch(ICircuitDataProvider dataProvider) {
            clear();
            if (dataProvider != null/* && dataProvider.getCircuitType() == getDifficulty()*/) {
                this.circuit = dataProvider;
                this.diff = dataProvider.getCircuitType();
                this.etched = true;
            }
        }

        @Override
        public boolean isEtchedCircuit() {
            return etched;
        }

        @Override
        @Nonnull
        public List<ItemStack> getSolderedComponents() {
            if (components == null){
                components = NonNullList.withSize(boardSize(), ItemStackHelper.NULL_STACK);
            }
            return components;
        }

        @Override
        public void setSolderedComponents(List<ItemStack> components) {
            if (!(components instanceof NonNullList)){
                throw new UnsupportedOperationException();
            }
            this.components = (NonNullList<ItemStack>) components;
        }

        private void clear(){
            etched = false;
            valid = false;
            circuit = null;
            components = null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("etched", etched);
            tag.setBoolean("valid", valid);
            if (circuit != null) {
                tag.setString("circuitData", circuit.getRegistryName().toString());
            } else {
               tag.setByte("diff", diff.getCircuitLevel());
            }
            if (components != null) {
                tag.setTag("components", InventoryHelper.writeItemsToNBT(components));
            }
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            clear();
            this.etched = nbt.getBoolean("etched");
            this.valid = nbt.getBoolean("valid");
            if (nbt.hasKey("circuitData")) {
                this.circuit = EFlux.circuitRegistry.getValue(new ResourceLocation(nbt.getString("circuitData")));
                this.diff = this.circuit.getCircuitType();
            } else {
                this.circuit = null;
                if (nbt.hasKey("diff")) {
                    this.diff = EnumCircuit.fromLevel(nbt.getByte("diff"));
                } else if (this.diff == null){
                    this.diff = EnumCircuit.SMALL;
                }
            }
            getSolderedComponents();
            InventoryHelper.readItemsFromNBT(nbt.getCompoundTag("components"), this.components);
        }

        @Override
        public ResourceLocation getCircuitName() {
            return circuit == null ? null : circuit.getRegistryName();
        }

    }

}