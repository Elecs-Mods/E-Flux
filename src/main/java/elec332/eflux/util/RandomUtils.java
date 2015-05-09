package elec332.eflux.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elec332 on 3-5-2015.
 */
public class RandomUtils {
    public static <E> List<E> copyOf(List<E> original){
        List<E> ret = new ArrayList<E>();
        for (E e : original)
            ret.add(e);
        return ret;
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] toArray(List<E> list){
        return (E[])list.toArray();
    }
}
