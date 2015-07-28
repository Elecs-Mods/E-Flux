package elec332.eflux.client.blocktextures;

import elec332.core.baseclasses.tileentity.BlockTileBase;

/**
 * Created by Elec332 on 23-7-2015.
 */
public interface IBlockTextureProvider {

    public String getTopIconName(boolean active);

    public String getSideTexture(boolean active, BlockTileBase.BlockSide side);

    public String getFrontTexture(boolean active);

    public String getBottomIconName(boolean active);



}
