package elec332.eflux.endernetwork.capabilities.factory;

import elec332.core.api.client.IIconRegistrar;
import elec332.core.api.client.model.IElecModelBakery;
import elec332.core.api.client.model.IElecQuadBakery;
import elec332.core.api.client.model.IElecTemplateBakery;
import elec332.core.client.model.loading.IModelAndTextureLoader;
import elec332.eflux.api.ender.IEnderCapability;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.api.ender.internal.IEnderNetwork;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;

/**
 * Created by Elec332 on 19-5-2016.
 */
public class DefaultFactory extends IEnderCapabilityFactory implements IModelAndTextureLoader {

    public DefaultFactory(ResourceLocation name, Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory, ResourceLocation... textures){
        setRegistryName(name);
        this.factory = factory;
        this.texture = textures;
    }

    private final Function<Pair<Side, IEnderNetwork>, IEnderCapability> factory;
    protected ResourceLocation[] texture;
    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite[] textures;
    @SideOnly(Side.CLIENT)
    private IBakedModel model;

    /**
     * Creates a new IEnderCapability
     *
     * @param side The side for which this capability will be created
     * @param network The network this capability will be created for
     * @return The newly created EnderCapability
     */
    @Override
    public IEnderCapability createNewCapability(Side side, IEnderNetwork network) {
        return factory.apply(Pair.of(side, network));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iconRegistrar) {
        textures = new TextureAtlasSprite[texture.length];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = iconRegistrar.registerSprite(texture[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(IElecQuadBakery quadBakery, IElecModelBakery modelBakery, IElecTemplateBakery templateBakery) {
        model = modelBakery.itemModelForTextures(textures);
    }

}
