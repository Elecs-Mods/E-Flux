package elec332.eflux.blocks.machines;

import elec332.eflux.EFlux;
import elec332.eflux.blocks.BaseBlockMachine;
import elec332.eflux.tileentity.TEGrinder;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 3-4-2015.
 */
@Deprecated
public class Grinder extends BaseBlockMachine {
    public Grinder(Material material, String name) {
        super(material, name);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        player.openGui(EFlux.instance, EnumMachines.GRINDER.ordinal(), world, x, y, z);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TEGrinder();
    }

    @Override
    public ItemStack ItemDropped() {
        return new ItemStack(Item.getItemFromBlock(this));
    }
}
