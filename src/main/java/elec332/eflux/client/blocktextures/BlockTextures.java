package elec332.eflux.client.blocktextures;

import elec332.core.util.BlockSide;

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
            public String getTopIconName(boolean active) {
                return defaultTopBottomTexture;
            }

            @Override
            public String getSideTexture(boolean active, BlockSide side) {
                if (side == BlockSide.BACK)
                    return defaultBackTexture;
                return defaultSideTexture;
            }

            @Override
            public String getFrontTexture(boolean active) {
                return front;
            }

            @Override
            public String getBottomIconName(boolean active) {
                return defaultTopBottomTexture;
            }
        };
    }

    public static IBlockTextureProvider getCustomSidedProvider(final String side){
        return new IBlockTextureProvider() {
            @Override
            public String getTopIconName(boolean active) {
                return defaultTopBottomTexture;
            }

            @Override
            public String getSideTexture(boolean active, BlockSide side_) {
                return side;
            }

            @Override
            public String getFrontTexture(boolean active) {
                return side;
            }

            @Override
            public String getBottomIconName(boolean active) {
                return defaultTopBottomTexture;
            }
        };
    }

    public static IBlockTextureProvider getCustomProvider(final String side, final String top, final  String bottom){
        return new IBlockTextureProvider() {
            @Override
            public String getTopIconName(boolean active) {
                return top;
            }

            @Override
            public String getSideTexture(boolean active, BlockSide side_) {
                return side;
            }

            @Override
            public String getFrontTexture(boolean active) {
                return side;
            }

            @Override
            public String getBottomIconName(boolean active) {
                return bottom;
            }
        };
    }

    public static IBlockTextureProvider getCustomTFProvider(final String top, final String front){
        return new IBlockTextureProvider() {
            @Override
            public String getTopIconName(boolean active) {
                return top;
            }

            @Override
            public String getSideTexture(boolean active, BlockSide side) {
                if (side == BlockSide.BACK)
                    return defaultBackTexture;
                return defaultSideTexture;
            }

            @Override
            public String getFrontTexture(boolean active) {
                return front;
            }

            @Override
            public String getBottomIconName(boolean active) {
                return defaultTopBottomTexture;
            }
        };
    }

    public static IBlockTextureProvider getCustomTBProvider(final String top, final String bottom){
        return new IBlockTextureProvider() {
            @Override
            public String getTopIconName(boolean active) {
                return top;
            }

            @Override
            public String getSideTexture(boolean active, BlockSide side) {
                return defaultSideTexture;
            }

            @Override
            public String getFrontTexture(boolean active) {
                return defaultSideTexture;
            }

            @Override
            public String getBottomIconName(boolean active) {
                return bottom;
            }
        };
    }

}
