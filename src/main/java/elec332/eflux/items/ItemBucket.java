package elec332.eflux.items;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.WorldHelper;
import elec332.eflux.blocks.BlockFluid;
import elec332.eflux.client.ClientHelper;
import elec332.eflux.client.WrappedModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import java.util.List;

import static net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple.FLUID_NBT_KEY;

/**
 * Created by Elec332 on 2-5-2016.
 */
public class ItemBucket extends net.minecraft.item.ItemBucket implements INoJsonItem {

    public ItemBucket(final BlockFluid containedBlock) {
        super(containedBlock);
        setRegistryName(containedBlock.getRegistryName());
        setUnlocalizedName(getRegistryName().toString().replace(":", ".").toLowerCase());
        setContainerItem(Items.BUCKET);
        MinecraftForge.EVENT_BUS.register(new Object(){

            @SubscribeEvent
            @SuppressWarnings("all")
            public void onBucketClicked(FillBucketEvent event){
                if (event.getEmptyBucket() == null || event.getEmptyBucket().getItem() == null || event.getTarget() == null){
                    return;
                }
                BlockPos pos = event.getTarget().getBlockPos();
                World world = event.getWorld();
                if (WorldHelper.getBlockAt(world, pos) != containedBlock){
                    return;
                }
                world.setBlockToAir(pos);
                event.setFilledBucket(new ItemStack(ItemBucket.this));
                event.setResult(Event.Result.ALLOW);
            }

        });
        this.fluid = containedBlock.fluid;
        this.fluidRL = fluid.getStill();
    }

    @SideOnly(Side.CLIENT)
    private IBakedModel model;
    private final Fluid fluid;
    private final ResourceLocation fluidRL;
    private static final ResourceLocation BUCKET_BACK, BUCKET_FRONT;

    @Override
    @Nonnull
    public ICapabilityProvider initCapabilities(@Nonnull ItemStack stack, NBTTagCompound nbt) {
        FluidHandlerItemStackSimple ret = new FluidHandlerItemStackSimple.SwapEmpty(stack, new ItemStack(Items.BUCKET), Fluid.BUCKET_VOLUME) {

            @Override
            public boolean canFillFluidType(FluidStack fluid) {
                return fluid != null && fluid.getFluid() == ItemBucket.this.fluid;
            }

        };
        ret.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true);
        return ret;
    }

    @Override
    public boolean updateItemStackNBT(NBTTagCompound nbt) {
        NBTTagCompound fluidTag = new NBTTagCompound();
        new FluidStack(fluid, Fluid.BUCKET_VOLUME).writeToNBT(fluidTag);
        nbt.setTag(FLUID_NBT_KEY, fluidTag);
        return true;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IBakedModel getItemModel(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        return model;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerTextures(IIconRegistrar iIconRegistrar) {
        iIconRegistrar.registerSprite(BUCKET_BACK);
        iIconRegistrar.registerSprite(BUCKET_FRONT);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels(ElecQuadBakery elecQuadBakery, ElecModelBakery elecModelBakery, ElecTemplateBakery elecTemplateBakery) {
        model = WrappedModel.wrapWithDefaultItemTransforms(new ModelDynBucket(BUCKET_BACK, fluidRL, BUCKET_FRONT, fluid, true).bake(ClientHelper.DEFAULT_ITEM_STATE, DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter()));
    }

    static {
        BUCKET_BACK = new ResourceLocation("forge", "items/bucket_base");
        BUCKET_FRONT = new ResourceLocation("forge", "items/bucket_cover");
    }

}
