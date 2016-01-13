package elec332.eflux.blocks;

import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.map.IBakedModelRotationMap;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockItemInlet extends BlockMachinePart {

    public BlockItemInlet(String name) {
        super(0, name);
    }

    @SideOnly(Side.CLIENT)
    IBakedModelRotationMap<IBlockModel> rotationMap;

    @Override
    protected void registerTiles() {
    }

    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    @Override
    public int getTypes() {
        return 1;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.blockItemInlet." + stack.getItemDamage());
    }

    @Override
    public IBakedModel getBlockModel(Item item, int meta) {
        return rotationMap.forRotation(ModelRotation.X0_Y0);
    }

    @Override
    public IBlockModel getBlockModel(IBlockState state, IBlockAccess iba, BlockPos pos) {
        EnumFacing facing = ((TileEntityBlockMachine) WorldHelper.getTileAt(iba, pos)).getTileFacing();
        if (facing != EnumFacing.UP && facing != EnumFacing.DOWN){
            return rotationMap.forRotation(DirectionHelper.getRotationFromFacing(facing));
        } else if (facing == EnumFacing.UP){
            return rotationMap.forRotation(ModelRotation.X270_Y0);
        }
        return rotationMap.forRotation(ModelRotation.X90_Y0);
    }

    @Override
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        rotationMap = modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(normal, normal, itemOutlet, normal, normal, normal), true, true);
    }
}
