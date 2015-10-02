package elec332.eflux.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.eflux.EFlux;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
public class BlockOres extends BlockWithMeta {

    public BlockOres() {
        super(Material.rock, "ore", EFlux.ModID);
        setResistance(5.0f);
        setHardness(2.5f);
    }

    private IIcon[] icons = new IIcon[4];

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        icons[0] = register.registerIcon("oreCopper.png");
        icons[1] = register.registerIcon("oreTin.png");
        icons[2] = register.registerIcon("oreZinc.png");
        icons[3] = register.registerIcon("oreSilver.png");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icons[meta];
    }


    @Override
    public void getSubBlocks(List<ItemStack> list, Item item, CreativeTabs creativeTab) {
        for (int i = 0; i < 4; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        switch (stack.getItemDamage()){
            case 0:
                return getUnlocalizedName() + ".copper";
            case 1:
                return getUnlocalizedName() + ".tin";
            case 2:
                return getUnlocalizedName() + ".zinc";
            case 3:
                return getUnlocalizedName() + ".silver";
        }
        return "ERROR_BLOCK_EFLUX";
    }
}
