package elec332.eflux.items;

import elec332.core.util.ItemStackHelper;
import elec332.core.util.PlayerHelper;
import elec332.core.world.StructureTemplate;
import elec332.core.world.WorldHelper;
import elec332.core.world.schematic.Schematic;
import elec332.core.world.schematic.SchematicHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEndergyCapability;
import elec332.eflux.api.ender.internal.IWeakEnderConnection;
import elec332.eflux.endernetwork.util.DefaultEnderConnectableItem;
import elec332.eflux.items.ender.AbstractEnderCapabilityItem;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import elec332.eflux.util.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemEFluxAreaMover extends AbstractEnderCapabilityItem<IEndergyCapability> {

    public ItemEFluxAreaMover() {
        super("itemAreaMover");
        setHasSubtypes(true);
    }

    private static final short CREATIVE_META = Short.MAX_VALUE - 3;

    @Override
    @SuppressWarnings("all")
    public EnumActionResult onItemUseFirst(EntityPlayer player, EnumHand hand, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
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
                                    PlayerHelper.sendMessageToPlayer(player, "There is a block at: " + m + " that has to be removed before the structure can be placed.");
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
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(EntityPlayer player, @Nonnull EnumHand hand, World world) {
        ItemStack stack = player.getHeldItem(hand);
        return isCreative(stack) ? new ActionResult<ItemStack>(EnumActionResult.PASS, stack) : super.onItemRightClick(player, hand, world);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void getSubItems(@Nonnull Item item, List<ItemStack> subItems, CreativeTabs creativeTab) {
        super.getSubItems(item, subItems, creativeTab);
        if (Config.General.creativeAreaMover){
            subItems.add(new ItemStack(item, 1, CREATIVE_META));
        }
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName(stack) + (isCreative(stack) ? ".creative" : "");
    }

    private boolean isCreative(ItemStack stack){
        return ItemStackHelper.isStackValid(stack) && (stack.getMetadata() == CREATIVE_META || (stack.hasTagCompound() && stack.getTagCompound().hasKey("creative") && stack.getTagCompound().getBoolean("creative")));
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
