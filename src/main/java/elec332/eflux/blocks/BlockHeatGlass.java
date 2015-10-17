package elec332.eflux.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import elec332.core.client.render.SidedBlockRenderingCache;
import elec332.eflux.client.blocktextures.MachineGlassTextureHandler;
import elec332.eflux.init.BlockRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 2-9-2015.
 */
public class BlockHeatGlass extends BlockMachinePart{

    public BlockHeatGlass(int types) {
        super(types, "BlockMachineGlass", new SidedBlockRenderingCache(new MachineGlassTextureHandler(), types));
    }

    @Override
    protected void registerTiles() {
        GameRegistry.registerTileEntity(LaserIndicator.class, "LaserIndicatorClass");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == BlockRegister.laserLens.meta)
            return new LaserIndicator();
        return new TileEntityBlockMachine();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.glass." + stack.getItemDamage());
    }

    public static class LaserIndicator extends TileEntityBlockMachine {
    }

}
