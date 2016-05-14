package elec332.eflux.items;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class EFluxCoils extends EFluxItemsIngot {

    public EFluxCoils(){
        components = new String[]{"co", "si", "cond"};
    }

    @Override
    protected String getName() {
        return "coils";
    }

}
