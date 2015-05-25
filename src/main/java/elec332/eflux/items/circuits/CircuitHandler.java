package elec332.eflux.items.circuits;

import com.google.common.collect.Lists;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.api.circuit.ICircuit;
import elec332.eflux.items.Components;
import elec332.eflux.recipes.RecipeRegistry;
import elec332.eflux.util.EnumMachines;
import elec332.eflux.util.RecipeItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class CircuitHandler {
    private static HashMap<EnumCircuit, CircuitHandler> mappings = new HashMap<EnumCircuit, CircuitHandler>();
    private static List<EnumCircuit> registeredTypes = new ArrayList<EnumCircuit>();
    private static boolean init = false;
    public static Item UnrefinedBoards;

    public static ICircuitDataProvider get(ICircuit circuit, int i){
        return get(circuit.getDifficulty()).mapping.get(i);
    }

    public static void register(ICircuitDataProvider provider, EnumCircuit circuit){
        if (!init)
            get(circuit).registerCircuit(provider);
        else throw new UnsupportedOperationException("Cannot register circuits after init");
    }

    public static CircuitHandler get(EnumCircuit circuit) {
        if (circuit == null)
            throw new IllegalArgumentException();
        CircuitHandler ret = mappings.get(circuit);
        if (ret == null) {
            ret = new CircuitHandler(circuit);
            mappings.put(circuit, ret);
            registeredTypes.add(circuit);
        }
        return ret;
    }

    public static void register(){
        init = true;
        UnrefinedBoards = new UnrefinedBoards("UnrefinedBoard", registeredTypes.size()).setCreativeTab(EFlux.CreativeTab);
        for (EnumCircuit circuit : registeredTypes)
            get(circuit).registerData();
    }

    ////////////////////////////////////////////////////////////////////
    private CircuitHandler(EnumCircuit circuit){
        this.circuit = circuit;
        mapping = Lists.newArrayList();
    }
    private EnumCircuit circuit;
    private List<ICircuitDataProvider> mapping;
    public Item circuitItem;

    private void registerCircuit(ICircuitDataProvider provider){
        if (!mapping.contains(provider))
            mapping.add(provider);
    }

    private void registerData(){
        Item circuit = new BasicCircuitBoard(mapping.size()).setCreativeTab(EFlux.CreativeTab);
        this.circuitItem = circuit;
        Item bluePrint = new BluePrint("BluePrint", mapping.size(), this.circuit).setCreativeTab(EFlux.CreativeTab);
        for (int i = 0; i < mapping.size(); i++) {
            RecipeRegistry.instance.registerRecipe(EnumMachines.ETCHINGMACHINE, Lists.newArrayList(new RecipeItemStack(UnrefinedBoards, this.circuit.ordinal()), new RecipeItemStack(bluePrint, i)), new ItemStack(circuit, 1, i));
        }
    }

    static {
        register(new ICircuitDataProvider() {
            @Override
            public List<ItemStack> getComponents() {
                return Lists.newArrayList(circuit(1), circuit(1), circuit(2));
            }
        }, EnumCircuit.SMALL);
        register(new ICircuitDataProvider() {
            @Override
            public List<ItemStack> getComponents() {
                return Lists.newArrayList(circuit(1), circuit(3), circuit(3));
            }
        }, EnumCircuit.SMALL);
        register(new ICircuitDataProvider() {
            @Override
            public List<ItemStack> getComponents() {
                return Lists.newArrayList(circuit(2), circuit(1), circuit(4));
            }
        }, EnumCircuit.SMALL);
    }

    private static ItemStack circuit(int i){
        return new ItemStack(Components.component, 1, i);
    }
}
