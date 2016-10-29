package elec332.eflux.endernetwork.capabilities.factory;

import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

/**
 * Created by Elec332 on 21-5-2016.
 */
public class DefaultMetaItemFactory extends DefaultFactory {

    public DefaultMetaItemFactory(ResourceLocation name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory, int types) {
        super(name, factory);
        typeArray = new int[types];
        for (int i = 0; i < types; i++) {
            typeArray[i] = i;
        }
        texture = new ResourceLocation[types];
        for (int i = 0; i < types; i++) {
            texture[i] = new ResourceLocation(name.getResourceDomain(), "items/endercap/"+name.getResourcePath()+"."+i);
        }
    }

    private final int[] typeArray;

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
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        models = new IBakedModel[textures.length];
        for (int i = 0; i < textures.length; i++) {
            models[i] = modelBakery.itemModelForTextures(textures[i]);
        }
    }

}
