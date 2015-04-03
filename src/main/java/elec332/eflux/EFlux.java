package elec332.eflux;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.init.CommandRegister;
import elec332.eflux.init.ItemRegister;
import elec332.core.helper.FileHelper;
import elec332.core.helper.MCModInfo;
import elec332.core.modBaseUtils.ModBase;
import elec332.core.modBaseUtils.ModInfo;
import elec332.eflux.proxies.CommonProxy;

import java.io.File;

/**
 * Created by Elec332 on 24-2-2015.
 */
@Mod(modid = EFlux.ModID, name = EFlux.ModName, dependencies = ModInfo.DEPENDENCIES+"@[#ELECCORE_VER#,)",
        acceptedMinecraftVersions = ModInfo.ACCEPTEDMCVERSIONS, useMetadata = true, canBeDeactivated = true)
public class EFlux extends ModBase {

    public static final String ModName = "E-Flux"; //Human readable name
    public static final String ModID = "EFlux";  //modid (usually lowercase)

    @SidedProxy(clientSide = "elec332.eflux.proxies.ClientProxy", serverSide = "elec332.eflux.proxies.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(ModID)
    public static EFlux instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.cfg = FileHelper.getConfigFileElec(event);
        loadConfiguration();
        ItemRegister.instance.preInit(event);
        BlockRegister.instance.preInit(event);
        //setting up mod stuff

        loadConfiguration();
        MCModInfo.CreateMCModInfo(event, "Created by Elec332",
                "mod description",
                "website link", "logo",
                new String[]{"Elec332"});
        notifyEvent(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        loadConfiguration();
        ItemRegister.instance.init(event);
        BlockRegister.instance.init(event);
        //register items/blocks

        notifyEvent(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        loadConfiguration();
        //Mod compat stuff

        notifyEvent(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandRegister.instance.init(event);
    }

    File cfg;

    @Override
    public File configFile() {
        return cfg;
    }

    @Override
    public String modID(){
        return ModID;
    }
}
