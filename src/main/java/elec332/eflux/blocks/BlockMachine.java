package elec332.eflux.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.baseclasses.tileentity.BlockTileBase;
import elec332.core.util.DirectionHelper;
import elec332.eflux.EFlux;
import elec332.eflux.util.EnumMachines;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

/**
 * Created by Elec332 on 30-4-2015.
 */
public class BlockMachine extends BlockTileBase {

    public BlockMachine(EnumMachines machine){
        super(machine.getBlockMaterial(), machine.getTileClass(), machine.toString(), EFlux.ModID);
        setCreativeTab(EFlux.creativeTab);
        this.machine = machine;
    }

    private EnumMachines machine;

    public EnumMachines getMachine(){
        return this.machine;
    }

    @Override
    public ItemStack ItemDropped() {
        return new ItemStack(this);
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0][DirectionHelper.ROTATION_MATRIX_YAW[2][side]];
    }

    @Override
    public int getRenderType() {
        return this.machine.getRenderID();
    }

    @Override
    public String getTopIconName(boolean active) {
        return "EFlux:" + this.machine.getTextureProvider().getTopIconName(active);
    }

    @Override
    public String getSideTexture(boolean active, BlockSide side) {
        return "EFlux:" + this.machine.getTextureProvider().getSideTexture(active, side);
    }

    @Override
    public String getFrontTexture(boolean active) {
        return "EFlux:" + this.machine.getTextureProvider().getFrontTexture(active);
    }

    @Override
    public String getBottomIconName(boolean active) {
        return "EFlux:" + this.machine.getTextureProvider().getBottomIconName(active);
    }
}
