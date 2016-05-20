package elec332.eflux.items;

import elec332.core.client.IIconRegistrar;
import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.INoJsonItem;
import elec332.core.client.model.model.IItemModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.world.StructureTemplate;
import elec332.core.world.WorldHelper;
import elec332.core.world.schematic.Schematic;
import elec332.core.world.schematic.SchematicHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IWeakEnderConnection;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.endernetwork.util.DefaultEnderConnectableItem;
import elec332.eflux.items.ender.AbstractEnderCapabilityItem;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import elec332.eflux.util.Config;
import elec332.eflux.api.ender.internal.IEndergyCapability;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemAreaMover extends AbstractEnderCapabilityItem<IEndergyCapability> implements INoJsonItem {

    public ItemAreaMover() {
        super("itemAreaMover");
        setHasSubtypes(true);
    }

    private static final short CREATIVE_META = Short.MAX_VALUE - 3;

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    private IItemModel model;

    @Override
    @SuppressWarnings("all")
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        boolean creative = isCreative(stack);
        IWeakEnderConnection<IEndergyCapability> connection = creative ? null : getCurrentConnection(stack);
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityAreaMover){
            if (!world.isRemote && !stack.hasTagCompound()){
                int range = ((TileEntityAreaMover) tile).getRange();
                int drain = (int) Math.pow(range, 3) / 4;
                if (creative || (connection.get() != null && connection.get().drainEndergy(drain))) {
                    stack.setTagCompound(((TileEntityAreaMover) tile).removeArea());
                    stack.getTagCompound().setBoolean("creative", creative);
                    stack.setItemDamage(range);
                }
            }
            return !world.isRemote ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        } else if (stack.hasTagCompound()){
            if (!world.isRemote) {
                BlockPos newPos = pos.offset(side);
                int range = stack.getItemDamage();
                int drain = (int) Math.pow(range, 3) / 4;
                if (creative || (connection.get() != null && connection.get().drainEndergy(drain))) {
                    newPos = newPos.add(-range, 0, -range);
                    for (int i = 0; i < 2 * range + 1; i++) {
                        for (int j = 0; j < 2 * range + 1; j++) {
                            for (int k = 0; k < 2 * range + 1; k++) {
                                BlockPos m = newPos.add(i, j, k);
                                if (!world.isAirBlock(m)) {
                                    player.addChatComponentMessage(new TextComponentString("There is a block at: " + m + " that has to be removed before the structure can be placed."));
                                    return EnumActionResult.FAIL;
                                }
                            }
                        }
                    }
                    Schematic schematic = SchematicHelper.INSTANCE.loadModSchematic(stack.getTagCompound());
                    StructureTemplate template = new StructureTemplate(schematic);
                    stack.setTagCompound(null);
                    template.generateStructure(newPos, world, world.getChunkProvider());
                    stack.setItemDamage(creative ? CREATIVE_META : 0);
                }
            }
            return !world.isRemote ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        }
        return EnumActionResult.FAIL;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        if (Config.General.creativeAreaMover){
            subItems.add(new ItemStack(itemIn, 1, CREATIVE_META));
        }
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + (isCreative(stack) ? ".creative" : "");
    }

    private boolean isCreative(ItemStack stack){
        return stack != null && stack.getItem() != null && (stack.getMetadata() == CREATIVE_META || (stack.hasTagCompound() && stack.getTagCompound().hasKey("creative") && stack.getTagCompound().getBoolean("creative")));
    }

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
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/areaMover"));
    }

    @Override
    protected ActionResult<ItemStack> execute(@Nonnull IEnderNetworkComponent<IEndergyCapability> component, @Nonnull IWeakEnderConnection<IEndergyCapability> connection, @Nonnull ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    @Override
    protected IEnderNetworkComponent<IEndergyCapability> createNewComponent(ItemStack stack) {
        return new DefaultEnderConnectableItem<>(EFluxAPI.ENDERGY_ENDER_CAPABILITY);
    }

}
