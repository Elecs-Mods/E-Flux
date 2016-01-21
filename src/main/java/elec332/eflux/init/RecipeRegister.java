package elec332.eflux.init;

import elec332.core.java.JavaHelper;
import elec332.core.util.OredictHelper;
import elec332.eflux.EFlux;
import elec332.eflux.recipes.CompressorRecipes;
import elec332.eflux.recipes.EFluxFurnaceRecipes;
import elec332.eflux.recipes.IEFluxFurnaceRecipe;
import elec332.eflux.util.DustPile;
import elec332.eflux.util.GrinderRecipes;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static elec332.eflux.EFlux.random;
import static elec332.eflux.init.BlockRegister.*;
import static elec332.eflux.init.ItemRegister.*;
import static elec332.eflux.util.EnumMachines.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class RecipeRegister {

    public static void registerRecipes(){
        registerCraftingRecipes();
        registerFurnaceRecipes();
        registerEFluxRecipes();
    }

    private static void registerCraftingRecipes(){
        //TEMP
        registerShapedRecipe(compressedIngot, "IOI", "IDI", "OIO", 'I', iron_ingot, 'O', obsidian, 'D', diamond);

        registerShapedRecipe(new ItemStack(wrench), "S S", " I ", " I ", 'S', silverIngot, 'I', iron_ingot);
        registerShapedRecipe(new ItemStack(multiBlockCreator), "ZIZ", "WIW", "III", 'I', iron_ingot, 'Z', zincIngot, 'W', wrench);
        registerShapedRecipe(new ItemStack(multimeter), "IGI", "ZLZ", "ICI", 'I', iron_ingot, 'G', glass, 'Z', zincIngot, 'L', glowstone, 'C', copperCoil);

        registerShapedRecipe(COAL_GENERATOR.getBlock(), "CIC", "IZI", "IFI", 'C', copperIngot, 'I', iron_ingot, 'Z', copperCoil, 'F', furnace);

        registerShapedRecipe(conductiveCoil, "RRG", "RCR", "GRR", 'R', redstone, 'G', gold_nugget, 'C', conductiveIngot);
        registerShapedRecipe(copperCoil, "RRG", "RCR", "GRR", 'R', redstone, 'G', gold_nugget, 'C', copperIngot);
        registerShapedRecipe(silverCoil, "RRG", "RSR", "GRR", 'R', redstone, 'G', gold_nugget, 'S', silverIngot);

        registerShapedRecipe(GROWTHLAMP.getBlock(), "ZCZ", "INI", "RGR", 'Z', zincIngot, 'C', copperCoil, 'I', iron_ingot, 'N', frameNormal.toItemStack(), 'R', redstone, 'G', glowstone);
        registerShapedRecipe(CHUNKMAIN.getBlock(), "CAC", "DYD", "EOE", 'C', conductiveCoil, 'A', frameAdvanced.toItemStack(), 'D', diamond, 'Y', ender_eye, 'E', ender_pearl, 'O', conductiveIngot);
        registerShapedRecipe(CHUNKSUB.getBlock(), "GEG", "YFY", "SYS", 'G', gold_ingot, 'E', ender_pearl, 'Y', ender_eye, 'F', frameNormal.toItemStack(), 'S', silverIngot);
        registerShapedRecipe(TESLACOIL.getBlock(), "CCC", "CFC", "COC", 'C', copperIngot, 'F', frameNormal.toItemStack(), 'O', conductiveCoil);
        registerShapedRecipe(SCANNER.getBlock(), "IGI", "DGF", "CZC", 'I', iron_ingot, 'G', glowstone, 'D', diamond, 'G', heatResistantGlass.toItemStack(), 'F', frameNormal.toItemStack(), 'C', silverCoil, 'Z', zincIngot);
        registerShapedRecipe(WASHER.getBlock(), "ZBI", "ZWF", "CGC", 'Z', zincIngot, 'B', bucket, 'I', iron_ingot, 'W', wool, 'F', frameNormal.toItemStack(), 'C', copperIngot, 'G', silverCoil);
        registerShapedRecipe(RUBBLESIEVE.getBlock(), "IBI", "TBF", "ICI", 'I', iron_ingot, 'B', iron_bars, 'T', trapdoor, 'F', frameBasic.toItemStack(), 'C', copperCoil);

        registerShapedRecipe(powerInlet.toItemStack(), "IRI", "ISI", "CFC", 'I', iron_ingot, 'R', redstone, 'S', silverCoil, 'C', copperIngot, 'F', frameNormal.toItemStack());

        registerShapedRecipe(frameBasic.toItemStack(), "CIC", "IZI", "CIC", 'C', copperIngot, 'I', iron_ingot, 'Z', zincIngot);
        registerShapedRecipe(frameNormal.toItemStack(), "ISI", "ZGZ", "ISI", 'I', iron_ingot, 'S', silverIngot, 'Z', zincIngot, 'G', gold_ingot);
        registerShapedRecipe(new ItemStack(frameAdvanced.block, 2, frameAdvanced.meta), "CZC", "IDI", "CGC", 'C', compressedIngot, 'Z', silverIngot, 'I', iron_ingot, 'D', diamond, 'G', gold_ingot);
        registerShapedRecipe(itemGate.toItemStack(), "RTR", "IHI", "ZBZ", 'R', redstone, 'T', trapdoor, 'I', iron_ingot, 'H', hopper, 'Z', zincIngot, 'B', frameBasic.toItemStack());
        registerShapedRecipe(new ItemStack(heatResistantGlass.block, 4, heatResistantGlass.meta), "IGI", "GOG", "IGI", 'I', iron_ingot, 'G', glass, 'O', obsidian);
        registerShapedRecipe(laserLens.toItemStack(), "ZDZ", "IGI", "GDG", 'Z', zincIngot, 'D', diamond, 'I', iron_ingot, 'G', heatResistantGlass.toItemStack());
        registerShapedRecipe(laserCore.toItemStack(), "CAC", "HSH", "GDG", 'C', conductiveCoil, 'A', frameAdvanced.toItemStack(), 'H', compressedIngot, 'S', silverCoil, 'G', gold_ingot, 'D', diamond);
        registerShapedRecipe(heater.toItemStack(), "SBS", "ZCZ", "IRI", 'S', silverCoil, 'B', frameBasic.toItemStack(), 'Z', zincIngot, 'C', copperCoil, 'I', iron_ingot, 'R', redstone_block);
        registerShapedRecipe(monitor.toItemStack(), "IGI", "C C", "ZFZ", 'I', iron_ingot, 'G', heatResistantGlass.toItemStack(), 'C', conductiveCoil, 'Z', zincIngot, 'F', frameNormal.toItemStack());
        registerShapedRecipe(radiator.toItemStack(), "CBC", "I I", "CFC", 'C', copperIngot, 'B', iron_bars, 'I', iron_ingot, 'F', frameBasic.toItemStack());
        registerShapedRecipe(motor.toItemStack(), "CIC", "SDS", "FGR", 'C', copperIngot, 'I', iron_ingot, 'S', conductiveCoil, 'D', compressedIngot, 'F', frameNormal.toItemStack(), 'G', copperCoil, 'R', redstone);
        registerShapedRecipe(precisionMotor.toItemStack(), "SGS", "CMC", "R R", 'S', silverIngot, 'G', gold_ingot, 'C', copperCoil, 'M', motor.toItemStack(), 'R', redstone);
        registerShapedRecipe(dustStorage.toItemStack(), "CBC", "ICI", "FGF", 'C', copperIngot, 'B', iron_bars, 'I', iron_ingot, 'C', chest, 'F', frameNormal.toItemStack(), 'G', itemGate.toItemStack());

        registerShapedRecipe(new ItemStack(cable, 5, 0), "RRR", "SCS", "RRR", 'R', redstone, 'S', silverIngot, 'C', copperIngot);
        registerShapedRecipe(new ItemStack(cable, 3, 1), "RRR", "CGC", "RRR", 'R', redstone, 'G', gold_ingot, 'C', conductiveIngot);
        registerShapedRecipe(new ItemStack(cable, 1, 2), "RER", "CSC", "RER", 'R', redstone, 'S', silverIngot, 'C', conductiveIngot, 'E', ender_pearl);

        registerShapedRecipe(BlockRegister.areaMover, "CGC", "ELE", "ISI", 'C', compressedIngot, 'G', heatResistantGlass.toItemStack(), 'E', ender_eye, 'L', CHUNKSUB.getBlock(), 'I', iron_ingot, 'S', silverIngot);
        registerShapedRecipe(new ItemStack(ItemRegister.areaMover), " E ", "IMI", "ZRZ", 'E', ender_eye, 'I', iron_ingot, 'M', multimeter, 'Z', zincIngot, 'R', redstone);

        CraftingManager.getInstance().addShapelessRecipe(new ItemStack(dustConductive.getItem(), 3, dustConductive.getItemDamage()), dustGold, dustSilver, dustTin);

        registerShapedRecipe(carbonMesh, "CCC", "CCC", "CCC", 'C', dustCoal);

    }

    private static void registerFurnaceRecipes(){
        registerSmelting(dustIron, iron_ingot);
        registerSmelting(dustGold, gold_ingot);
        registerSmelting(dustCopper, copperIngot);
        registerSmelting(dustConductive, conductiveIngot);
        registerSmelting(dustSilver, silverIngot);
        registerSmelting(dustTin, tinIngot);
        registerSmelting(dustZinc, zincIngot);
        registerSmelting(oreCopper.toItemStack(), copperIngot);
        registerSmelting(oreSilver.toItemStack(), silverIngot);
        registerSmelting(oreTin.toItemStack(), tinIngot);
        registerSmelting(oreZinc.toItemStack(), zincIngot);
    }

    private static void registerEFluxRecipes(){
        CompressorRecipes.getInstance().registerRecipe(carbonMesh, carbonPlate, false);
        GrinderRecipes.instance.addRecipe("vanillaCoal", new GrinderRecipes.OreDictStack("dustCoal", 18));
        GrinderRecipes.instance.addRecipe(new GrinderRecipes.IGrinderRecipe() {

            @Override
            public boolean accepts(ItemStack stack) {
                return JavaHelper.doesListContainPartially(OredictHelper.getOreNames(stack), "ore") && getDust(stack) != null;
            }

            @Override
            public GrinderRecipes.OreDictStack[] getOutput(ItemStack stack, int total) {
                String dust = getDust(stack);
                int stone = random.nextInt(total/3) + 1;
                GrinderRecipes.OreDictStack c = new GrinderRecipes.OreDictStack(dust, total - stone);
                GrinderRecipes.OreDictStack s = new GrinderRecipes.OreDictStack(GrinderRecipes.stoneDust, stone);
                return new GrinderRecipes.OreDictStack[]{c, s};
            }

            private String getDust(ItemStack stack){
                return JavaHelper.getFirstEntryContaining(OredictHelper.getAllDusts(), JavaHelper.getFirstEntryContaining(OredictHelper.getOreNames(stack), "ore").replace("ore", ""));
            }

        });
        GrinderRecipes.instance.addRecipe(new GrinderRecipes.IGrinderRecipe() {

            @Override
            public boolean accepts(ItemStack stack) {
                return JavaHelper.doesListContainPartially(OredictHelper.getOreNames(stack), "ingot");
            }

            @Override
            public GrinderRecipes.OreDictStack[] getOutput(ItemStack stack, int total) {
                String dust = JavaHelper.getFirstEntryContaining(OredictHelper.getAllDusts(), JavaHelper.getFirstEntryContaining(OredictHelper.getOreNames(stack), "ingot").replace("ingot", ""));
                return new GrinderRecipes.OreDictStack[]{new GrinderRecipes.OreDictStack(dust, total)};
            }

        });
        GameRegistry.addRecipe(new IRecipe() {

            @Override
            public boolean matches(InventoryCrafting inv, World worldIn) {
                int total = 0;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (stack != null){
                        Item item = stack.getItem();
                        if (item == null || item != ItemRegister.groundMesh){
                            return false;
                        }
                        DustPile d = DustPile.fromNBT(stack.getTagCompound());
                        total += d.getSize();
                        if (total > 9){
                            return false;
                        }
                    }
                }
                return total <= 9 && total > 0;
            }

            @Override
            public ItemStack getCraftingResult(InventoryCrafting inv) {
                DustPile dustPile = DustPile.newDustPile();
                dustPile.scanned = true;
                dustPile.clean = true;
                dustPile.pure = true;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (stack != null && stack.getItem() == ItemRegister.groundMesh){
                        DustPile d2 = DustPile.fromNBT(stack.getTagCompound());
                        dustPile.add(d2);
                    }
                }
                if (dustPile.getSize() > 9){
                    return null;
                }
                ItemStack ret = dustPile.getContentStack(1);
                if (ret != null)
                    return ret;
                ret = new ItemStack(groundMesh);
                ret.setTagCompound(dustPile.toNBT());
                return ret;
            }

            @Override
            public int getRecipeSize() {
                return 4;
            }

            @Override
            public ItemStack getRecipeOutput() {
                return null;
            }

            @Override
            public ItemStack[] getRemainingItems(InventoryCrafting inv) {
                return ForgeHooks.defaultRecipeGetRemainingItems(inv);
            }

        });
        EFluxFurnaceRecipes.getInstance().registerRecipe(new IEFluxFurnaceRecipe() {

            @Override
            public boolean accepts(ItemStack input) {
                if (!(input.getItem() != groundMesh && input.getTagCompound() != null)){
                    return false;
                }
                DustPile dustPile = DustPile.fromNBT(input.getTagCompound());
                return dustPile.scanned && dustPile.pure && dustPile.clean && dustPile.getAmount("dustGold") == 3 && dustPile.getAmount("dustTin") == 3 && dustPile.getAmount("dustSilver") == 3 && dustPile.getSize() == 9;
            }

            @Override
            public ItemStack getOutput(ItemStack stack) {
                ItemStack ret = conductiveIngot.copy();
                ret.stackSize = 1 + (EFlux.random.nextFloat() > .7 ? 1 : 0);
                return ret;
            }

            @Override
            public float getExperience(ItemStack input) {
                return 0.9f;
            }

        });
    }

    private static void registerSmelting(ItemStack in, Item item){
        registerSmelting(in, new ItemStack(item));
    }

    private static void registerSmelting(ItemStack in, ItemStack out){
        FurnaceRecipes.instance().addSmeltingRecipe(in, out, 0.4f);
    }

    private static void registerShapedRecipe(Block block, Object... params){
        registerShapedRecipe(new ItemStack(block, 1), params);
    }

    private static void registerShapedRecipe(ItemStack stack, Object... params){
        GameRegistry.addShapedRecipe(stack, params);
    }

}
