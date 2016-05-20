package elec332.eflux.api.ender;

import elec332.eflux.api.ender.internal.IEnderNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 7-5-2016.
 */
public abstract class IEnderCapabilityFactory extends IForgeRegistryEntry.Impl<IEnderCapabilityFactory> {

    private static final int[] DEFAULT_TYPES = new int[]{0};

    /**
     * Creates a new IEnderCapability
     *
     * @param side The side for which this capability will be created
     * @return The newly created EnderCapability
     */
    public abstract IEnderCapability createNewCapability(Side side, IEnderNetwork enderNetwork);

    /**
     * If true, an item for this capability will be created once it is registered.
     *
     * @return Whether to register an item for this capability
     */
    public boolean createItem(){
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel();
    }

    public int[] getTypes(){
        return DEFAULT_TYPES;
    }

}
