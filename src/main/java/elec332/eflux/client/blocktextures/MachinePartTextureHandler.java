package elec332.eflux.client.blocktextures;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Elec332 on 1-9-2015.
 */
public final class MachinePartTextureHandler {

    public MachinePartTextureHandler(int i){
        this.types = i;
        this.icons = new IIcon[types][6];
    }

    private final int types;
    private IIcon[][] icons;

    public IIcon getIconForWorldRendering(IBlockAccess iba, int x, int y, int z, int side){
        return getIconForInventoryRendering(iba.getBlockMetadata(x, y, z), side);
    }

    public IIcon getIconForInventoryRendering(int meta, int side) {
        return icons[meta][side];
    }

    public void registerTextures(IIconRegister register){
        for (int i = 0; i < types; i++) {
            for (int j = 0; j < 6; j++) {
                icons[i][j] = register.registerIcon("eflux:machinepart/"+getTexturesForBlock(i, j));
            }
        }
    }

    private String getTexturesForBlock(int meta, int side){
        switch (meta){
            case 0:
                return "basic";
            case 1:
                return "normal";
            case 2:
                return "advanced";
            case 3:
                return "itemOutlet";
            case 4:
                return "laserLens";
            case 5:
                return "laserCore";
            default:
                return "null";
        }
    }

}
