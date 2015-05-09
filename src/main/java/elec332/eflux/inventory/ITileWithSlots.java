package elec332.eflux.inventory;

/**
 * Created by Elec332 on 5-5-2015.
 */
public interface ITileWithSlots {

    public void addSlots(BaseContainer container);

    public int getProgress();

    public void setProgress(int i);

    public float getProgressScaled();

    public boolean isWorking();
    
}
