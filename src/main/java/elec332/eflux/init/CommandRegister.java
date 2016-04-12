package elec332.eflux.init;

import com.google.common.collect.Lists;
import elec332.eflux.EFlux;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import java.util.List;

/**
 * Created by Elec332 on 24-2-2015.
 */
public final class CommandRegister {
    public static final CommandRegister instance = new CommandRegister();
    private CommandRegister(){
    }

    public void init(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandBase() {

            private static final String reloadConfig = "reloadConfig";

            @Override
            public String getCommandName() {
                return "EFlux";
            }

            @Override
            public String getCommandUsage(ICommandSender sender) {
                return "UsageString";
            }

            @Override
            public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
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
            }

        });
    }
}
