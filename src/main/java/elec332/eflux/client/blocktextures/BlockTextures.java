package elec332.eflux.client.blocktextures;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 24-7-2015.
 */
public class BlockTextures {

    public static final String defaultSideTexture = "default_side";
    public static final String defaultTopBottomTexture = defaultSideTexture;//"default_top&bottom";
    public static final String defaultBackTexture = defaultSideTexture;//"default_back";

    public static IBlockTextureProvider getDefaultProvider(){
        return getDefaultProvider("nope");
    }

    public static IBlockTextureProvider getDefaultProvider(final String front){
        return new IBlockTextureProvider() {

            @Override
            public String getIconName(EnumFacing side, boolean active) {
                switch (side){
                    case UP:
                    case DOWN:
                        return defaultTopBottomTexture;
                    case NORTH:
                        return front;
                    case SOUTH:
                        return defaultBackTexture;
                    case EAST:
                    case WEST:
                        return defaultSideTexture;
                    default:
                        return "null";
                }
            }

        };
    }

    public static IBlockTextureProvider getCustomSidedProvider(final String sideTexture){
        return new IBlockTextureProvider() {

            @Override
            public String getIconName(EnumFacing side, boolean active) {
                switch (side){
                    case UP:
                    case DOWN:
                        return defaultTopBottomTexture;
                    case NORTH:
                    case SOUTH:
                    case EAST:
                    case WEST:
                        return sideTexture;
                    default:
                        return "null";
                }
            }

        };
    }

    public static IBlockTextureProvider getCustomProvider(final String sideTexture, final String top, final  String bottom){
        return new IBlockTextureProvider() {

            @Override
            public String getIconName(EnumFacing side, boolean active) {
                switch (side){
                    case UP:
                        return top;
                    case DOWN:
                        return bottom;
                    case NORTH:
                    case SOUTH:
                    case EAST:
                    case WEST:
                        return sideTexture;
                    default:
                        return "null";
                }
            }

        };
    }

    public static IBlockTextureProvider getCustomTFProvider(final String top, final String front){
        return new IBlockTextureProvider() {

            @Override
            public String getIconName(EnumFacing side, boolean active) {
                switch (side){
                    case UP:
                        return top;
                    case DOWN:
                        return defaultTopBottomTexture;
                    case NORTH:
                        return front;
                    case SOUTH:
                        return defaultBackTexture;
                    case EAST:
                    case WEST:
                        return defaultSideTexture;
                    default:
                        return "null";
                }
            }

        };
    }

    public static IBlockTextureProvider getCustomTBProvider(final String top, final String bottom){
        return new IBlockTextureProvider() {

            @Override
            public String getIconName(EnumFacing side, boolean active) {
                switch (side){
                    case UP:
                        return top;
                    case DOWN:
                        return bottom;
                    case NORTH:
                    case SOUTH:
                    case EAST:
                    case WEST:
                        return defaultSideTexture;
                    default:
                        return "null";
                }
            }

        };
    }

    //Impl

    public static IBlockTextureProvider getCoalGenProvider(){
        return new IBlockTextureProvider() {

            @Override
            public String getIconName(EnumFacing side, boolean active) {
                switch (side){
                    case UP:
                    case DOWN:
                        return defaultTopBottomTexture;
                    case NORTH:
                        return active ? "coalGeneratorFrontA" : "coalGeneratorFront";
                    case SOUTH:
                        return defaultBackTexture;
                    case EAST:
                    case WEST:
                        return defaultSideTexture;
                    default:
                        return "null";
                }
            }

        };
    }

    public static IBlockTextureProvider getCapacitorProvider(){
        return getCustomProvider("cap_side", "cap_top", "def_cap");
    }

    public static IBlockTextureProvider getAssemblyTableProvider(){
        return getCustomTFProvider("at_top", "at_front");
    }

    public static IBlockTextureProvider getGrowthLampProvider(){
        return getCustomTBProvider(BlockTextures.defaultBackTexture, "gl_facing");
    }

    public static IBlockTextureProvider getChunkMainProvider(){
        return getDefaultProvider("chunkmain_front");
    }

    public static IBlockTextureProvider getChunkSubProvider(){
        return getDefaultProvider("cs_front");
    }

    public static IBlockTextureProvider getTeslaCoilProvider(){
        return getCustomSidedProvider("teslacoil_side");
    }

    public static IBlockTextureProvider getScannerProvider(){
        return getDefaultProvider("scannerFront");
    }

    public static IBlockTextureProvider getWasherProvider(){
        return getDefaultProvider("washer_front");
    }

    public static IBlockTextureProvider getRubbleSieveProvider(){
        return getDefaultProvider("rubbleSieve");
    }

    public static IBlockTextureProvider getHeatGlassProvider(){
        return getCustomProvider("heatGlass", "heatGlass", "heatGlass");
    }

    public static IBlockTextureProvider getLaserLensProvider(){
        return new IBlockTextureProvider() {
            @Override
            public String getIconName(EnumFacing side, boolean active) {
                if (side.getAxis() == EnumFacing.Axis.Z){
                    return "laserLensFront";
                }
                return "heatGlass";
            }
        };
    }

    public static IBlockTextureProvider getRFConverterProvider(){
        return new IBlockTextureProvider() {
            @Override
            public String getIconName(EnumFacing side, boolean active) {
                if (side == EnumFacing.NORTH){
                    return "powerinlet_front";
                } else if (side == EnumFacing.SOUTH){
                    return "precisionMotor";
                }
                return defaultSideTexture;
            }
        };
    }

}
