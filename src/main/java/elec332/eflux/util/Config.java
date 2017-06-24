package elec332.eflux.util;

import elec332.core.config.Configurable;

/**
 * Created by Elec332 on 15-4-2015.
 */
public class Config {

    @Configurable(category = "debug", comment = "Prints a LOT of debug information to the game log")
    public static boolean debugLog = false;

    @Configurable.Class
    public static class General {

        @Configurable(comment = "Set to true to enable the creative area mover.")
        public static boolean creativeAreaMover = false;

    }

    @Configurable.Class
    public static class Machines {

        @Configurable.Class
        public static class GrowthLamp {

            @Configurable(comment = "Sets the y range in which to search for plants")
            public static int growthLampY = 5;

            @Configurable(comment = "Sets the x & z range in which to search for plants")
            public static int growthLampXZ = 5;

        }

        @Configurable.Class
        public static class Heater {

            @Configurable(minValue = 1, maxValue = 1000)
            public static int generatedHeatPerTick = 20;

        }

    }

    @Configurable.Class
    public static class Misc {

        @Configurable(comment = "Sets the maximum range for the area mover")
        public static int areaMoverRangeMax = 32;

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

        @Configurable.Class
        public static class DistillationTower {

            @Configurable(minValue = 10, maxValue = 1000)
            public static int minHeat = 4000;

            @Configurable(minValue = 100, maxValue = 10000)
            public static int requiredHeat = 2000;

            @Configurable(minValue = 1, maxValue = 1000)
            public static int heatDispersion = 65;

        }

        @Configurable.Class
        public static class Desalter {

            @Configurable(minValue = 10, maxValue = 1000)
            public static int minHeat = 200;

            @Configurable(minValue = 10, maxValue = 1000)
            public static int requiredHeat = 200;

            @Configurable(minValue = 1, maxValue = 1000)
            public static int heatDispersion = 65;

        }

    }

    @Configurable(category = "energy", comment = "This defines the conversion for the RF power system")
    public static int RFConversionNumber = 10;


}
