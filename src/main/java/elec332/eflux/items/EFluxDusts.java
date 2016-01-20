package elec332.eflux.items;

/**
 * Created by Elec332 on 18-1-2016.
 */
public class EFluxDusts extends EFluxItems {

    public EFluxDusts(){
        components = new String[]{"dustIron", "dustGold", "dustCopper", "dustZinc", "dustSilver", "dustCoal", "dustStone", "dustTin", "dustConductive"};
    }

    @Override
    protected String getName() {
        return "dusts";
    }

}
