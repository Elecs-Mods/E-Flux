package elec332.eflux.blocks.machines;

import elec332.eflux.blocks.BaseBlockMachine;
import elec332.eflux.items.Wrench;
import elec332.eflux.util.EnumMachines;
import elec332.eflux.util.IComparatorOverride;
import elec332.eflux.util.IEFluxTile;
import elec332.eflux.util.IRedstoneHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 6-4-2015.
 */
public class BlockMachine extends BaseBlockMachine {

    public BlockMachine(String name, EnumMachines machine) {
        super(Material.rock, name);
        setResistance(4.5F);
        setHardness(2.0F);
        setStepSound(soundTypeStone);
        this.machine = machine;
    }

    private EnumMachines machine;

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof Wrench)
            return false;
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            return ((IEFluxTile) tile).onBlockActivated(player, side, hitX, hitY, hitZ);
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return this.machine.getTileEntity();
    }

    @Override
    public ItemStack ItemDropped() {
        return new ItemStack(this);
    }

    @Override
    public int getRenderType() {
        return this.machine.getRenderID();
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            ((IEFluxTile) tile).onBlockPlacedBy(entityLiving, stack);
        super.onBlockPlacedBy(world, x, y, z, entityLiving, stack);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            ((IEFluxTile) tile).onNeighborBlockChange(block);
        super.onNeighborBlockChange(world, x, y, z, block);
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            ((IEFluxTile) tile).onBlockAdded();
        super.onBlockAdded(world, x, y, z);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            ((IEFluxTile) tile).onBlockRemoved();
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            return ((IEFluxTile) tile).getLightValue();
        return super.getLightValue(world, x, y, z);
    }

    @Override
    public int getLightOpacity(IBlockAccess world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IEFluxTile)
            return ((IEFluxTile) tile).getLightOpacity();
        return super.getLightOpacity(world, x, y, z);
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IRedstoneHandler)
            return ((IRedstoneHandler) tile).isProvidingWeakPower(side);
        return super.isProvidingWeakPower(world, x, y, z, side);
    }

    @Override
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int direction) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IRedstoneHandler)
            return ((IRedstoneHandler) tile).canConnectRedstone(direction);
        return super.canConnectRedstone(world, x, y, z, direction);
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return this.machine.hasComparatorInputOverride();
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof IComparatorOverride)
            return ((IComparatorOverride) tile).getComparatorInputOverride(side);
        return super.getComparatorInputOverride(world, x, y, z, side);
    }
}
