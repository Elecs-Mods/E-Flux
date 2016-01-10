package elec332.eflux.client.blocktextures;

import net.minecraft.util.EnumFacing;

/**
 * Created by Elec332 on 23-7-2015.
 */
public interface IBlockTextureProvider {

    public String getIconName(EnumFacing side, boolean active);

}
