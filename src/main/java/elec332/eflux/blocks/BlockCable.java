package elec332.eflux.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import elec332.core.client.render.RenderHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.render.RenderHandler;
import elec332.eflux.tileentity.energy.cable.AdvancedCable;
import elec332.eflux.tileentity.energy.cable.BasicCable;
import elec332.eflux.tileentity.energy.cable.NormalCable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 20-9-2015.
 */
public class BlockCable extends BlockWithMeta implements ITileEntityProvider{

    public BlockCable(String blockName) {
        super(Material.cloth, blockName, EFlux.ModID);
        setCreativeTab(EFlux.creativeTab);
    }

    @Override
    public BlockWithMeta register() {
        super.register();
        GameRegistry.registerTileEntity(BasicCable.class, "EFluxBasicCable");
        GameRegistry.registerTileEntity(NormalCable.class, "EFluxNormalCable");
        GameRegistry.registerTileEntity(AdvancedCable.class, "EFluxAdvancedCable");
        return this;
    }

    IIcon[] icon = new IIcon[3];

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icon[0] = iconRegister.registerIcon("EFlux:testCable");
        icon[1] = iconRegister.registerIcon("EFlux:testCable");
        icon[2] = iconRegister.registerIcon("EFlux:testCable");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return icon[meta];
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return icon[world.getBlockMetadata(x, y, z)];
    }

    @Override
    public int getRenderType() {
        return RenderHelper.getRenderID(this);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        switch (metadata){
            case 0:
                return new BasicCable();
            case 1:
                return new NormalCable();
            case 2:
                return new AdvancedCable();
            default:
                return null;
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess p_149719_1_, int p_149719_2_, int p_149719_3_, int p_149719_4_) {
        float thickness = 6 * RenderHelper.renderUnit;
        float heightStuff = (1 - thickness)/2;
        float f1 = thickness + heightStuff;
        setBlockBounds(heightStuff, heightStuff, heightStuff, f1, f1, f1);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 0;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName()+"."+stack.stackSize;
    }

    @Override
    public void getSubBlocks(List<ItemStack> list, Item item, CreativeTabs creativeTab) {
        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }
}
