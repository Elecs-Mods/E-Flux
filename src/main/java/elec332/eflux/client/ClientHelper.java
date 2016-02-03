package elec332.eflux.client;

import net.minecraft.util.StatCollector;

/**
 * Created by Elec332 on 1-2-2016.
 */
public class ClientHelper {

    public static String translateToLocal(String s){
        return StatCollector.translateToLocal(s).replace("\\n", "\n");
    }

}
