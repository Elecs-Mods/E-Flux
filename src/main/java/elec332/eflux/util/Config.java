package elec332.eflux.util;

import elec332.core.config.Configurable;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class Config {

    @Configurable(category = "debug", comment = "Prints a LOT of debug information to the game log")
    public static boolean DebugLog = false;

}
