package elec332.eflux.tileentity;

/**
 * Created by Elec332 on 3-4-2015.
 */
public class TEGrinder extends BaseMachineTEWithInventory{

    public TEGrinder() {
        super(6000, 40, 18);
    }

    @Override
    protected String standardInventoryName() {
        return "Grinder";
    }
}
