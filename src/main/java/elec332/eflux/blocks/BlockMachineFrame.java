package elec332.eflux.blocks;

import elec332.core.client.model.ElecModelBakery;
import elec332.core.client.model.ElecQuadBakery;
import elec332.core.client.model.RenderingRegistry;
import elec332.core.client.model.model.IBlockModel;
import elec332.core.client.model.template.ElecTemplateBakery;
import elec332.eflux.client.render.MachineFrameRenderer;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Elec332 on 13-1-2016.
 */
public class BlockMachineFrame extends BlockMachinePart {

    public BlockMachineFrame(String name) {
        super(3, name);
    }

    @SideOnly(Side.CLIENT)
    public static IBlockModel model;

    @Override
    public BlockMachinePart register() {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()){
            registerClient();
        }
        return super.register();
    }

    @Override
    protected void registerTiles() {
    }

    @Override
    public int getTypes() {
        return 3;
    }

    @Override
    public int getRenderType() {
        return RenderingRegistry.SPECIAL_BLOCK_RENDERER_ID;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return StatCollector.translateToLocal("eflux.machine.blockFrame." + stack.getItemDamage());
    }

    @Override
    public IBakedModel getBlockModel(Item item, int meta) {
        return model;
    }

    @Override
    public void registerModels(ElecQuadBakery quadBakery, ElecModelBakery modelBakery, ElecTemplateBakery templateBakery) {
        model = modelBakery.forTemplate(templateBakery.newDefaultBlockTemplate(normal));
    }

    private void registerClient(){
        RenderingRegistry.instance().registerRenderer(this, new MachineFrameRenderer(model));
    }

}
