package elec332.eflux.compat;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;
import elec332.eflux.EFlux;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

/**
 * Created by Elec332 on 28-4-2015.
 */
public class Compat {

    public static final Compat instance = new Compat(EFlux.config);
    private Compat(Configuration config){
        config.getCategory("compat").setComment("Sets if compat for the mod will be enabled, FALSE = always disabled, AUTO = enabled if mod is loaded, TRUE = always enabled (Not recommended, will crash if said mod isn't loaded)");
        configuration = config;
        compatHandlers = Lists.newArrayList();
        locked = false;
    }

    private final Configuration configuration;
    private List<ICompatHandler> compatHandlers;
    private boolean locked;

    public void loadList(){
        RF = compatEnabled("CoFHAPI|energy", ModType.API);
        RFTools = compatEnabled("RFTools");
    }

    public void addHandler(ICompatHandler handler){
        if (locked)
            throw new RuntimeException("A mod attempted to register a CompatHandler too late!");
        compatHandlers.add(handler);
    }

    public void init(){
        locked = true;
        for (ICompatHandler handler : compatHandlers){
            if (compatEnabled(handler.getType(), handler.compatEnabled(), handler.getName())) {
                handler.init();
            } else {
                EFlux.logger.info(handler.getName()+" was not detected, skipping compat handler for it.");
            }
        }

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

    public enum ModType{
        MOD, API
    }

    public static abstract class ICompatHandler{

        public ModType getType(){
            return ModType.MOD;
        }

        public CompatEnabled compatEnabled(){
            return CompatEnabled.AUTO;
        }


        public abstract String getName();

        public abstract void init();

    }
}
