package elec332.eflux;

import com.google.common.collect.Lists;
import elec332.core.api.IElecCoreMod;
import elec332.core.api.config.IConfigWrapper;
import elec332.core.api.data.IExternalSaveHandler;
import elec332.core.api.module.IModuleController;
import elec332.core.api.network.ModNetworkHandler;
import elec332.core.api.registry.ISingleRegister;
import elec332.core.config.ConfigWrapper;
import elec332.core.main.ElecCoreRegistrar;
import elec332.core.multiblock.MultiBlockRegistry;
import elec332.core.network.IElecNetworkHandler;
import elec332.core.server.ServerHelper;
import elec332.core.util.LoadTimer;
import elec332.core.util.MCModInfo;
import elec332.core.util.RegistryHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.endernetwork.EnderRegistryCallbacks;
import elec332.eflux.grid.energy.EFluxGridHandler;
import elec332.eflux.grid.tank.EFluxTankHandler;
import elec332.eflux.handler.ChunkLoaderPlayerProperties;
import elec332.eflux.handler.EnderNetworkInfoProvider;
import elec332.eflux.handler.PlayerEventHandler;
import elec332.eflux.handler.WorldEventHandler;
import elec332.eflux.init.*;
import elec332.eflux.items.AbstractTexturedEFluxItem;
import elec332.eflux.items.ItemEFluxBluePrint;
import elec332.eflux.items.circuits.ICircuitDataProvider;
import elec332.eflux.network.PacketPlayerConnection;
import elec332.eflux.network.PacketSyncEnderContainerGui;
import elec332.eflux.network.PacketSyncEnderNetwork;
import elec332.eflux.proxies.CommonProxy;
import elec332.eflux.recipes.EFluxFurnaceRecipes;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.recipes.old.RecipeRegistry;
import elec332.eflux.util.CalculationHelper;
import elec332.eflux.util.Config;
import elec332.eflux.util.RecipeItemStack;
import elec332.eflux.world.WorldGenRegister;
import net.minecraft.command.ICommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = EFlux.ModID, name = EFlux.ModName, dependencies = "required-after:Forge@[#FORGE_VER#,);required-after:ElecCore@[#ELECCORE_VER#,);required-after:mcmultipart@[1.1.0,)",
        acceptedMinecraftVersions = "[1.10,)", useMetadata = true, canBeDeactivated = true)
public class EFlux implements IModuleController, IElecCoreMod {

    public static final String ModName = "E-Flux";
    public static final String ModID = "EFlux";
    public static File baseFolder;

    @SidedProxy(clientSide = "elec332.eflux.proxies.ClientProxy", serverSide = "elec332.eflux.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static EFlux instance;
    @ModNetworkHandler
    public static IElecNetworkHandler networkHandler;
    public static Configuration config;
    public static CreativeTabs creativeTab;
    public static Logger logger;
    public static IConfigWrapper configWrapper, configOres;
    public static Random random;
    public static MultiBlockRegistry multiBlockRegistry;
    public static FMLControlledNamespacedRegistry<IEnderCapabilityFactory> enderCapabilityRegistry;
    public static FMLControlledNamespacedRegistry<ICircuitDataProvider> circuitRegistry;

    private LoadTimer loadTimer;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        loadTimer = new LoadTimer(logger, ModName);
        loadTimer.startPhase(event);
        creativeTab = new CreativeTabs("EFlux") {

            Item item = new AbstractTexturedEFluxItem("circuit"){};

            @Override
            @Nonnull
            @SuppressWarnings("all")
            public Item getTabIconItem() {
                return item;
            }

        };
        enderCapabilityRegistry = RegistryHelper.createRegistry(new EFluxResourceLocation("enderCapabilities"), IEnderCapabilityFactory.class, EnderRegistryCallbacks.INSTANCE);
        circuitRegistry = RegistryHelper.createRegistry(new EFluxResourceLocation("circuits"), ICircuitDataProvider.class, RegistryHelper.getNullCallback());
        EFluxAPI.dummyLoad();
        baseFolder = new File(event.getModConfigurationDirectory(), "E-Flux");
        config = new Configuration(new File(baseFolder, "EFlux.cfg"));
        configWrapper = new ConfigWrapper(config);
        configOres = new ConfigWrapper(new Configuration(new File(baseFolder, "Ores.cfg")));
        random = new Random();
        networkHandler.registerClientPacket(new PacketSyncEnderNetwork());
        networkHandler.registerClientPacket(new PacketSyncEnderContainerGui());
        networkHandler.registerClientPacket(new PacketPlayerConnection());
        multiBlockRegistry = new MultiBlockRegistry();
        ElecCoreRegistrar.GRIDHANDLERS.register(new EFluxGridHandler());

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

        loadTimer.endPhase(event);
        MCModInfo.createMCModInfo(event,
                "Created by Elec332",
                "E-Flux",
                "website link",
                "logo",
                new String[]{"Elec332"}
        );
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        loadTimer.startPhase(event);
        ServerHelper.instance.registerExtendedPlayerProperties("EFluxChunks", ChunkLoaderPlayerProperties.class);
        CapabilityRegister.init();
        ItemRegister.init();
        BlockRegister.init();
        MultiPartRegister.init();
        FluidRegister.init();
        proxy.initRenderStuff();
        WorldGenRegister.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        MultiBlockRegister.init();
        configOres.refresh();
        configWrapper.refresh();
        registerRecipes();
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
        MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
        ElecCoreRegistrar.GRIDHANDLERS.register(new EFluxTankHandler());
        ElecCoreRegistrar.INFORMATION_PROVIDERS.register(new EnderNetworkInfoProvider());

        ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ForgeChunkManager.LoadingCallback() {

            @Override
            public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
                //Dummy, just load my chunks please.....
            }

        });
        RecipeRegister.registerRecipes();
        IForgeRegistry<VillagerRegistry.VillagerProfession> villagerRegistry = VillagerRegistry.instance().getRegistry();
        new VillagerRegistry.VillagerCareer(villagerRegistry.getValue(new ResourceLocation("smith")), "technician").addTrade(1, new EntityVillager.ITradeList() {

            @Override
            public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
                ICircuitDataProvider randomCircuit = getRandomBlueprint(random);
                int i = 14 + random.nextInt(9) * (randomCircuit.getCircuitType().getCircuitLevel() + 1);
                recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), ItemEFluxBluePrint.createBlueprint(randomCircuit)));
            }

            private ICircuitDataProvider getRandomBlueprint(Random random){
                List<ICircuitDataProvider> circuits = circuitRegistry.getValues();
                return circuits.get(random.nextInt(circuits.size()));
            }

        });
        //register items/blocks
        loadTimer.endPhase(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadTimer.startPhase(event);
        //Nope
        loadTimer.endPhase(event);
    }

    @Override
    public void registerServerCommands(ISingleRegister<ICommand> commandRegistry) {
        CommandRegister.instance.init(commandRegistry);
    }

    @Override
    public void registerSaveHandlers(ISingleRegister<IExternalSaveHandler> saveHandlerRegistry) {
        EnderNetworkManager.registerSaveHandler(saveHandlerRegistry);
    }

    @Override
    public boolean isModuleEnabled(String s) {
        return true;
    }

    public static void systemPrintDebug(Object s){
        if (Config.debugLog) {
            System.out.println(s);
        }
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
