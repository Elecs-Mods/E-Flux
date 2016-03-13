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
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.tileentity.misc.TileEntityAreaMover;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemAreaMover extends EFluxItem implements INoJsonItem {

    public ItemAreaMover() {
        super("itemAreaMover");
        setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    private TextureAtlasSprite texture;
    @SideOnly(Side.CLIENT)
    private IItemModel model;

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        if (tile instanceof TileEntityAreaMover){
            if (!world.isRemote && !stack.hasTagCompound()){
                int range = ((TileEntityAreaMover) tile).getRange();
                stack.setTagCompound(((TileEntityAreaMover) tile).removeArea());
                stack.setItemDamage(range);
            }
            return !world.isRemote;
        } else if (stack.hasTagCompound()){
            if (!world.isRemote) {
                BlockPos newPos = pos.offset(side);
                int range = stack.getItemDamage();
                newPos = newPos.add(-range, 0, -range);
                for (int i = 0; i < 2 * range + 1; i++) {
                    for (int j = 0; j < 2 * range + 1; j++) {
                        for (int k = 0; k < 2 * range + 1; k++) {
                            BlockPos m = newPos.add(i, j, k);
                            if (!world.isAirBlock(m)){
                                player.addChatComponentMessage(new ChatComponentText("There is a block at: "+m+" that has to be removed before the structure can be placed."));
                                return false;
                            }
                        }
                    }
                }
                Schematic schematic = SchematicHelper.INSTANCE.loadModSchematic(stack.getTagCompound());
                StructureTemplate template = new StructureTemplate(schematic);
                stack.setTagCompound(null);
                template.generateStructure(newPos, world, world.getChunkProvider());
                stack.setItemDamage(0);
            }
            return !world.isRemote;
        }
        return false;
    }

    @Override
    public IItemModel getItemModel(ItemStack stack, World world, EntityLivingBase entity) {
        return model;
    }

    /**
     * A helper method to prevent you from having to hook into the event,
     * use this to make your quads. (This always comes AFTER the textures are loaded)
     */
    @Override
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.itemModelForTextures(texture);
    }

    /**
     * Use this to register your textures.
     *
     * @param iconRegistrar The IIconRegistrar.
     */
    @Override
    public void registerTextures(IIconRegistrar iconRegistrar) {
        texture = iconRegistrar.registerSprite(new EFluxResourceLocation("items/areaMover"));
    }
}
