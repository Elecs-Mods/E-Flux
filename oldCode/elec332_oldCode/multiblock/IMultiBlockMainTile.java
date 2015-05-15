package elec332_oldCode.multiblock;

/**
 * Created by Elec332 on 14-5-2015.
 */
public interface IMultiBlockMainTile extends IMultiBlockTile{

    public MultiBlockControllerBase newController();

    public boolean isInvalid();

}
