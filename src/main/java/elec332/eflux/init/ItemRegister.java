package elec332.eflux.init;

import elec332.core.util.RegistryHelper;
import elec332.eflux.api.circuit.EnumCircuit;
import elec332.eflux.circuit.ICircuitDataProvider;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.items.*;
import elec332.eflux.items.ender.ItemEnderInventory;
import elec332.eflux.items.ender.capability.ItemEFluxEnderCapabilityPlayerInventory;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 24-2-2015.
 */
public final class ItemRegister {

    public static Item wrench, multimeter,  groundMesh, areaMover, multiBlockCreator, manual, entangledEnder, nullBlueprint;

    public static ItemEFluxElectricComponents components, brokenComponents;
    @SuppressWarnings("all")
    private static Item efluxItems, ingot, coil, dusts, cable, unrefinedBoard;
    public static ItemStack copperIngot, tinIngot, zincIngot, silverIngot, copperCoil, silverCoil, conductiveCoil, compressedIngot, carbonPlate, scrap, conductiveIngot, carbonMesh;
    //Dusts
    public static ItemStack dustIron, dustGold, dustCopper, dustZinc, dustSilver, dustCoal, dustStone, dustTin, dustConductive;
    //MultiParts
    public static ItemStack cableBasic, cableNormal, cableAdvanced;
    //Circuit Boards
    public static ItemStack smallUnrefinedBoard, normalUnrefinedBoard, advancedUnrefinedBoard;
    //Ender stuff
    public static Item enderLink, enderConfigurator, enderInventoryViewer;
    public static Item enderCapabilityPlayerInventory;
    //Misc
    public static ItemStack redstoneUpgrade;

    public static void init(){

        Item cable = RegistryHelper.register(new ItemBlock(BlockRegister.cable){

            @Override
            public int getMetadata(int damage) {
                return damage;
            }

            @Override
            @Nonnull
            public String getUnlocalizedName(ItemStack stack) {
                return super.getUnlocalizedName(stack)+"."+stack.getMetadata();
            }

        }.setRegistryName(BlockRegister.cable.getRegistryName()));
        cableBasic = new ItemStack(cable, 1, 0);
        cableNormal = new ItemStack(cable, 1, 1);
        cableAdvanced = new ItemStack(cable, 1, 2);


        multimeter = RegistryHelper.register(new ItemEFluxMultiMeter());
        wrench = RegistryHelper.register(new ItemEFluxWrench());
        groundMesh = RegistryHelper.register(new ItemEFluxGroundMesh());
        areaMover = RegistryHelper.register(new ItemEFluxAreaMover());
        multiBlockCreator = RegistryHelper.register(new ItemEFluxMultiBlockCreator());
        entangledEnder = RegistryHelper.register(new ItemEFluxInfusedEnder());

        efluxItems = new ItemEFluxGenerics();
        RegistryHelper.register(efluxItems);
        carbonPlate = new ItemStack(efluxItems, 1, 0);
        scrap = new ItemStack(efluxItems, 1, 1);
        carbonMesh = new ItemStack(efluxItems, 1, 2);

        ingot = new ItemEFluxIngots();
        RegistryHelper.register(ingot);
        copperIngot = new ItemStack(ingot, 1, 0);
        tinIngot = new ItemStack(ingot, 1, 1);
        zincIngot = new ItemStack(ingot, 1, 2);
        silverIngot = new ItemStack(ingot, 1, 3);
        compressedIngot = new ItemStack(ingot, 1, 4);
        conductiveIngot = new ItemStack(ingot, 1, 5);

        coil = new ItemEFluxCoils();
        RegistryHelper.register(coil);
        copperCoil = new ItemStack(coil, 1, 0);
        silverCoil = new ItemStack(coil, 1, 1);
        conductiveCoil = new ItemStack(coil, 1, 2);

        dusts = new ItemEFluxDusts();
        RegistryHelper.register(dusts);
        dustIron = new ItemStack(dusts, 1, 0);
        dustGold = new ItemStack(dusts, 1, 1);
        dustCopper = new ItemStack(dusts, 1, 2);
        dustZinc = new ItemStack(dusts, 1, 3);
        dustSilver = new ItemStack(dusts, 1, 4);
        dustCoal = new ItemStack(dusts, 1, 5);
        dustStone = new ItemStack(dusts, 1, 6);
        dustTin = new ItemStack(dusts, 1, 7);
        dustConductive = new ItemStack(dusts, 1, 8);

        unrefinedBoard = new ItemEFluxCircuit();
        RegistryHelper.register(unrefinedBoard);
        smallUnrefinedBoard = ((ItemEFluxCircuit)unrefinedBoard).createNewEmptyCircuit(EnumCircuit.SMALL);
        normalUnrefinedBoard = ((ItemEFluxCircuit)unrefinedBoard).createNewEmptyCircuit(EnumCircuit.NORMAL);
        advancedUnrefinedBoard = ((ItemEFluxCircuit)unrefinedBoard).createNewEmptyCircuit(EnumCircuit.ADVANCED);

        components = RegistryHelper.register(new ItemEFluxElectricComponents());
        brokenComponents = RegistryHelper.register(new ItemEFluxElectricComponents.BrokenComponents());
        manual = RegistryHelper.register(new ItemEFluxManual());
        nullBlueprint = RegistryHelper.register(new ItemEFluxBluePrint());
        enderLink = RegistryHelper.register(new ItemEFluxEnderLink());
        enderConfigurator = RegistryHelper.register(new ItemEFluxEnderConfigurator());
        enderCapabilityPlayerInventory = RegistryHelper.register(new ItemEFluxEnderCapabilityPlayerInventory());
        enderInventoryViewer = RegistryHelper.register(new ItemEnderInventory());
        //What was I doing here again?
        redstoneUpgrade = new ItemStack(RegistryHelper.register(new AbstractEFluxItem("redstoneUpgrade"){}).setMaxStackSize(1));

        registerOreDictionary();
        registerCircuits();
    }

    private static void registerOreDictionary(){
        List<String> components = ((ItemEFluxDusts) dusts).getComponents();
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
    }

    private static void registerCircuits() {
        RegistryHelper.register(new ICircuitDataProvider() {

            @Override
            @Nonnull
            public ItemStack[] getComponents() {
                return new ItemStack[]{
                        null, circuit(3), circuit(4), circuit(2), circuit(1), circuit(1), circuit(4), circuit(3), null
                };
            }

            @Override
            @Nonnull
            public EnumCircuit getCircuitType() {
                return EnumCircuit.NORMAL;
            }

        }, new EFluxResourceLocation("shock"));

    }

    private static ItemStack circuit(int i){
        return new ItemStack(components, 1, i);
    }

    static void registerMultiPartItems(){
        //cable = GameRegistry.register(new ItemEFluxCable());
        cableBasic = new ItemStack(cable, 1, 0);
        cableNormal = new ItemStack(cable, 1, 1);
        cableAdvanced = new ItemStack(cable, 1, 2);
    }

}
