package elec332.eflux.init;

import com.google.common.collect.Lists;
import elec332.core.api.registry.ISingleRegister;
import elec332.core.main.ElecCore;
import elec332.core.util.AbstractCommandBase;
import elec332.core.util.PlayerHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.circuit.CircuitHelper;
import elec332.eflux.client.EFluxResourceLocation;
import elec332.eflux.items.circuits.IEFluxCircuit;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

/**
 * Created by Elec332 on 24-2-2015.
 */
@SuppressWarnings("all")
public final class CommandRegister {
    public static final CommandRegister instance = new CommandRegister();
    private CommandRegister(){
    }

    public void init(ISingleRegister<ICommand> commandRegistry){
        commandRegistry.register(new AbstractCommandBase() {

            private static final String reloadConfig = "reloadConfig";

            @Override
            public String getMCCommandName() {
                return "EFlux";
            }

            @Override
            public String getMCCommandUsage(ICommandSender sender) {
                return "UsageString";
            }

            @Override
            public List<String> getMCTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
                return Lists.newArrayList(reloadConfig);
            }

            @Override
            public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
                if (args.length == 0){
                    return;
                }
                String arg = args[0];
                if (arg.equals(reloadConfig)){
                    System.out.println("reloading configs");
                    EFlux.configWrapper.refresh();
                }
                if (ElecCore.developmentEnvironment) {
                    if (arg.equals("oil")) {
                        TileEntity tile = WorldHelper.getTileAt(((EntityPlayer) sender).getEntityWorld(), PlayerHelper.getPosPlayerIsLookingAt((EntityPlayer) sender, 5D).getBlockPos());
                        if (tile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)){
                            IFluidHandler fluidHandler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
                            if (fluidHandler != null){
                                fluidHandler.fill(new FluidStack(FluidRegister.oil, 1000), true);
                            }
                        }
                    } else if (arg.equals("circuit")){
                        ItemStack stack = ItemRegister.normalUnrefinedBoard.copy();
                        ((IEFluxCircuit)CircuitHelper.getCircuit(stack)).etch(EFlux.circuitRegistry.getObject(new EFluxResourceLocation("shock")));
                        ((EntityPlayer) sender).inventory.addItemStackToInventory(stack);
                    }
                }
            }

        });
    }
}
