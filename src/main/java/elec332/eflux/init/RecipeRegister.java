package elec332.eflux.init;

import elec332.core.java.JavaHelper;
import elec332.core.util.ItemStackHelper;
import elec332.core.util.OredictHelper;
import elec332.core.util.recipes.IDefaultRecipe;
import elec332.core.util.recipes.RecipeHelper;
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
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

import static elec332.eflux.EFlux.random;
import static elec332.eflux.init.BlockRegister.cable;
import static elec332.eflux.init.BlockRegister.*;
import static elec332.eflux.init.ItemRegister.*;
import static elec332.eflux.util.EnumMachines.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;

/**
 * Created by Elec332 on 13-1-2016.
 */
public final class RecipeRegister {

    public static void registerRecipes(){
        registerCraftingRecipes();
        registerFurnaceRecipes();
        registerEFluxRecipes();
    }

    private static void registerCraftingRecipes(){
        //TEMP
        registerShapedRecipe(compressedIngot, "IOI", "IDI", "OIO", 'I', IRON_INGOT, 'O', OBSIDIAN, 'D', DIAMOND);

        registerShapedRecipe(new ItemStack(wrench), "S S", " I ", " I ", 'S', silverIngot, 'I', IRON_INGOT);
        registerShapedRecipe(new ItemStack(multiBlockCreator), "ZIZ", "WIW", "III", 'I', IRON_INGOT, 'Z', zincIngot, 'W', wrench);
        registerShapedRecipe(new ItemStack(multimeter), "IGI", "ZLZ", "ICI", 'I', IRON_INGOT, 'G', GLASS, 'Z', zincIngot, 'L', GLOWSTONE, 'C', copperCoil);

        registerShapedRecipe(COAL_GENERATOR.getBlock(), "CIC", "IZI", "IFI", 'C', copperIngot, 'I', IRON_INGOT, 'Z', copperCoil, 'F', FURNACE);

        registerShapedRecipe(conductiveCoil, "RRG", "RCR", "GRR", 'R', REDSTONE, 'G', GOLD_NUGGET, 'C', conductiveIngot);
        registerShapedRecipe(copperCoil, "RRG", "RCR", "GRR", 'R', REDSTONE, 'G', GOLD_NUGGET, 'C', copperIngot);
        registerShapedRecipe(silverCoil, "RRG", "RSR", "GRR", 'R', REDSTONE, 'G', GOLD_NUGGET, 'S', silverIngot);

        registerShapedRecipe(GROWTHLAMP.getBlock(), "ZCZ", "INI", "RGR", 'Z', zincIngot, 'C', copperCoil, 'I', IRON_INGOT, 'N', frameNormal.toItemStack(), 'R', REDSTONE, 'G', GLOWSTONE);
        registerShapedRecipe(CHUNKMAIN.getBlock(), "CAC", "DYD", "EOE", 'C', conductiveCoil, 'A', frameAdvanced.toItemStack(), 'D', DIAMOND, 'Y', ENDER_EYE, 'E', ENDER_PEARL, 'O', conductiveIngot);
        registerShapedRecipe(CHUNKSUB.getBlock(), "GEG", "YFY", "SYS", 'G', GOLD_INGOT, 'E', ENDER_PEARL, 'Y', ENDER_EYE, 'F', frameNormal.toItemStack(), 'S', silverIngot);
        registerShapedRecipe(TESLACOIL.getBlock(), "CCC", "CFC", "COC", 'C', copperIngot, 'F', frameNormal.toItemStack(), 'O', conductiveCoil);
        registerShapedRecipe(SCANNER.getBlock(), "IGI", "DGF", "CZC", 'I', IRON_INGOT, 'G', GLOWSTONE, 'D', DIAMOND, 'G', heatResistantGlass.toItemStack(), 'F', frameNormal.toItemStack(), 'C', silverCoil, 'Z', zincIngot);
        registerShapedRecipe(WASHER.getBlock(), "ZBI", "ZWF", "CGC", 'Z', zincIngot, 'B', BUCKET, 'I', IRON_INGOT, 'W', WOOL, 'F', frameNormal.toItemStack(), 'C', copperIngot, 'G', silverCoil);
        registerShapedRecipe(RUBBLESIEVE.getBlock(), "IBI", "TBF", "ICI", 'I', IRON_INGOT, 'B', IRON_BARS, 'T', TRAPDOOR, 'F', frameBasic.toItemStack(), 'C', copperCoil);

        registerShapedRecipe(powerInlet.toItemStack(), "IRI", "ISI", "CFC", 'I', IRON_INGOT, 'R', REDSTONE, 'S', silverCoil, 'C', copperIngot, 'F', frameNormal.toItemStack());

        registerShapedRecipe(frameBasic.toItemStack(), "CIC", "IZI", "CIC", 'C', copperIngot, 'I', IRON_INGOT, 'Z', zincIngot);
        registerShapedRecipe(frameNormal.toItemStack(), "ISI", "ZGZ", "ISI", 'I', IRON_INGOT, 'S', silverIngot, 'Z', zincIngot, 'G', GOLD_INGOT);
        registerShapedRecipe(new ItemStack(frameAdvanced.block, 2, frameAdvanced.meta), "CZC", "IDI", "CGC", 'C', compressedIngot, 'Z', silverIngot, 'I', IRON_INGOT, 'D', DIAMOND, 'G', GOLD_INGOT);
        registerShapedRecipe(itemGate.toItemStack(), "RTR", "IHI", "ZBZ", 'R', REDSTONE, 'T', TRAPDOOR, 'I', IRON_INGOT, 'H', HOPPER, 'Z', zincIngot, 'B', frameBasic.toItemStack());
        registerShapedRecipe(new ItemStack(heatResistantGlass.block, 4, heatResistantGlass.meta), "IGI", "GOG", "IGI", 'I', IRON_INGOT, 'G', GLASS, 'O', OBSIDIAN);
        registerShapedRecipe(laserLens.toItemStack(), "ZDZ", "IGI", "GDG", 'Z', zincIngot, 'D', DIAMOND, 'I', IRON_INGOT, 'G', heatResistantGlass.toItemStack());
        registerShapedRecipe(laserCore.toItemStack(), "CAC", "HSH", "GDG", 'C', conductiveCoil, 'A', frameAdvanced.toItemStack(), 'H', compressedIngot, 'S', silverCoil, 'G', GOLD_INGOT, 'D', DIAMOND);
        registerShapedRecipe(heater.toItemStack(), "SBS", "ZCZ", "IRI", 'S', silverCoil, 'B', frameBasic.toItemStack(), 'Z', zincIngot, 'C', copperCoil, 'I', IRON_INGOT, 'R', REDSTONE_BLOCK);
        registerShapedRecipe(monitor.toItemStack(), "IGI", "C C", "ZFZ", 'I', IRON_INGOT, 'G', heatResistantGlass.toItemStack(), 'C', conductiveCoil, 'Z', zincIngot, 'F', frameNormal.toItemStack());
        registerShapedRecipe(radiator.toItemStack(), "CBC", "I I", "CFC", 'C', copperIngot, 'B', IRON_BARS, 'I', IRON_INGOT, 'F', frameBasic.toItemStack());
        registerShapedRecipe(motor.toItemStack(), "CIC", "SDS", "FGR", 'C', copperIngot, 'I', IRON_INGOT, 'S', conductiveCoil, 'D', compressedIngot, 'F', frameNormal.toItemStack(), 'G', copperCoil, 'R', REDSTONE);
        registerShapedRecipe(precisionMotor.toItemStack(), "SGS", "CMC", "R R", 'S', silverIngot, 'G', GOLD_INGOT, 'C', copperCoil, 'M', motor.toItemStack(), 'R', REDSTONE);
        registerShapedRecipe(dustStorage.toItemStack(), "CBC", "ICI", "FGF", 'C', copperIngot, 'B', IRON_BARS, 'I', IRON_INGOT, 'C', CHEST, 'F', frameNormal.toItemStack(), 'G', itemGate.toItemStack());

        registerShapedRecipe(new ItemStack(cable, 5, 0), "RRR", "SCS", "RRR", 'R', REDSTONE, 'S', silverIngot, 'C', copperIngot);
        registerShapedRecipe(new ItemStack(cable, 3, 1), "RRR", "CGC", "RRR", 'R', REDSTONE, 'G', GOLD_INGOT, 'C', conductiveIngot);
        registerShapedRecipe(new ItemStack(cable, 1, 2), "RER", "CSC", "RER", 'R', REDSTONE, 'S', silverIngot, 'C', conductiveIngot, 'E', ENDER_PEARL);

        registerShapedRecipe(BlockRegister.areaMover, "CGC", "ELE", "ISI", 'C', compressedIngot, 'G', heatResistantGlass.toItemStack(), 'E', ENDER_EYE, 'L', CHUNKSUB.getBlock(), 'I', IRON_INGOT, 'S', silverIngot);
        registerShapedRecipe(new ItemStack(ItemRegister.areaMover), " E ", "IMI", "ZRZ", 'E', ENDER_EYE, 'I', IRON_INGOT, 'M', multimeter, 'Z', zincIngot, 'R', REDSTONE);

        RecipeHelper.getCraftingManager().addShapelessRecipe(new ItemStack(dustConductive.getItem(), 3, dustConductive.getItemDamage()), dustGold, dustSilver, dustTin);

        registerShapedRecipe(carbonMesh, "CCC", "CCC", "CCC", 'C', dustCoal);

        RecipeHelper.getCraftingManager().addShapelessRecipe(new ItemStack(manual), BOOK, REDSTONE);

        //todo: nbt check for below
        registerShapedRecipe(smallUnrefinedBoard, "RIR", "CCC", "GDG", 'R', REDSTONE, 'I', new ItemStack(DYE, 1, 4), 'C', copperIngot, 'G', new ItemStack(DYE, 1, 8), 'D', dustIron);
        registerShapedRecipe(normalUnrefinedBoard, "CSC", "RIL", "CSC", 'C', copperIngot, 'S', silverIngot, 'R', REDSTONE, 'I', smallUnrefinedBoard, 'L', new ItemStack(DYE, 1, 15));
        registerShapedRecipe(advancedUnrefinedBoard, "CBC", "BLB", "SCS", 'C', conductiveIngot, 'B', normalUnrefinedBoard, 'L', new ItemStack(DYE, 1, 11), 'S', GOLD_INGOT);

    }

