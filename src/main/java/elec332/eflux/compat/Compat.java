package elec332.eflux.compat;

import elec332.core.config.Configurable;
import elec332.eflux.EFlux;

import java.lang.reflect.Field;

/**
 * Created by Elec332 on 28-4-2015.
 */
public class Compat {

    public static final Compat instance = new Compat();
    private Compat(){
    }

    public void loadList(){
        LoadedMods.check();
        EFlux.configWrapper.registerConfig(this);
        for (Field field : LoadedMods.class.getFields()){
            if (field.getType().isAssignableFrom(Boolean.TYPE)){

            }
        }
    }

    @Configurable(validStrings = {"TRUE", "AUTO", "FALSE"})
    private String RF = compatType.AUTO.toString();





    private enum compatType{
        TRUE, AUTO, FALSE
    }
}
