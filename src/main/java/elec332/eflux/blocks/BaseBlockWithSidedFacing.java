package elec332.eflux.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.util.DirectionHelper;
import elec332.eflux.EFlux;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class BaseBlockWithSidedFacing extends BlockBase {

    @SideOnly(Side.CLIENT)
    public IIcon[] icons = new IIcon[6];

    public BaseBlockWithSidedFacing(Material material, String name){
        super(material, name);
        this.setBlockTextureName(EFlux.ModID + ":" + name);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icons[DirectionHelper.ROTATION_MATRIX_YAW[meta][side]];
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLivingBase, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, entityLivingBase, stack);
        //PlayerHelper.addPersonalMessageToClient("PlacementNumber: " + DirectionHelper.getDirectionNumberOnPlacement(entityLivingBase));
        world.setBlockMetadataWithNotify(x, y, z, DirectionHelper.getDirectionNumberOnPlacement(entityLivingBase), 2);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {
        for (int i = 0; i < 6; i ++) {
            this.icons[i] = reg.registerIcon(this.textureName + "_" + i);
        }
    }
}
