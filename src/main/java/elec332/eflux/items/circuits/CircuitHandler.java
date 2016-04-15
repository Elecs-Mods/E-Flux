package elec332.eflux.items.circuits;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.recipes.old.RecipeRegistry;
import elec332.eflux.util.RecipeItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Elec332 on 4-5-2015.
 */
public class CircuitHandler {

    private static final Map<EnumCircuit, CircuitHandler> mappings;
    private static final Set<EnumCircuit> registeredTypes;
    private static boolean init = false;

    public static void register(ICircuitDataProvider provider, EnumCircuit circuit){
        if (init) {
            throw new UnsupportedOperationException("Cannot register circuits after init");
        }
        get(circuit).registerCircuit(provider);
    }

    public static CircuitHandler get(EnumCircuit circuit) {
        if (circuit == null) {
            throw new IllegalArgumentException();
        }
        return mappings.get(circuit);
    }

    public static void register(){
        init = true;
        for (EnumCircuit circuit : registeredTypes) {
            get(circuit).registerData();
        }
    }

    ////////////////////////////////////////////////////////////////////

    private CircuitHandler(EnumCircuit circuit){
        this.circuit = circuit;
        mapping = Maps.newHashMap();
        nameToBluePrintAndBoardMap = Maps.newHashMap();
    }

    private final EnumCircuit circuit;
    private final Map<String, ICircuitDataProvider> mapping;
    private final Map<String, Pair<Item, Item>> nameToBluePrintAndBoardMap;

    private void registerCircuit(ICircuitDataProvider provider){
        String s = provider.getName();
        if (!mapping.containsKey(s)) {
            mapping.put(s, provider);
            nameToBluePrintAndBoardMap.put(s, Pair.of((Item)new BluePrint(s, circuit), (Item)new Circuit(s, circuit)));
        }
    }

    public ICircuitDataProvider fromName(String name){
        return mapping.get(name);
    }

    public Item getBluePrintFromName(String name){
        return nameToBluePrintAndBoardMap.get(name).getLeft();
    }

    public Item getCircuitFromName(String name){
        return nameToBluePrintAndBoardMap.get(name).getRight();
    }

    public ItemStack getUnrefinedCircuit(){
        return new ItemStack(ItemRegister.smallUnrefinedBoard.getItem(), 1, circuit.ordinal());
    }

    private void registerData(){
        for (String s : mapping.keySet()) {
            RecipeRegistry.instance.registerRecipe(EnumRecipeMachine.ETCHINGMACHINE, Lists.newArrayList(new RecipeItemStack(getUnrefinedCircuit()), new RecipeItemStack(getBluePrintFromName(s))), new ItemStack(getCircuitFromName(s)));
        }
    }

    static {
        mappings = Maps.newHashMap();
        registeredTypes = EnumSet.allOf(EnumCircuit.class);
        for (EnumCircuit circuit : registeredTypes){
            mappings.put(circuit, new CircuitHandler(circuit));
        }
    }

}
