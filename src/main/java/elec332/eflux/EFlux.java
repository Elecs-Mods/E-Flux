package elec332.eflux;

import com.google.common.base.Preconditions;
import elec332.core.api.IElecCoreMod;
import elec332.core.api.config.IConfigWrapper;
import elec332.core.api.data.IExternalSaveHandler;
import elec332.core.api.info.IInfoDataAccessorBlock;
import elec332.core.api.info.IInfoProvider;
import elec332.core.api.info.IInformation;
import elec332.core.api.module.IModuleController;
import elec332.core.api.network.ModNetworkHandler;
import elec332.core.api.registry.ISingleRegister;
import elec332.core.api.util.IDependencyHandler;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.config.ConfigWrapper;
import elec332.core.inventory.window.WindowManager;
import elec332.core.java.ReflectionHelper;
import elec332.core.main.ElecCoreRegistrar;
import elec332.core.multiblock.MultiBlockRegistry;
import elec332.core.network.IElecNetworkHandler;
import elec332.core.server.ServerHelper;
import elec332.core.util.*;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.api.energy.EnergyType;
import elec332.eflux.api.util.IBreakableMachine;
import elec332.eflux.circuit.ICircuitDataProvider;
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
import elec332.eflux.network.PacketPlayerConnection;
import elec332.eflux.network.PacketSyncEnderNetwork;
import elec332.eflux.proxies.CommonProxy;
import elec332.eflux.recipes.EFluxFurnaceRecipes;
import elec332.eflux.util.CalculationHelper;
import elec332.eflux.util.Config;
import elec332.eflux.world.WorldGenRegister;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.command.ICommand;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = EFlux.ModID, name = EFlux.ModName, dependencies = "required-after:eleccore;",
        acceptedMinecraftVersions = "[1.12,)", useMetadata = true, canBeDeactivated = true)
public class EFlux implements IModuleController, IElecCoreMod, IDependencyHandler {

    public static final String ModName = "E-Flux";
    public static final String ModID = "eflux";
    private static final String FORGE_VERSION = "#FORGE_VER#";
    private static final String ELECCORE_VERSION = "#ELECCORE_VER#";
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
    public static ForgeRegistry<IEnderCapabilityFactory> enderCapabilityRegistry;
    public static ForgeRegistry<ICircuitDataProvider> circuitRegistry;
    public static EFluxGridHandler gridHandler;

    private LoadTimer loadTimer;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = LogManager.getLogger("EFlux");
        loadTimer = new LoadTimer(logger, ModName);
        loadTimer.startPhase(event);
        creativeTab = AbstractCreativeTab.create("EFlux", new Supplier<ItemStack>() {

            @Override
            public ItemStack get() {
                return new ItemStack(RenderingRegistry.instance().registerFakeItem(new AbstractTexturedEFluxItem("circuit"){}));
            }

        });
        enderCapabilityRegistry = RegistryHelper.createRegistry(new EFluxResourceLocation("enderCapabilities"), IEnderCapabilityFactory.class, EnderRegistryCallbacks.INSTANCE);
        circuitRegistry = RegistryHelper.createRegistry(new EFluxResourceLocation("circuits"), ICircuitDataProvider.class, RegistryHelper.getNullCallback());
        EFluxAPI.dummyLoad();
        baseFolder = new File(event.getModConfigurationDirectory(), "E-Flux");
        config = new Configuration(new File(baseFolder, "EFlux.cfg"));
        configWrapper = new ConfigWrapper(config);
        configOres = new ConfigWrapper(new Configuration(new File(baseFolder, "WorldGen.cfg")));
        random = new Random();
        networkHandler.registerClientPacket(new PacketSyncEnderNetwork());
        networkHandler.registerClientPacket(new PacketPlayerConnection());
        multiBlockRegistry = new MultiBlockRegistry();
        //ElecCoreRegistrar.GRIDHANDLERS.register(gridHandler = new EFluxGridHandler());

