package elec332.eflux.init;

import elec332.eflux.multipart.cable.PartAdvancedCable;
import elec332.eflux.multipart.cable.PartBasicCable;
import elec332.eflux.multipart.cable.PartNormalCable;
import mcmultipart.multipart.MultipartRegistry;

/**
 * Created by Elec332 on 10-2-2016.
 */
public final class MultiPartRegister {

    public static void init(){
        MultipartRegistry.registerPart(PartAdvancedCable.class, "PartEFluxAdvancedCable");
        MultipartRegistry.registerPart(PartNormalCable.class, "PartEFluxNormalCable");
        MultipartRegistry.registerPart(PartBasicCable.class, "PartEFluxBasicCable");

        ItemRegister.instance.registerMultiPartItems();
    }

}
