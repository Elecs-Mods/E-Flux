package elec332.eflux.items;

import elec332.core.api.wrench.IRightClickCancel;
import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.util.IMultiMeterDataProvider;
import elec332.eflux.api.util.IMultiMeterDataProviderMultiLine;
import elec332.eflux.client.EFluxResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 5-4-2015.
 */
public class MultiMeter extends Item implements IRightClickCancel, INoJsonItem {
    public MultiMeter(String name) {
        setCreativeTab(EFlux.creativeTab);
        setUnlocalizedName(EFlux.ModID + "." + name);
        //setTextureName(EFlux.ModID + ":" + name);
        setContainerItem(this);
        setMaxStackSize(1);
        GameRegistry.registerItem(this, name);
    }

    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(this, 1, itemStack.getItemDamage() + 1);
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float HitX, float HitY, float HitZ) {
        TileEntity tileEntity = WorldHelper.getTileAt(world, pos);
        if (!world.isRemote) {
            if (tileEntity instanceof IMultiMeterDataProvider)
                player.addChatComponentMessage(new ChatComponentText(((IMultiMeterDataProvider) tileEntity).getProvidedData()));
            if (tileEntity instanceof IMultiMeterDataProviderMultiLine)
                for (String s : ((IMultiMeterDataProviderMultiLine) tileEntity).getProvidedData())
                    player.addChatComponentMessage(new ChatComponentText(s));
            //TODO: more provided info
            return true;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    private IItemModel model;

    @Override
    @SideOnly(Side.CLIENT)
    public IItemModel getItemModel(Item item, int meta) {
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
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/multimeter"));
    }

}
