package elec332_oldCode.multiblock;

/**
 * Created by Elec332 on 14-5-2015.
 */
public class BaseMultiBlockTile extends BaseMultiBlockPart implements IMultiBlockTile {

    private MultiBlockControllerBase controller;

    @Override
    public void setController(MultiBlockControllerBase controller) {
        this.controller = controller;
    }

    @Override
    public void onMultiBlockInvalidated() {
        this.controller = null;
    }

    @Override
    public MultiBlockControllerBase getController() {
        return controller;
    }
}