        //DEBUG///////////////////
        logger.info(CalculationHelper.calcRequestedEF(23, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(17, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(16, 20, 40, 1000, 0.15f));
        logger.info(CalculationHelper.calcRequestedEF(24, 20, 40, 1000, 0.15f));
        logger.info(Math.sqrt(Math.abs(Math.cos(10))));
        /////////////////////////


        //setting up mod stuff
        configWrapper.registerConfig(new Config());
        configWrapper.refresh();

        CapabilityRegister.init();
        BlockRegister.init();
        ItemRegister.init();
        MultiPartRegister.init();
        FluidRegister.init();
        proxy.initRenderStuff();

        loadTimer.endPhase(event);
        MCModInfo.createMCModInfo(event,
                "Created by Elec332",
                "E-Flux",
                "website link",
                "logo",
                new String[]{"Elec332"}
        );
        MinecraftForge.EVENT_BUS.register(new Object(){

            @SideOnly(Side.CLIENT)
            @SubscribeEvent(priority = EventPriority.LOWEST)
            @SuppressWarnings("all")
            public void afterAllModelsBaked(ModelBakeEvent event){
                ModelLoader modelLoader = event.getModelLoader();
                try {
                    //Set<ModelResourceLocation> set = (Set<ModelResourceLocation>) ReflectionHelper.makeFinalFieldModifiable(ModelLoader.class.getDeclaredField("missingVariants")).get(modelLoader);
                    Map<ResourceLocation, Exception> exceptionMap = (Map<ResourceLocation, Exception>) ReflectionHelper.makeFinalFieldModifiable(ModelLoader.class.getDeclaredField("loadingExceptions")).get(modelLoader);
                    File f = new File((File) FMLInjectionData.data()[6], "missing_json.txt");
                    if (!f.exists()){
                        f.createNewFile();
                    }
                    PrintStream ps = new PrintStream(new FileOutputStream(f));
                    Map<ResourceLocation, Exception> exceptionMap_ = exceptionMap.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByKey(new Comparator<ResourceLocation>() {

                                @Override
                                public int compare(ResourceLocation o1, ResourceLocation o2) {
                                    int i = o1.getResourcePath().compareTo(o2.getResourcePath());
                                    if (i == 0){
                                        String f = "", f1 = "";
                                        if (o1 instanceof ModelResourceLocation){
                                            f = ((ModelResourceLocation) o1).getVariant();
                                        }
                                        if (o2 instanceof ModelResourceLocation){
                                            f1 = ((ModelResourceLocation) o2).getVariant();
                                        }
                                        return f.compareTo(f1);
                                    }
                                    return i;
                                }

                            }))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (e1, e2) -> e1,
                                    LinkedHashMap::new
                            ));
                    for (Map.Entry<ResourceLocation, Exception> e : exceptionMap_.entrySet()){
                        if (!(e.getKey().getResourceDomain().equals("eflux") || e.getKey().getResourcePath().contains("eflux"))){
                            continue;
                        }
                        ps.println("----------");
                        ps.println("ResourceLocation: "+e.getKey());
                        Throwable ex = e.getValue();
                        int i = 0;
                        while ((ex = ex.getCause()) != null){
                            if (ex instanceof FileNotFoundException){
                                ps.println("FileNotFound: "+ex.getMessage());
                                i++;
                            }
                            if (i >= 8){
                                //break;
                            }
                        }
                        ps.println("----------");
                    }

                    ps.close();
                } catch (Exception e1){
                    e1.printStackTrace();
                }
            }

        });
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        loadTimer.startPhase(event);
        ServerHelper.instance.registerExtendedPlayerProperties("EFluxChunks", ChunkLoaderPlayerProperties.class);
        //CapabilityRegister.init();
        //BlockRegister.init();
        //ItemRegister.init();
        //MultiPartRegister.init();
        //FluidRegister.init();
        //proxy.initRenderStuff();
        WorldGenRegister.init();
        WindowManager.INSTANCE.register(proxy);
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
        IForgeRegistry<VillagerRegistry.VillagerProfession> villagerRegistry = RegistryHelper.getVillagerRegistry();
        VillagerRegistry.VillagerProfession smith = Preconditions.checkNotNull(villagerRegistry.getValue(new ResourceLocation("smith")));
        new VillagerRegistry.VillagerCareer(smith, "technician").addTrade(1, IElecTradeList.wrap(new IElecTradeList() {

            @Override
            public void modifyMerchantRecipeList(IMerchant merchant, @Nonnull MerchantRecipeList tradeList, @Nonnull Random random) {
                ICircuitDataProvider randomCircuit = getRandomBlueprint(random);
                int i = 14 + random.nextInt(9) * (randomCircuit.getCircuitType().getCircuitLevel() + 1);
                tradeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, i), ItemEFluxBluePrint.createBlueprint(randomCircuit)));
            }

            private ICircuitDataProvider getRandomBlueprint(Random random){
                List<ICircuitDataProvider> circuits = circuitRegistry.getValues();
                return circuits.get(random.nextInt(circuits.size()));
            }

        }));
        ElecCoreRegistrar.INFORMATION_PROVIDERS.register(new IInfoProvider() {

            @Override
            public void addInformation(@Nonnull IInformation information, @Nonnull IInfoDataAccessorBlock hitData) {
                if (hitData.getData().getBoolean("_M-broken")) {
                    information.addInformation(SpecialChars.ALIGNCENTER + SpecialChars.ITALIC + "Broken");
                }
            }

            @Nonnull
            @Override
            public NBTTagCompound getInfoNBTData(@Nonnull NBTTagCompound tag, TileEntity tile, @Nonnull EntityPlayerMP player, @Nonnull IInfoDataAccessorBlock hitData) {
                if (tile instanceof IBreakableMachine){
                    tag.setBoolean("_M-broken", ((IBreakableMachine) tile).isBroken());
                }
                return tag;
            }

        });
        EnderNetworkManager.dummy();

        //register items/blocks
        loadTimer.endPhase(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadTimer.startPhase(event);
        if (EnergyType.values().length != 2){
            throw new RuntimeException("Someone is messing with the laws of physics.");
        }
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
    public boolean useLangCompat() {
        return false;
    }

    @Override
    public boolean isModuleEnabled(String s) {
        return true;
    }

    @Override
    public String getRequiredForgeVersion(String mcVersion) {
        return mcVersion.equals("1.11") ? FORGE_VERSION : null;
    }

    @Override
    public String getRequiredElecCoreVersion(String mcVersion) {
        return ELECCORE_VERSION;
    }

    public static void systemPrintDebug(Object s){
        if (Config.debugLog) {
            System.out.println(s);
        }
    }

    private void registerRecipes(){
        //RecipeRegistry.instance.registerRecipe(EnumRecipeMachine.COMPRESSOR, "ingotIron", new ItemStack(Items.DYE, 3, 5));
        //RecipeRegistry.instance.registerRecipe(EnumRecipeMachine.COMPRESSOR, Lists.newArrayList(new RecipeItemStack("gemDiamond"), new RecipeItemStack(Items.BEEF)), new ItemStack(Items.EXPERIENCE_BOTTLE, 6));
    }

    /*
     * I normally never do this, but its ugly above.
     */
    public EFlux(){
        FurnaceRecipes.SMELTING_BASE = new EFluxFurnaceRecipes();
    }

}
