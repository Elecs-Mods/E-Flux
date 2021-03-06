package elec332.eflux.client;

import elec332.core.client.model.ElecModelBakery;
import elec332.core.util.StatCollector;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Optional;

/**
 * Created by Elec332 on 1-2-2016.
 */
public class ClientHelper {

    public static final ResourceLocation DEFAULT_GUI_LOCATION = new EFluxResourceLocation("gui/GuiNull.png");

    public static String translateToLocal(String s){
        return StatCollector.translateToLocal(s).replace("\\n", "\n");
    }

    @SuppressWarnings("all")
    public static final IModelState DEFAULT_ITEM_STATE = new IModelState() {

        @Override
        public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
            if (part.isPresent()) {
                Object oPart = part.get();
                if (oPart.getClass() == ItemCameraTransforms.TransformType.class) {
                    return Optional.of(new TRSRTransformation(ElecModelBakery.DEFAULT_ITEM.getTransform((ItemCameraTransforms.TransformType) oPart)));
                }
            }
            return Optional.empty();
        }

    };

}
