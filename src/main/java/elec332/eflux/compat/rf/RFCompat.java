package elec332.eflux.compat.rf;

import elec332.core.api.module.ElecModule;
import elec332.core.util.recipes.RecipeHelper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.blocks.data.AbstractEFluxBlockMachineData;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.init.ItemRegister;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * Created by Elec332 on 20-7-2015.
 */
@ElecModule(owner = EFlux.ModID, name = "RFCompat", modDependencies = "CoFHAPI|energy")
public class RFCompat {

    public static Block rfConverterBlock;

    @ElecModule.EventHandler
    public void init(FMLInitializationEvent event) {
        EFlux.logger.info("Initialising RF compat...");
        rfConverterBlock = new BlockMachine(DATA).registerTile().register().setCreativeTab(EFlux.creativeTab); //TODO
        RecipeHelper.getCraftingManager().addRecipe(new ItemStack(rfConverterBlock), "CFC", "ZCZ", "FRF", 'C', ItemRegister.conductiveIngot, 'F', ItemRegister.conductiveCoil, 'Z', ItemRegister.zincIngot, 'R', BlockRegister.powerInlet.toItemStack());
    }

    private static final IEFluxBlockMachineData DATA;

    static {
        DATA = new AbstractEFluxBlockMachineData() {

            @Override
            public Class<? extends TileEntity> getTileClass() {
                return TileRFConverter.class;
            }

            @Override
            public String getName() {
                return "RFConverter";
            }

        };
    }

}
