package elec332.eflux.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class DirectionBlock extends Block {
    public DirectionBlock(Material materialIn) {
        super(Material.rock);
    }

    //public IIcon[] icons = new IIcon[6];
/*
    public DirectionBlock(){
        super(Material.rock, EFlux.ModID, "DirectionBlock");
        this.setBlockTextureName(EFlux.ModID + ":test");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[side];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 6; i ++) {
            this.icons[i] = reg.registerIcon(this.textureName + "_" + i);
        }
    }*/
}
