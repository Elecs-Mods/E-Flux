package elec332.eflux.util;

/**
 * Created by Elec332 on 1-5-2015.
 */
public class CalculationHelper {
    public static int calcRequestedEF(int rp, int optimumRP, int efForOptimumRP, int maxEF, float acc){
        if (rp <= optimumRP*(1+acc) && optimumRP*(1-acc) <= rp) {
            float i = (((-1 / (optimumRP * 0.75f)) * (float) Math.pow((rp - optimumRP), 2)) + optimumRP);//((rp - optimumRP)^2)) + optimumRP);
            float j = i / optimumRP;
            int ret = (int) (j * efForOptimumRP);
            //System.out.println(i);
            //System.out.println(j);
            //System.out.println(ret);
            return Math.min(ret, maxEF);
        } else return 0;
    }
}
