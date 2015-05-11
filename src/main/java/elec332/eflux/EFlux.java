package elec332.eflux;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import elec332.core.config.ConfigWrapper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModInfo;
import elec332.core.network.NetworkHandler;
import elec332.eflux.compat.Compat;
import elec332.eflux.grid.power.EventHandler;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.init.CommandRegister;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.items.circuits.CircuitHandler;
import elec332.eflux.proxies.CommonProxy;
import elec332.eflux.util.CalculationHelper;
import elec332.eflux.util.Config;
import elec332.eflux.world.WorldGenOres;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Random;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = EFlux.ModID, name = EFlux.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = ModInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class EFlux {

    public static final String ModName = "E-Flux";
    public static final String ModID = "EFlux";
    public static File baseFolder;

    @SidedProxy(clientSide = "elec332.eflux.proxies.ClientProxy", serverSide = "elec332.eflux.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static EFlux instance;
    public static Configuration config;
    public static CreativeTabs CreativeTab;
    public static Logger logger;
    public static ConfigWrapper configWrapper;
    public static Random random;
    public static NetworkHandler networkHandler;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        baseFolder = new File(event.getModConfigurationDirectory(), "E-Flux");
        config = new Configuration(new File(baseFolder, "EFlux.cfg"));
        CreativeTab = new CreativeTabs("EFlux") {
            @Override
            public Item getTabIconItem() {
                return Item.getItemFromBlock(Blocks.anvil);  //TODO: replace with mod item, once we got a nice one
            }
        };
        logger = event.getModLog();
        configWrapper = new ConfigWrapper(config);
        random = new Random();
        networkHandler = new NetworkHandler(ModID);

        //DEBUG///////////////////
        logger.info(CalculationHelper.calcRequestedEF(23, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(17, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(16, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(24, 20, 40, 1000, 0.15f));
        /////////////////////////


        //setting up mod stuff
        configWrapper.registerConfig(new Config());
        configWrapper.refresh();
        Compat.instance.loadList();
        logger.info("RF API loaded: "+Compat.RF);
        logger.info("RFTools: "+Compat.RFTools);


        MCModInfo.CreateMCModInfo(event, "Created by Elec332",
                "E-Flux",
                "website link", "logo",
                new String[]{"Elec332"});
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ItemRegister.instance.init(event);
        BlockRegister.instance.init(event);
        new WorldGenOres(new File(baseFolder, "Ores.cfg")).register();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        configWrapper.refresh();
        EventHandler eventHandler = new EventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        FMLCommonHandler.instance().bus().register(eventHandler);
        CircuitHandler.register();
        //GameRegistry.registerTileEntity(TEGrinder.class, EnumMachines.GRINDER.toString());
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
}
