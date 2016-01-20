package elec332.eflux.init;

import elec332.eflux.EFlux;
import elec332.eflux.items.*;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * Created by Elec332 on 24-2-2015.
 */
public class ItemRegister {
    public static final ItemRegister instance = new ItemRegister();
    private ItemRegister(){
    }

    public static Item wrench, multimeter,  groundMesh, areaMover, multiBlockCreator;
    @SuppressWarnings("all")
    private static Item EFluxItems, ingot, coil, dusts;
    public static ItemStack copperIngot, tinIngot, zincIngot, silverIngot, copperCoil, silverCoil, conductiveCoil, compressedIngot, carbonPlate, scrap, conductiveIngot;
    public static ItemStack dustIron, dustGold, dustCopper, dustZinc, dustSilver, dustCoal, dustStone, dustTin, dustConductive;

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

    }

}
