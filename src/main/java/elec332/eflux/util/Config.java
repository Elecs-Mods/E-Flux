package elec332.eflux.util;

import elec332.core.config.Configurable;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class Config {

    @Configurable(category = "debug", comment = "Prints a LOT of debug information to the game log")
    public static boolean DebugLog = false;

    @Configurable.Class
    public static class Machines {

        @Configurable(comment = "Sets the y range in which to search for plants")
        public static int growthLampY = 5;

        @Configurable(comment = "Sets the x & z range in which to search for plants")
        public static int growthLampXZ = 5;

    }

    @Configurable.Class
    public static class MultiBlocks {

        @Configurable.Class
        public static class Compressor {

            @Configurable
            public static float acceptance = 0.34f;

            @Configurable
            public static int optimalRP = 15;

        }

        @Configurable.Class
        public static class Furnace {

        }

        @Configurable.Class
        public static class Grinder {

        }

        @Configurable.Class
        public static class Laser {

        }

    }

    @Configurable(category = "energy", comment = "This defines the conversion for the RF power system")
    public static int RFConversionNumber = 10;


}
