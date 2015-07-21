package elec332.eflux.util;

import elec332.core.config.Configurable;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class Config {

    @Configurable(category = "debug", comment = "Prints a LOT of debug information to the game log")
    public static boolean DebugLog = false;

    public static class Machines {
        private static final String categoryName = "Machines";

        @Configurable(category = categoryName, comment = "Sets the y range in which to search for plants")
        public static int growthLampY = 5;

        @Configurable(category = categoryName, comment = "Sets the x & z range in which to search for plants")
        public static int growthLampXZ = 5;

    }

    @Configurable(category = "energy", comment = "This defines the conversion for the RF power system")
    public static int RFConversionNumber = 10;
}
