package elec332.eflux.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import elec332.core.baseclasses.tileentity.TileBase;
import elec332.core.util.DirectionHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.blocktextures.MachinePartTextureHandler;
import elec332.eflux.tileentity.multiblock.TileMultiBlockTile;
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
public class BlockMachinePart extends BlockWithMeta implements ITileEntityProvider{

    public BlockMachinePart(int types) {
        super(Material.rock, "BlockMachinePart", EFlux.ModID);
        this.types = types;
        this.textureHandler = new MachinePartTextureHandler(types);
        setResistance(5.0f);
        setHardness(2.5f);
    }

    private final int types;
    private final MachinePartTextureHandler textureHandler;

    @Override
    public BlockMachinePart register() {
        super.register();
        GameRegistry.registerTileEntity(TileEntityBlockMachine.class, blockName);
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityBlockMachine();
    }

    public static final class TileEntityBlockMachine extends TileMultiBlockTile{

        public TileEntityBlockMachine(){
            super();
        }

        private ForgeDirection facing;

        @Override
        public void onBlockPlacedBy(EntityLivingBase entityLiving, ItemStack stack) {
            this.facing = DirectionHelper.getFacingOnPlacement(entityLiving);
        }

        @Override
        public ForgeDirection getTileFacing() {
            return facing;
        }

        @Override
        public void writeToNBT(NBTTagCompound tagCompound) {
            super.writeToNBT(tagCompound);
            tagCompound.setString("facing", facing.toString());
        }

        @Override
        public void readFromNBT(NBTTagCompound tagCompound) {
            super.readFromNBT(tagCompound);
            this.facing = ForgeDirection.valueOf(tagCompound.getString("facing"));
        }
    }
}
