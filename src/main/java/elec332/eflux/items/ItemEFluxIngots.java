package elec332.eflux.items;

/**
 * Created by Elec332 on 17-1-2016.
 */
public class ItemEFluxIngots extends ItemEFluxGenerics {

    public ItemEFluxIngots(){
        components = new String[]{"copper", "tin", "zinc", "silver", "compressed", "conductive"};
    }

    @Override
    protected String getName() {
        return "ingots";
    }

}
