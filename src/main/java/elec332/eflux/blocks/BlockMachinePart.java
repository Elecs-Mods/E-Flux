package elec332.eflux.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.api.wrench.IWrenchable;
import elec332.core.baseclasses.tileentity.TileBase;
import elec332.core.client.render.SidedBlockRenderingCache;
import elec332.core.world.location.BlockData;
import elec332.core.util.BlockLoc;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.blocktextures.MachinePartTextureHandler;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.tileentity.multiblock.TileEntityDustStorage;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import elec332.eflux.tileentity.multiblock.TileMultiBlockTile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 28-8-2015.
 */
public class BlockMachinePart extends BlockWithMeta implements ITileEntityProvider, IWrenchable{

    public BlockMachinePart(int types){
        this(types, "BlockMachinePart", new SidedBlockRenderingCache(new MachinePartTextureHandler(), types, 2));
    }

    public BlockMachinePart(int types, String name, SidedBlockRenderingCache renderingCache) {
        super(Material.rock, name, EFlux.ModID);
        this.types = types;
        this.textureHandler = renderingCache;
        setResistance(5.0f);
        setHardness(2.5f);
    }

    private final int types;
    private final SidedBlockRenderingCache textureHandler;
    private static boolean register;

    @Override
    public BlockMachinePart register() {
        super.register();
        if (!register) {
            GameRegistry.registerTileEntity(TileEntityBlockMachine.class, blockName);
            GameRegistry.registerTileEntity(TileEntityMultiBlockItemGate.class, "itemGate");
            GameRegistry.registerTileEntity(TileEntityDustStorage.class, "dustStorage");
            register = true;
        }
        return this;
    }

    public int getTypes(){
        return this.types;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        textureHandler.registerTextures(register);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return textureHandler.getIconForInventoryRendering(meta, side);
    }

    @Override
    public IIcon getIcon(IBlockAccess iba, int x, int y, int z, int side) {
        return textureHandler.getIconForWorldRendering(iba, x, y, z, side);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < types; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.part."+stack.getItemDamage());
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileBase)
            return ((TileBase) tile).onBlockActivated(player, side, hitX, hitY, hitZ);
        return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack stack) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityBlockMachine) {
            ((TileEntityBlockMachine) tile).onBlockPlacedBy(entityLiving, stack);
        } else {
            super.onBlockPlacedBy(world, x, y, z, entityLiving, stack);
        }
    }

    @Override
    public ItemStack ItemDropped(World world, int x, int y, int z) {
        return new ItemStack(this, 1, world.getBlockMetadata(x, y, z));
    }

    @Override
    public void onWrenched(World world, int i, int i1, int i2, ForgeDirection forgeDirection) {
        TileEntity tile = world.getTileEntity(i, i1, i2);
        if (tile instanceof TileBase)
            ((TileBase) tile).onWrenched(forgeDirection);
    }

    @Override
    public void onPostBlockPlaced(World world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityBlockMachine)
            ((TileEntityBlockMachine) tile).pokeCheckStuff();
        super.onPostBlockPlaced(world, x, y, z, side);
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityBlockMachine)
            ((TileEntityBlockMachine) tile).pokeCheckStuff();
        super.onBlockPreDestroy(world, x, y, z, side);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityBlockMachine) {
            ((TileEntityBlockMachine) tile).pokeCheckStuff();
        } else if (tile instanceof TileBase) {
            ((TileBase) tile).onNeighborBlockChange(block);
        } else {
            super.onNeighborBlockChange(world, x, y, z, block);
        }
    }


    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        if (meta == BlockRegister.itemGate.meta)
            return new TileEntityMultiBlockItemGate();
        if (meta == BlockRegister.dustStorage.meta)
            return new TileEntityDustStorage();
        return new TileEntityBlockMachine();
    }

    public static class TileEntityBlockMachine extends TileMultiBlockTile {

        public TileEntityBlockMachine(){
            super();
        }

        private ForgeDirection facing;
        public int monitorSide;

        @Override
        public void onBlockPlacedBy(EntityLivingBase entityLiving, ItemStack stack) {
            this.facing = DirectionHelper.getFacingOnPlacement(entityLiving);
            onNeighborBlockChange(null);
        }

        @Override
        public void onWrenched(ForgeDirection forgeDirection) {
            if (getMultiBlock() == null) {
                if (forgeDirection != ForgeDirection.UP || forgeDirection != ForgeDirection.DOWN || (getBlockType() == BlockRegister.itemGate.block && getBlockMetadata() == BlockRegister.itemGate.meta))
                this.facing = forgeDirection;
                markDirty();
                syncData();
                reRenderBlock();
            }
        }

        public void pokeCheckStuff() {
            if (getBlockType() != BlockRegister.monitor.block || getBlockMetadata() != BlockRegister.monitor.meta)
                return;
            boolean neighbour = false;
            for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
                if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN)
                    continue;
                BlockLoc toCheck = myLocation().atSide(direction);
                Block block = WorldHelper.getBlockAt(worldObj, toCheck);
                int meta = WorldHelper.getBlockMeta(worldObj, toCheck);
                if (new BlockData(block, meta).equals(BlockRegister.monitor)) {
                    neighbour = true;
                    TileEntityBlockMachine tile = (TileEntityBlockMachine) WorldHelper.getTileAt(worldObj, toCheck);
                    if (monitorSide == 0 && tile.getTileFacing() == facing){
                        if (DirectionHelper.rotateLeft(facing) == direction){
                            monitorSide = 1;
                            tile.onNeighborBlockChange(getBlockType());
                            if (tile.monitorSide == monitorSide)
                                monitorSide = 0;
                            reRenderBlock();
                        } else if (DirectionHelper.rotateRight(facing) == direction){
                            monitorSide = 2;
                            tile.onNeighborBlockChange(getBlockType());
                            if (tile.monitorSide == monitorSide)
                                monitorSide = 0;
                            reRenderBlock();
                        }
                    }
                }
            }
            if (!neighbour){
                monitorSide = 0;
                reRenderBlock();
            }
        }

        @Override
        public ForgeDirection getTileFacing() {
            return facing;
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setString("facing", facing.toString());
            tagCompound.setInteger("monS", monitorSide);
        }

        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.facing = ForgeDirection.valueOf(tagCompound.getString("facing"));
            this.monitorSide = tagCompound.getInteger("monS");
        }
    }
}
