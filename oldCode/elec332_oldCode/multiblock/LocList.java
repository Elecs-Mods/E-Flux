package elec332_oldCode.multiblock;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Elec332 on 15-5-2015.
 */
public class LocList {
    public LocList(IMultiBlockPart multiBlockPart){
        this.locations = new HashSet<IMultiBlockPart>();
        this.locations.add(multiBlockPart);
    }

    public Set<IMultiBlockPart> locations;

    public LocList merge(LocList locList){
        this.locations.addAll(locList.locations);
        return this;
    }
}
