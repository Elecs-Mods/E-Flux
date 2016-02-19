package elec332.eflux.client.manual;

import com.google.common.base.Strings;
import elec332.eflux.client.ManualInit;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 30-1-2016.
 */
public enum ManualHandler {

    instance;

    private ManualHandler(){
        mainPage = new ManualCategory("Manual-Main");
    }

    private ManualCategory mainPage;

    @Nonnull
    public ManualCategory registerCategory(String category){
        if (Strings.isNullOrEmpty(category)){
            throw new IllegalArgumentException();
        }
        String[] s = category.split("/");
        ManualCategory pre = s.length > 1 ? getCategory(s[s.length - 2]) : mainPage;
        if (pre == null){
            throw new IllegalArgumentException("Sub-categories for "+category+" have to be registered first!");
        }
        ManualCategory ret = getCategory(category);
        if (ret == null){
            if (pre.getSubCategories() < 4) {
                ret = new ManualCategory(category);
                pre.addSubcategory(ret);
                return ret;
            } else {
                throw new IllegalStateException("There is currently only support for 4 categories.");
            }
        } else {
            return ret;
        }
    }

    @Nullable
    public ManualCategory getCategory(@Nonnull String category){
        if (Strings.isNullOrEmpty(category)){
            throw new IllegalArgumentException();
        }
        String[] s = category.split("/");
        int i = 0;
        ManualCategory latestC = mainPage;
        while (i < s.length){
            if (latestC == null){
                return null;
            }
            latestC = getCategory(s[i], latestC);
            i++;
        }
        return latestC;
    }

    private ManualCategory getCategory(String category, ManualCategory page){
        for (ManualCategory c : page.subCategories){
            if (c.getName().toLowerCase().equals(category.toLowerCase())){
                return c;
            }
        }
        return null;
    }

    public List<ManualCategory> getCategoriesForPlayer(EntityPlayer player, ManualCategory c){
        if (player != null){
            return c.getSubCategories(player);
        }
        throw new IllegalArgumentException();
    }

    public ManualCategory getMainCategory(){
        return mainPage;
    }

    public void debug(){
        mainPage = new ManualCategory("Manual-Main");
        ManualInit.init();
    }



}
