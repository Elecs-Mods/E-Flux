package elec332.eflux.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;
import elec332.eflux.EFlux;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by Elec332 on 28-4-2015.
 */
public class Compat {

    public static final Compat instance = new Compat(EFlux.config);
    private Compat(Configuration config){
        config.getCategory("compat").setComment("Sets if compat for the mod will be enabled, FALSE = always disabled, AUTO = enabled if mod is loaded, TRUE = always enabled (Not recommended, will crash if said mod isn't loaded)");
        configuration = config;
    }
    private final Configuration configuration;

    public void loadList(){
        RF = compatEnabled("CoFHAPI|energy", ModType.API);
        RFTools = compatEnabled("RFTools");
    }

    public static boolean RF;
    public static boolean RFTools;


    ///////////////////////////////////////////////////////////////
    private boolean compatEnabled(String name){
        return compatEnabled(name, ModType.MOD);
    }

    private boolean compatEnabled(String name, ModType type){
        return compatEnabled(type, CompatEnabled.AUTO, name);
    }

    private boolean compatEnabled(ModType type, CompatEnabled enabled, String name){
        switch (isConfigEnabled(name, enabled)){
            case FALSE:
                return false;
            case AUTO:
                break;
            case TRUE:
                return true;
        }
        if (type == ModType.API)
            return isAPILoaded(name);
        else
            return Loader.isModLoaded(name);
    }

    private CompatEnabled isConfigEnabled(String name, CompatEnabled def){
        return CompatEnabled.valueOf(configuration.getString(name, "compat", def.toString(), "", new String[]{CompatEnabled.FALSE.toString(), CompatEnabled.AUTO.toString(), CompatEnabled.TRUE.toString()}));
    }

    private static boolean isAPILoaded(String name){
        for (ModContainer API : ModAPIManager.INSTANCE.getAPIList()){
            if (API.getModId().equals(name))
                return true;
        }
        return false;
    }

    private enum CompatEnabled{
        FALSE, AUTO, TRUE
    }

    private enum ModType{
        MOD, API
    }
}
