package elec332.eflux.tileentity.clable;

/**
 * Created by Elec332 on 15-4-2015.
 */
public enum EnumCableType {
    BASIC(50);

    /////////DATA////////
    private EnumCableType(int maxHandle){
        this.maxHandle = maxHandle;
    }

    private int maxHandle;

    public int getMaxHandle() {
        return maxHandle;
    }
}
