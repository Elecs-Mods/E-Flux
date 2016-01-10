package elec332.eflux.client.blocktextures;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 24-7-2015.
 */
public class BlockTextures {

    public static final String defaultSideTexture = "default_side";
    public static final String defaultTopBottomTexture = "default_top&bottom";
    public static final String defaultBackTexture = "default_back";

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

}
