package elec332.eflux.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.eflux.EFlux;
import elec332.eflux.init.ItemRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Elec332 on 10-9-2015.
 */
public class DustPile {

    public static DustPile newDustPile(){
        return new DustPile();
    }

    public static DustPile fromNBT(NBTTagCompound tagCompound){
        DustPile ret = newDustPile();
        ret.readFromNBT(tagCompound);
        return ret;
    }

    private DustPile(){
        this.content = Lists.newArrayList();
    }

    private List<GrinderRecipes.OreDictStack> content;
    private int total;
    public boolean scanned, clean, pure;

    public void addGrindResult(ItemStack stack){
        checkAdd(GrinderRecipes.instance.getGrindResult(stack));
    }

    public List<GrinderRecipes.OreDictStack> getContent(){
        return ImmutableList.copyOf(content);
    }

    public NBTTagCompound getStack(){
        if (content.isEmpty()) {
            return null;
        }
        System.out.println(content);
        NBTTagCompound ret = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        final boolean ensureMax = total <= 9;
        int i = 9;
        content.sort(Comparator.comparingInt(o -> o.amount));
        final float f = 9.0f/(total/2);
        System.out.println(f);
        List<GrinderRecipes.OreDictStack> toRemove = Lists.newArrayList();
        for (GrinderRecipes.OreDictStack dustPart : content){
            if (i == 0) {
                break;
            }
            int q = (int) (f * dustPart.amount * EFlux.random.nextFloat() * 3);
            if (q > dustPart.amount || ensureMax){
                q = dustPart.amount;
            }
            int add = Math.min(q, i);
            if (add > 1) {
                i -= add;
                total -= add;
                dustPart.amount -= add;
                if (dustPart.amount <= 0) {
                    toRemove.add(dustPart);
                }
                GrinderRecipes.OreDictStack copy = dustPart.copy();
                copy.amount = add;
                NBTTagCompound tag = new NBTTagCompound();
                copy.writeToNBT(tag);
                tag.setInteger("nuggets", add);
                list.appendTag(tag);
            }
        }
        content.removeAll(toRemove);
        if (ensureMax){
            content.clear();
            total = 0;
        }
        System.out.println("tags: "+list.tagCount());
        if (list.tagCount() > 0) {
            ret.setTag("dusts", list);
            return ret;
        }
        return null;
    }

    public int getAmount(String name){
        int i = 0;
        for (GrinderRecipes.OreDictStack dustPart : content) {
            if (dustPart.name.equals(name)) {
                i += dustPart.amount;
            }
        }
        return i;
    }

    public int wash(){
        pure = true;
        return removeAll(GrinderRecipes.stoneDust);
    }

    public int sieve(){
        clean = true;
        return removeAll(GrinderRecipes.scrap);
    }

    private int removeAll(String name){
        int i = 0;
        List<GrinderRecipes.OreDictStack> toRemove = Lists.newArrayList();
        for (GrinderRecipes.OreDictStack dustPart : content) {
            if (dustPart.name.equals(name)) {
                i += dustPart.amount;
                toRemove.add(dustPart);
            }
        }
        content.removeAll(toRemove);
        total -= i;
        return i;
    }

    @Nullable
    public NBTTagCompound toNBT(){
        if (content.isEmpty()) {
            return null;
        }
        NBTTagCompound ret = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (GrinderRecipes.OreDictStack dustPart : content){
            NBTTagCompound tag = new NBTTagCompound();
            dustPart.writeToNBT(tag);
            list.appendTag(tag);
        }
        ret.setBoolean("dusts_scanned", scanned);
        ret.setTag("dusts", list);
        ret.setBoolean("dusts_clean", clean);
        ret.setBoolean("dusts_pure", pure);
        return ret;
    }

    public void readFromNBT(NBTTagCompound tagCompound){
        if (tagCompound == null || !tagCompound.hasKey("dusts")) {
            return;
        }
        NBTTagList tagList = tagCompound.getTagList("dusts", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            GrinderRecipes.OreDictStack stack = GrinderRecipes.OreDictStack.readFromNBT(tag);
            if (stack == null){
                continue;
            }
            addPart(stack);
            total += stack.amount;
        }
        scanned = tagCompound.getBoolean("dusts_scanned");
        clean = tagCompound.getBoolean("dusts_clean");
        pure = tagCompound.getBoolean("dusts_pure");
    }

    public ItemStack scan(int stacks){
        scanned = true;
        clean = true;
        pure = true;
        for (GrinderRecipes.OreDictStack dustPart : content){
            if (dustPart.name.equals(GrinderRecipes.scrap)) {
                clean = false;
            }
            if (dustPart.name.equals(GrinderRecipes.stoneDust)) {
                pure = false;
            }
        }

        ItemStack ret = getContentStack(stacks);
        if (ret != null) {
            return ret;
        }
        ret = new ItemStack(ItemRegister.groundMesh);
        ret.stackSize = stacks;
        ret.setTagCompound(toNBT());
        return ret;
    }

    @Nullable
    public ItemStack getContentStack(int stacks){
        if (content.size() == 1 && (content.get(0).amount * stacks) % 9 == 0) {
            GrinderRecipes.OreDictStack s = content.get(0);
            int size = (s.amount * stacks) / 9;
            List<ItemStack> stacks1 = OreDictionary.getOres(s.name);
            if (!(stacks1 == null || stacks1.isEmpty())) {
                ItemStack ret = stacks1.get(0).copy();
                ret.stackSize = size;
                content.clear();
                return ret;
            }
        }
        return null;
    }

    public void add(DustPile pile){
        if (!(scanned && pile.scanned)){
            scanned = false;
        }
        if (!(pure && pile.pure)){
            pure = false;
        }
        if (!(clean && pile.clean)){
            clean = false;
        }
        checkAdd(pile.content);
    }

    private void checkAdd(List<GrinderRecipes.OreDictStack> dustParts){
        for (GrinderRecipes.OreDictStack dustPart : dustParts){
            addPart(dustPart);
        }
    }

    private void addPart(GrinderRecipes.OreDictStack dustPart){
        total += dustPart.amount;
        boolean b = false;
        for (GrinderRecipes.OreDictStack dustPart_ : content){
            if (dustPart_.canMerge(dustPart)){
                dustPart_.merge(dustPart);
                b = true;
            }
        }
        if (!b) {
            content.add(dustPart);
        }
    }

    public int getSize(){
        return total;
    }

}