    private static void registerFurnaceRecipes(){
        registerSmelting(dustIron, IRON_INGOT);
        registerSmelting(dustGold, GOLD_INGOT);
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
        RecipeHelper.getCraftingManager().registerRecipe(new DustRecipe(), "dusts");
        //RecipeHelper.registerRecipeSorter(new EFluxResourceLocation("dusts"), DustRecipe.class);
        EFluxFurnaceRecipes.getInstance().registerRecipe(new IEFluxFurnaceRecipe() {

            @Override
            public boolean accepts(@Nonnull ItemStack input) {
                if (!(input.getItem() != groundMesh && input.getTagCompound() != null)){
                    return false;
                }
                DustPile dustPile = DustPile.fromNBT(input.getTagCompound());
                return dustPile.scanned && dustPile.pure && dustPile.clean && dustPile.getAmount("dustGold") == 3 && dustPile.getAmount("dustTin") == 3 && dustPile.getAmount("dustSilver") == 3 && dustPile.getSize() == 9;
            }

            @Override
            public ItemStack getOutput(@Nonnull ItemStack stack) {
                ItemStack ret = conductiveIngot.copy();
                ret.stackSize = 1 + (EFlux.random.nextFloat() > .7 ? 1 : 0);
                return ret;
            }

            @Override
            public float getExperience(@Nonnull ItemStack input) {
                return 0.9f;
            }

        });
        //CircuitHandler.register();
    }

    @SuppressWarnings("all")
    private static class DustRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IDefaultRecipe {

        @Override
        public boolean matches(InventoryCrafting inv, World worldIn) {
            int total = 0;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (ItemStackHelper.isStackValid(stack)){
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
                if (ItemStackHelper.isStackValid(stack) && stack.getItem() == ItemRegister.groundMesh){
                    DustPile d2 = DustPile.fromNBT(stack.getTagCompound());
                    dustPile.add(d2);
                }
            }
            if (dustPile.getSize() > 9){
                return null;
            }
            ItemStack ret = dustPile.getContentStack(1);
            if (ret != null) {
                return ret;
            }
            ret = new ItemStack(groundMesh);
            ret.setTagCompound(dustPile.toNBT());
            return ret;
        }

        @Override
        public boolean canFit(int width, int height) {
            return width >= 2 && height >= 2;
        }

        @Override
        public ItemStack getRecipeOutput() {
            return ItemStackHelper.NULL_STACK;
        }

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
        RecipeHelper.getCraftingManager().addRecipe(stack, params);//RecipeHelper.registerRecipe(RecipeHelper.SHAPED_RECIPE_FUNCTION, Preconditions.checkNotNull(stack), params);
    }

}
