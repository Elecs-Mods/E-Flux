package elec332.eflux.items;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.map.BakedModelMetaMap;
import elec332.core.client.model.map.IBakedModelMetaMap;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.multipart.cable.PartAdvancedCable;
import elec332.eflux.multipart.cable.PartBasicCable;
import elec332.eflux.multipart.cable.PartNormalCable;
import mcmultipart.multipart.IMultipart;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 12-2-2016.
 */
public class ItemCable extends ItemEFluxMultiPart implements INoJsonItem {

    public ItemCable() {
        super("cable");
    }

    @SideOnly(Side.CLIENT)
    private IBakedModelMetaMap<IItemModel> models;
    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite[] textures;

    @Override
    public IMultipart createPart(World world, BlockPos pos, EnumFacing side, Vec3 hit, ItemStack stack, EntityPlayer player) {
        switch (stack.getMetadata()){
            case 0:
                return new PartBasicCable();
            case 1:
                return new PartNormalCable();
            case 2:
                return new PartAdvancedCable();
            default:
                return null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IItemModel getItemModel(Item item, int meta) {
        return models.forMeta(meta);
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        models = new BakedModelMetaMap<IItemModel>();
        for (int i = 0; i < textures.length; i++) {
            models.setModelForMeta(i, modelBakery.itemModelForTextures(textures[i]));
        }
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[3];
        textures[0] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/basicCable"));
        textures[1] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/normalCable"));
        textures[2] = iconRegistrar.registerSprite(new EFluxResourceLocation("blocks/advancedCable"));
    }

}
