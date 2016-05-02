package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.api.wrench.IWrenchable;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class Wrench extends Item implements IRightClickCancel, INoJsonItem {

    public Wrench(String name) {
        setCreativeTab(EFlux.creativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        //setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
        setNoRepair();
        setMaxDamage(72);
        setMaxStackSize(1);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        Block block = WorldHelper.getBlockAt(world, pos);
        if (block instanceof IWrenchable) {
            if (player.isSneaking()) {
                world.setBlockToAir(pos);
                if (!world.isRemote) {
                    world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), ((IWrenchable) block).ItemDropped(world, pos)));
                }
            } else if (!world.isRemote){
                ((IWrenchable) block).onWrenched(world, pos, side);
            }
            //player.swingItem();
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        } else if (block.rotateBlock(world, pos, side)) {
            //player.swingItem();
            stack.damageItem(1, player);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    private IItemModel model;

    @Override
    @SideOnly(Side.CLIENT)
    public IItemModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.itemModelForTextures(texture);
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/wrench"));
    }

}
