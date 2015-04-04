package elec332.eflux.blocks.machines;

import elec332.eflux.blocks.BaseBlockMachine;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class Grinder extends BaseBlockMachine {
    public Grinder(Material material, String name) {
        super(material, name);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return null;
    }

    @Override
    public ItemStack ItemDropped() {
        return new ItemStack(Item.getItemFromBlock(this));
    }
}
