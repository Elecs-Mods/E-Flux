package elec332.eflux.init;

import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.items.*;
import elec332.eflux.items.circuits.CircuitHandler;
import elec332.eflux.items.circuits.ICircuitDataProvider;
import elec332.eflux.items.circuits.UnrefinedBoard;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Elec332 on 24-2-2015.
 */
public final class ItemRegister {
    public static final ItemRegister instance = new ItemRegister();
    private ItemRegister(){
    }

    public static Item wrench, multimeter,  groundMesh, areaMover, multiBlockCreator, manual;
    @SuppressWarnings("all")
    private static Item EFluxItems, ingot, coil, dusts, cable, unrefinedBoard;
    public static ItemStack copperIngot, tinIngot, zincIngot, silverIngot, copperCoil, silverCoil, conductiveCoil, compressedIngot, carbonPlate, scrap, conductiveIngot, carbonMesh;
    //Dusts
    public static ItemStack dustIron, dustGold, dustCopper, dustZinc, dustSilver, dustCoal, dustStone, dustTin, dustConductive;
    //MultiParts
    public static ItemStack cableBasic, cableNormal, cableAdvanced;
    //Circuit Boards
    public static ItemStack smallUnrefinedBoard, normalUnrefinedBoard, advancedUnrefinedBoard;
    //Assembled Circuit Boards
    public static ItemStack shockBoard;
    //Misc
    public static ItemStack redstoneUpgrade;

    public void init(FMLInitializationEvent event){
        //if (ElecCore.developmentEnvironment)
        //    new Breaker();
        multimeter = new MultiMeter("MultiMeter");
        wrench = new Wrench("Wrench");
        Components.init();
        groundMesh = new GroundMesh().register();
        areaMover = new ItemAreaMover().register().setCreativeTab(EFlux.creativeTab);
        multiBlockCreator = new ItemMultiBlockCreator().register();

        EFluxItems = new EFluxItems();
        GameRegistry.registerItem(EFluxItems, "GenericItems");
        carbonPlate = new ItemStack(EFluxItems, 1, 0);
        scrap = new ItemStack(EFluxItems, 1, 1);
        carbonMesh = new ItemStack(EFluxItems, 1, 2);

        ingot = new EFluxItemsIngot();
        GameRegistry.registerItem(ingot, "EFluxIngot");
        copperIngot = new ItemStack(ingot, 1, 0);
        tinIngot = new ItemStack(ingot, 1, 1);
        zincIngot = new ItemStack(ingot, 1, 2);
        silverIngot = new ItemStack(ingot, 1, 3);
        compressedIngot = new ItemStack(ingot, 1, 4);
        conductiveIngot = new ItemStack(ingot, 1, 5);

        coil = new EFluxCoils();
        GameRegistry.registerItem(coil, "EFluxCoil");
        copperCoil = new ItemStack(coil, 1, 0);
        silverCoil = new ItemStack(coil, 1, 1);
        conductiveCoil = new ItemStack(coil, 1, 2);

        dusts = new EFluxDusts();
        GameRegistry.registerItem(dusts, "EFluxDust");
        dustIron = new ItemStack(dusts, 1, 0);
        dustGold = new ItemStack(dusts, 1, 1);
        dustCopper = new ItemStack(dusts, 1, 2);
        dustZinc = new ItemStack(dusts, 1, 3);
        dustSilver = new ItemStack(dusts, 1, 4);
        dustCoal = new ItemStack(dusts, 1, 5);
        dustStone = new ItemStack(dusts, 1, 6);
        dustTin = new ItemStack(dusts, 1, 7);
        dustConductive = new ItemStack(dusts, 1, 8);

        manual = new ItemManual().register();

        redstoneUpgrade = new ItemStack(new EFluxItem("redstoneUpgrade"){}.register().setMaxStackSize(1));

        List<String> components = ((EFluxDusts) dusts).getComponents();
        for (int i = 0; i < components.size(); i++) {
            OreDictionary.registerOre(components.get(i), new ItemStack(dusts, 1, i));
        }

        OreDictionary.registerOre(GrinderRecipes.scrap, scrap);
        OreDictionary.registerOre(GrinderRecipes.stoneDust, dustStone);
        OreDictionary.registerOre("ingotCopper", copperIngot);
        OreDictionary.registerOre("ingotTin", tinIngot);
        OreDictionary.registerOre("ingotSilver", silverIngot);
        OreDictionary.registerOre("ingotZinc", zincIngot);
        OreDictionary.registerOre("ingotConductive", conductiveIngot);

        OreDictionary.registerOre("vanillaCoal", Items.COAL);

        registerCircuits();
    }

    private void registerCircuits(){
        unrefinedBoard = new UnrefinedBoard();
        GameRegistry.registerItem(unrefinedBoard, "UnrefinedBoard");
        smallUnrefinedBoard = new ItemStack(unrefinedBoard, 1, 0);
        normalUnrefinedBoard = new ItemStack(unrefinedBoard, 1, 1);
        advancedUnrefinedBoard = new ItemStack(unrefinedBoard, 1, 2);
        registerCircuits_();
    }

    private void registerCircuits_(){
        CircuitHandler.register(new ICircuitDataProvider() {

            @Override
            public ItemStack[] getComponents() {
                return new ItemStack[]{
                        null, circuit(3), circuit(4), circuit(2), circuit(1), circuit(1), circuit(4), circuit(3), null
                };
            }

            @Override
            public String getName() {
                return "shock";
            }

        }, EnumCircuit.SMALL);

        shockBoard = getBoard(EnumCircuit.SMALL, "shock");

    }

    private static ItemStack circuit(int i){
        return new ItemStack(Components.component, 1, i);
    }

    private static ItemStack getBoard(EnumCircuit circuit, String name){
        return new ItemStack(CircuitHandler.get(circuit).getCircuitFromName(name));
    }

    protected void initMultiPartItems(){
        cable = new ItemCable().register();
        cableBasic = new ItemStack(cable, 1, 0);
        cableNormal = new ItemStack(cable, 1, 1);
        cableAdvanced = new ItemStack(cable, 1, 2);
    }

}
