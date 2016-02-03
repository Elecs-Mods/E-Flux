package elec332.eflux.client.manual;

import com.google.common.collect.Lists;
import elec332.eflux.client.ClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Elec332 on 30-1-2016.
 */
public class ManualCategory {

    protected ManualCategory(String name){
        this.name = name.toLowerCase();
        pages = Lists.newArrayList();
        subCategories = Lists.newArrayList();
    }

    final String name;
    private ItemStack displayStack;
    private List<ManualPage> pages;
    List<ManualCategory> subCategories;
    private boolean last;
    private ManualCategory superCategory;

    private String unlocalisedName;

    @SuppressWarnings("unused")
    public boolean isValidForPlayer(EntityPlayer player){
        return true;
    }

    public String getName(){
        return name;
    }

    public ManualCategory setDisplayStack(ItemStack displayStack) {
        this.displayStack = ItemStack.copyItemStack(displayStack);
        return this;
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }

    public List<ManualCategory> getSubCategories(EntityPlayer player){
        List<ManualCategory> ret = Lists.newArrayList();
        List<ManualCategory> forEach = last ? superCategory.subCategories : subCategories;
        for (ManualCategory category : forEach) {
            if (category.isValidForPlayer(player)) {
                ret.add(category);
            }
        }
        return ret;
    }

    public int getSubCategories(){
        return subCategories.size();
    }

    public List<ManualPage> getPages(){
        return pages;
    }

    public ManualCategory addPage(ManualPage page){
        page.setCategory(this);
        pages.add(page);
        return this;
    }

    public ManualCategory setUnlocalisedName(String unlocalisedName) {
        this.unlocalisedName = unlocalisedName;
        return this;
    }

    public String getLocalisedName(){
        return ClientHelper.translateToLocal(getUnLocalisedName());
    }

    public String getUnLocalisedName(){
        return unlocalisedName;
    }

    public ManualCategory setLastCategory(){
        this.last = true;
        return this;
    }

    void addSubcategory(ManualCategory category){
        subCategories.add(category);
        category.superCategory = this;
    }

}
