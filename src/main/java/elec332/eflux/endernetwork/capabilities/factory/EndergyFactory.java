package elec332.eflux.endernetwork.capabilities.factory;

import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.endernetwork.EnderCapabilityHelper;
import elec332.eflux.endernetwork.capabilities.EFluxCapabilityEndergy;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 19-5-2016.
 */
public class EndergyFactory extends DefaultFactory {

    public EndergyFactory() {
        super(new EFluxResourceLocation("endergy"), EnderCapabilityHelper.getConstructor(EFluxCapabilityEndergy.class));
    }

    private static final int types = 3;

    private static final ResourceLocation[] textureList;
    private static final int[] typeArray;

    @Override
    public int[] getTypes() {
        return typeArray;
    }

    @SideOnly(Side.CLIENT)
    private IBakedModel[] models;

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        int meta = stack.getMetadata();
        if (models.length <= meta){
            return models[0];
        }
        return models[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        models = new IBakedModel[textures.length];
        for (int i = 0; i < textures.length; i++) {
            models[i] = modelBakery.itemModelForTextures(textures[i]);
        }
    }

    static {
        typeArray = new int[types];
        for (int i = 0; i < types; i++) {
            typeArray[i] = i;
        }
        textureList = new ResourceLocation[types];
        for (int i = 0; i < types; i++) {
            textureList[i] = new EFluxResourceLocation("items/endergyUpgrade."+i);
        }
    }

}
