package elec332.eflux.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.eflux.EFlux;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class DirectionBlock extends BlockBase {
    @SideOnly(Side.CLIENT)
    public IIcon[] icons = new IIcon[6];

    public DirectionBlock(){
        super(Material.rock, "DirectionBlock");
        this.setBlockTextureName(EFlux.ModID + ":test");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side];
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 6; i ++) {
            this.icons[i] = reg.registerIcon(this.textureName + "_" + i);
        }
    }
}
