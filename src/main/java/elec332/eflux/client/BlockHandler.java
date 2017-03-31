package elec332.eflux.client;

import elec332.core.api.client.model.loading.IBlockModelHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

/**
 * Created by Elec332 on 18-3-2017.
 */
public class BlockHandler implements IBlockModelHandler {
    @Override
    public boolean handleBlock(Block block) {
        return false;
    }

    @Override
    public String getIdentifier(IBlockState iBlockState) {
        return null;
    }

    @Override
    public IBakedModel getModelFor(IBlockState iBlockState, String s, ModelResourceLocation modelResourceLocation) {
        return null;
    }
}
