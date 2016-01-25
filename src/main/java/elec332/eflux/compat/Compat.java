package elec332.eflux.compat;

import elec332.core.util.AbstractCompatHandler;
import elec332.eflux.EFlux;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by Elec332 on 28-4-2015.
 */
public class Compat extends AbstractCompatHandler{

    public static final Compat instance = new Compat(EFlux.config);
    private Compat(Configuration config) {
        super(config, EFlux.logger);
    }

    public void loadList(){
        RF = compatEnabled("CoFHAPI|energy", ModType.API);
        RFTools = compatEnabled("RFTools");
        MCMP = compatEnabled(ModType.MOD, CompatEnabled.TRUE, "mcmultipart");
    }

    public static boolean RF, RFTools, MCMP;

}
