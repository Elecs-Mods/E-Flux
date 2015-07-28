package elec332.eflux.client.blocktextures;

import elec332.core.baseclasses.tileentity.BlockTileBase;

/**
 * Created by Elec332 on 24-7-2015.
 */
public class BlockTextures {

    private static final String defaultSideTexture = "default_side";
    private static final String defaultTopBottomTexture = "default_top&bottom";
    private static final String defaultBackTexture = "default_back";

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
            public String getSideTexture(boolean active, BlockTileBase.BlockSide side) {
                if (side == BlockTileBase.BlockSide.BACK)
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

}
