package elec332.eflux.compat.rf;

import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.blocks.data.AbstractEFluxBlockMachineData;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.compat.Compat;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.init.ItemRegister;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Elec332 on 20-7-2015.
 */
public class RFCompat extends Compat.ICompatHandler {

    @Override
    public Compat.ModType getType() {
        return Compat.ModType.API;
    }

    @Override
    public String getName() {
        return "CoFHAPI|energy";
    }

    public static Block rfConverterBlock;

    @Override
    public void init() {
        EFlux.logger.info("Initialising RF compat...");
        rfConverterBlock = new BlockMachine(DATA).registerTile().register().setCreativeTab(EFlux.creativeTab);
        GameRegistry.addShapedRecipe(new ItemStack(rfConverterBlock), "CFC", "ZCZ", "FRF", 'C', ItemRegister.conductiveIngot, 'F', ItemRegister.conductiveCoil, 'Z', ItemRegister.zincIngot, 'R', BlockRegister.powerInlet.toItemStack());
    }

    private static final IEFluxBlockMachineData DATA;

    static {
        DATA = new AbstractEFluxBlockMachineData() {
            @Override
            public Class<? extends TileEntity> getTileClass() {
                return TileRFConverter.class;
            }

            @Override
            public IBlockTextureProvider getTextureProvider() {
                return BlockTextures.getRFConverterProvider();
            }

            @Override
            public String getName() {
                return "RFConverter";
            }
        };
    }

}
