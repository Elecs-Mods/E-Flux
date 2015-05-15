package elec332_oldCode.multiblock.test;

import elec332_oldCode.multiblock.BaseMultiBlockMasterTile;
import elec332_oldCode.multiblock.MultiBlockControllerBase;

/**
 * Created by Elec332 on 15-5-2015.
 */
public class TestTileMaster extends BaseMultiBlockMasterTile {
    @Override
    public MultiBlockControllerBase newController() {
        return new MBC(this);
    }
}
