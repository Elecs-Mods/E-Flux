package elec332.eflux.compat;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;

/**
 * Created by Elec332 on 28-4-2015.
 */
public class LoadedMods {

    protected static void check(){
        RF_API = isAPILoaded("CoFHAPI|energy");
        RFTools = Loader.isModLoaded("RFTools");
    }

    public static boolean RF_API;
    public static boolean RFTools;


///////////////////////////////////////////////////////////////////////////
    private static boolean isAPILoaded(String name){
        for (ModContainer API : ModAPIManager.INSTANCE.getAPIList()){
           if (API.getModId().equals(name))
               return true;
        }
        return false;
    }
}
