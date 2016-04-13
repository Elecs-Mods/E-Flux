package elec332.eflux;

import com.google.common.collect.Lists;
import elec332.core.config.ConfigWrapper;
import elec332.core.main.ElecCore;
import elec332.core.modBaseUtils.ModInfo;
import elec332.core.multiblock.MultiBlockRegistry;
import elec332.core.network.NetworkHandler;
import elec332.core.server.ServerHelper;
import elec332.core.util.EventHelper;
import elec332.core.util.MCModInfo;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.compat.Compat;
import elec332.eflux.compat.rf.RFCompat;
import elec332.eflux.compat.waila.WailaCompatHandler;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.grid.power.EventHandler;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.handler.PlayerEventHandler;
import elec332.eflux.init.*;
import elec332.eflux.items.circuits.CircuitHandler;
import elec332.eflux.proxies.CommonProxy;
import elec332.eflux.recipes.EFluxFurnaceRecipes;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.recipes.old.RecipeRegistry;
import elec332.eflux.util.CalculationHelper;
import elec332.eflux.util.Config;
import elec332.eflux.util.RecipeItemStack;
import elec332.eflux.world.WorldGenOres;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = EFlux.ModID, name = EFlux.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)"/*;required-after:mcmultipart@[1.1.0,)"*/,
        acceptedMinecraftVersions = ModInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class EFlux { //TODO

    public static final String ModName = "E-Flux";
    public static final String ModID = "EFlux";
    public static File baseFolder;

    @SidedProxy(clientSide = "elec332.eflux.proxies.ClientProxy", serverSide = "elec332.eflux.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static EFlux instance;
    public static Configuration config;
    public static CreativeTabs creativeTab;
    public static Logger logger;
    public static ConfigWrapper configWrapper;
    public static Random random;
    public static NetworkHandler networkHandler;
    public static MultiBlockRegistry multiBlockRegistry;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        EFluxAPI.dummyLoad();
        baseFolder = new File(event.getModConfigurationDirectory(), "E-Flux");
        config = new Configuration(new File(baseFolder, "EFlux.cfg"));
        creativeTab = new CreativeTabs("EFlux") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.ANVIL);  //TODO: replace with mod item, once we got a nice one
            }
        };
        logger = event.getModLog();
        configWrapper = new ConfigWrapper(config);
        random = new Random();
        networkHandler = new NetworkHandler(ModID);
        multiBlockRegistry = new MultiBlockRegistry();

        ElecCore.oldBlocks = true;

        //DEBUG///////////////////
        logger.info(new RecipeItemStack(Items.IRON_INGOT).setStackSize(3).equals(new RecipeItemStack("ingotIron").setStackSize(2)));

        logger.info(CalculationHelper.calcRequestedEF(23, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(17, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(16, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(24, 20, 40, 1000, 0.15f));
        logger.info(Math.sqrt(Math.abs(Math.cos(10))));
        /////////////////////////


        //setting up mod stuff
        configWrapper.registerConfig(new Config());
        configWrapper.refresh();
        Compat.instance.loadList();
        Compat.instance.addHandler(new RFCompat());
        Compat.instance.addHandler(new WailaCompatHandler());
        logger.info("RF API loaded: "+Compat.RF);
        logger.info("RFTools: "+Compat.RFTools);


        MCModInfo.createMCModInfo(event, "Created by Elec332",
                "E-Flux",
                "website link", "logo",
                new String[]{"Elec332"});
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException{
        ServerHelper.instance.registerExtendedPlayerProperties("EFluxChunks", ChunkLoaderPlayerProperties.class);
        ItemRegister.instance.init(event);
        BlockRegister.instance.init(event);
        MultiPartRegister.init();
        FluidRegister.instance.init();
        proxy.initRenderStuff();
        new WorldGenOres(new File(baseFolder, "Ores.cfg")).register();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        MultiBlockRegister.init();
        configWrapper.refresh();
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        registerRecipes();
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

        Compat.instance.init();
        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ForgeChunkManager.LoadingCallback() {
            @Override
            public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
                //Dummy, just load my chunks please.....
            }
        });
        RecipeRegister.registerRecipes();
        ServerHelper.instance.registerExtendedProperties("EFluxEnderNetwork", new Callable<INBTSerializable<NBTTagCompound>>() {
            @Override
            public INBTSerializable<NBTTagCompound> call() throws Exception {
                return EnderNetworkManager.instance;
            }
        });
        //register items/blocks

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        //Nope
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegister.instance.init(event);
    }

    public static void systemPrintDebug(Object s){
        if (Config.DebugLog)
            System.out.println(s);
    }

    private void registerRecipes(){
        RecipeRegistry.instance.registerRecipe(EnumRecipeMachine.COMPRESSOR, "ingotIron", new ItemStack(Items.DYE, 3, 5));
        RecipeRegistry.instance.registerRecipe(EnumRecipeMachine.COMPRESSOR, Lists.newArrayList(new RecipeItemStack("gemDiamond"), new RecipeItemStack(Items.BEEF)), new ItemStack(Items.EXPERIENCE_BOTTLE, 6));
    }

    /*
     * I normally never do this, but its ugly above.
     */
    public EFlux(){
        FurnaceRecipes.SMELTING_BASE = new EFluxFurnaceRecipes();
    }

}
