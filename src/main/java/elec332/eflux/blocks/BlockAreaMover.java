package elec332.eflux.blocks;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonBlock;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class BlockAreaMover extends Block implements INoJsonBlock, ITileEntityProvider {

    public BlockAreaMover() {
        super(Material.ROCK);
        setUnlocalizedName(EFlux.ModID.toLowerCase()+".areaMover");
        setRegistryName(new EFluxResourceLocation("areaMover"));
    }

    @SideOnly(Side.CLIENT)
    private IBakedModel model;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;

    public BlockAreaMover register(){
        GameRegistry.register(this);
        GameRegistry.register(new BAMItemBlock(this).setRegistryName(getRegistryName()));
        return this;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (int i = 0; i < 3; i++) {
            list.add(new ItemStack(this, 1, i));
        }
    }

    /**
     * This method is used when a model is requested to render the block in a world.
     *
     * @param state The current BlockState.
     * @return The model to render for this block for the given arguments.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getBlockModel(IBlockState state) {
        return model;
    }

    /**
     * This method is used when a model is requested when its not placed, so for an item.
     *
     * @return The model to render when the block is not placed.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(textures));
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[6];
        for (int i = 0; i < 6; i++) {
            textures[i] = (i == 1) ? iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/moverTop")) : iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/moverSide"));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAreaMover();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(stack, player, tooltip, advanced);
        int range = TileEntityAreaMover.getRange(stack.getItemDamage());
        tooltip.add("Range: "+range);
    }

    public class BAMItemBlock extends ItemBlock {

        public BAMItemBlock(Block block) {
            super(block);
        }

        @Override
        public String getUnlocalizedName(ItemStack stack) {
            boolean creative = stack.getItemDamage() > 3;
            return super.getUnlocalizedName(stack) + "." + (creative ? "creative" : stack.getItemDamage());
        }

        @Override
        public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            EnumActionResult ret = super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            if (ret == EnumActionResult.SUCCESS){
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();
                if (!block.isReplaceable(worldIn, pos)) {
                    pos = pos.offset(facing);
                }
                TileEntity tile = WorldHelper.getTileAt(worldIn, pos);
                if (tile instanceof TileEntityAreaMover){
                    ((TileEntityAreaMover) tile).setRange(stack.getItemDamage());
                }
            }
            return ret;
        }

        @Override
        public int getMetadata(int damage) {
            return damage;
        }

    }

}
