package elec332.eflux.client;

import elec332.eflux.client.manual.ManualCategory;
import elec332.eflux.client.manual.ManualHandler;
import elec332.eflux.client.manual.pages.PageMultiBlockStructure;
import elec332.eflux.client.manual.pages.PageText;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.init.MultiBlockRegister;
import elec332.eflux.util.EnumMachines;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 1-2-2016.
 */
public class ManualInit {

    public static void init(){

        ManualHandler.instance.getMainCategory().addPage(new PageText(manualTitle("main")).setText(manualText("main")));

        newCategoryWithTextPage("powerSystem", new ItemStack(BlockRegister.cable)).addPage(PageText.newPageWithText("eflux.manual.text.powerSystem.1")).setLastCategory();

        newCategoryWithTextPage("multiBlocks", BlockRegister.powerInlet.toItemStack());
        newCategoryWithTextPage("multiBlocks/compressor", BlockRegister.monitor.toItemStack()).addPage(new PageMultiBlockStructure(manualTitle("compressor"), MultiBlockRegister.BlockStructures.compressor, 80, 100, compressorManual)).setLastCategory();
        newCategoryWithTextPage("multiBlocks/grinder", BlockRegister.motor.toItemStack()).addPage(new PageMultiBlockStructure(manualTitle("grinder"), MultiBlockRegister.BlockStructures.grinder, 80, 100, grinderManual)).setLastCategory();
        newCategoryWithTextPage("multiBlocks/furnace", BlockRegister.heater.toItemStack()).addPage(new PageMultiBlockStructure(manualTitle("furnace"), MultiBlockRegister.BlockStructures.furnace, 80, 100, furnaceManual)).setLastCategory();
        newCategoryWithTextPage("multiBlocks/laser", BlockRegister.laserCore.toItemStack()).addPage(new PageMultiBlockStructure(manualTitle("laser"), MultiBlockRegister.BlockStructures.laser, 80, 100, laserManual)).setLastCategory();

        newCategoryWithTextPage("machines", new ItemStack(EnumMachines.SCANNER.getBlock()));
        newCategoryWithTextPage("machines/chunkLoader", new ItemStack(EnumMachines.CHUNKMAIN.getBlock())).addPage(PageText.newPageWithText("eflux.manual.text.chunkLoader.1")).setLastCategory();
        newCategoryWithTextPage("machines/areaMover", new ItemStack(BlockRegister.areaMover)).setLastCategory();
        newCategoryWithTextPage("machines/miscMachines", new ItemStack(EnumMachines.TESLACOIL.getBlock())).addPage(PageText.newPageWithText(manualText("miscMachines.1"))).setLastCategory();

        ManualCategory c = newCategoryWithTextPage("oreProcessing", BlockRegister.dustStorage.toItemStack()).setLastCategory().addPage(new PageText(manualTitle("oreProcessing.1")).setText(manualText("oreProcessing.1")));
        c.addPage(new PageText(manualTitle("oreProcessing.2")).setText(manualText("oreProcessing.2"))).addPage(new PageText(manualTitle("oreProcessing.3")).setText(manualText("oreProcessing.3"))).addPage(new PageText(manualTitle("oreProcessing.4")).setText(manualText("oreProcessing.4")));
        c.addPage(new PageText(manualTitle("oreProcessing.5")).setText(manualText("oreProcessing.5"))).addPage(new PageText(manualTitle("oreProcessing.6")).setText(manualText("oreProcessing.6")));
    }

    private static ManualCategory newCategoryWithTextPage(String title, ItemStack display){
        String[] s = title.split("/");
        String name = s[s.length - 1];
        return ManualHandler.instance.registerCategory(title).setDisplayStack(display).setUnlocalisedName(manualTitle(name)).addPage(PageText.newPageWithText(manualText(name)));
    }

    private static String manualTitle(String title){
        return "eflux.manual."+title;
    }

    private static String manualText(String title){
        return "eflux.manual.text."+title;
    }

    private static final PageMultiBlockStructure.IMultiBlockStructureManualHelper compressorManual, grinderManual, furnaceManual, laserManual;

    static {
        compressorManual = new PageMultiBlockStructure.IMultiBlockStructureManualHelper.DefaultImpl(){
            @Override
            public void getNote(int length, int width, int height, List<String> list) {
                if (width == 1){
                    if (height == 1){
                        if (length != 1){
                            list.add("Should be facing inwards.");
                            return;
                        }
                    }
                    if (length == 1){
                        if (height == 0) {
                            list.add("Should be in output mode.");
                            list.add("Should be facing downwards");
                            return;
                        }
                        if (height == 2){
                            list.add("Should be in input mode");
                            list.add("Should be facing upwards.");
                            return;
                        }
                    }
                }
                if (length == 1 && height == 1 && width == 2){
                    list.add("Should be facing inwards.");
                }
            }
        };
        grinderManual = new PageMultiBlockStructure.IMultiBlockStructureManualHelper.DefaultImpl(){
            @Override
            public void getNote(int length, int width, int height, List<String> list) {
                if (width == 1){
                    if (height == 1 && length != 1){
                        list.add("Should be facing towards the outside.");
                    } else if (height == 2 && length == 1){
                        list.add("Should be facing upwards.");
                        list.add("Should be in input mode.");
                    }
                }
            }
        };
        furnaceManual = new PageMultiBlockStructure.IMultiBlockStructureManualHelper.DefaultImpl(){
            @Override
            public void getNote(int length, int width, int height, List<String> list) {
                if (width == 1){
                    if (height == 1){
                        if (length == 0 || length == 2){
                            list.add("Should be facing towards the inside.");
                        }
                    } else if (length == 1){
                        if (height == 0){
                            list.add("Should be facing downwards.");
                            list.add("Should be in output mode.");
                        } else if (height == 2){
                            list.add("Should be facing upwards.");
                            list.add("Should be in input mode.");
                        }
                    }
                } else if (length == 1 && width == 2 && height == 1){
                    list.add("Should be facing towards the inside.");
                }
            }
        };
        laserManual = new PageMultiBlockStructure.IMultiBlockStructureManualHelper.DefaultImpl(){
            @Override
            public void getNote(int length, int width, int height, List<String> list) {
                if (height == 1){
                    if (width == 0){
                        if (length == 2 || length == 3){
                            list.add("Should be facing to the outside.");
                        }
                    } else if (width == 1){
                        if (length == 1 || length == 3){
                            list.add("Should be facing to the laser output side.");
                        }
                    } else if (width == 2){
                        if (length > 0 && length < 4){
                            list.add("Should be facing to the outside.");
                        }
                    }
                } else if (height == 0 && length == 4 && width == 1){
                    list.add("Should be in output mode.");
                }
            }
        };
    }

}
