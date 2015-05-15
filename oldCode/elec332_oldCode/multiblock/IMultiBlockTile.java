package elec332_oldCode.multiblock;

/**
 * Created by Elec332 on 14-5-2015.
 */
public interface IMultiBlockTile extends IMultiBlockPart{

    public void setController(MultiBlockControllerBase controller);

    public void onMultiBlockInvalidated();

    public MultiBlockControllerBase getController();

}
