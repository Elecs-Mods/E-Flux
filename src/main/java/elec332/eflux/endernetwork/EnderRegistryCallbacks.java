package elec332.eflux.endernetwork;

import com.google.common.collect.Lists;
import elec332.core.api.client.ITextureLoader;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.loading.IModelLoader;
import elec332.core.util.RegistryHelper;
import elec332.eflux.api.ender.IEnderCapabilityFactory;
import elec332.eflux.items.ender.capability.ItemEFluxEnderCapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Elec332 on 7-5-2016.
 */
@SuppressWarnings("unchecked")
public enum EnderRegistryCallbacks implements RegistryHelper.FullRegistryCallback<IEnderCapabilityFactory> {

    INSTANCE;

    private final List<Integer> registeredTypes = Lists.newArrayList();

    @Override
    public void onCreate(IForgeRegistryInternal<IEnderCapabilityFactory> owner, RegistryManager stage) {

    }

    @Override
    public void onClear(IForgeRegistryInternal<IEnderCapabilityFactory> owner, RegistryManager stage) {

    }

    @Override
    public void onAdd(IForgeRegistryInternal<IEnderCapabilityFactory> owner, RegistryManager stage, int id, IEnderCapabilityFactory obj) {
        if (!registeredTypes.contains(id)) {
            if (obj.createItem()) {
                RegistryHelper.register(new ItemEFluxEnderCapability(obj));
            }
            if (FMLCommonHandler.instance().getSide().isClient()) {
                if (obj instanceof IModelLoader) {
                    RenderingRegistry.instance().registerLoader((IModelLoader) obj);
                }
                if (obj instanceof ITextureLoader) {
                    RenderingRegistry.instance().registerLoader((ITextureLoader) obj);
                }
            }
            registeredTypes.add(id);
        }
    }

}
