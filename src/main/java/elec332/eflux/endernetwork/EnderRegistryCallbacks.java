package elec332.eflux.endernetwork;

import elec332.core.client.ITextureLoader;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.model.IModelLoader;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.items.ender.capability.EFluxEnderCapabilityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.Map;

/**
 * Created by Elec332 on 7-5-2016.
 */
@SuppressWarnings("unchecked")
public enum EnderRegistryCallbacks implements IForgeRegistry.AddCallback<IEnderCapabilityFactory>, IForgeRegistry.ClearCallback<IEnderCapabilityFactory>, IForgeRegistry.CreateCallback<IEnderCapabilityFactory> {

    INSTANCE;

    @Override
    public void onCreate(Map<ResourceLocation, ?> slaveset) {
    }

    @Override
    public void onClear(Map<ResourceLocation, ?> slaveset) {
    }

    @Override
    public void onAdd(IEnderCapabilityFactory obj, int id, Map<ResourceLocation, ?> slaveset) {
        if (obj.createItem()){
            GameRegistry.register(new EFluxEnderCapabilityItem(obj));
        }
        if (FMLCommonHandler.instance().getSide().isClient()){
            if (obj instanceof IModelLoader) {
                RenderingRegistry.instance().registerLoader((IModelLoader) obj);
            }
            if (obj instanceof ITextureLoader){
                RenderingRegistry.instance().registerLoader((ITextureLoader) obj);
            }
        }
    }

}
