package elec332.eflux.items;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemEFluxCoils extends ItemEFluxGenerics {

    public ItemEFluxCoils(){
        components = new String[]{"co", "si", "cond"};
    }

    @Override
    protected String getName() {
        return "coils";
    }

}
