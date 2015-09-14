package elec332.eflux.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import elec332.eflux.EFlux;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Collections;
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

    private List<DustPart> content;
    private int total;
    public boolean scanned, clean, pure;

    public void addGrindResult(ItemStack stack){
        checkAdd(GrinderRecipes.instance.getGrindResult(stack));
    }

    public List<DustPart> getContent(){
        return ImmutableList.copyOf(content);
    }

    private DustPart getPart(String s){
        for (DustPart dustPart : content){
            if (dustPart.content.equals(s))
                return dustPart;
        }
        return null;
    }

    public NBTTagCompound getStack(){
        if (content.isEmpty())
            return null;
        NBTTagCompound ret = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        final boolean ensureMax = total <= 9;
        int i = 9;
        Collections.sort(content, new Comparator<DustPart>() {
            @Override
            public int compare(DustPart o1, DustPart o2) {
                return o1.nuggetAmount - o2.nuggetAmount;
            }
        });
        final float f = 9.0f/(total/2);
        List<DustPart> toRemove = Lists.newArrayList();
        for (DustPart dustPart : content){
            if (i == 0)
                break;
            int q = (int) (f * dustPart.nuggetAmount * EFlux.random.nextFloat() * 3);
            if (q > dustPart.nuggetAmount || ensureMax){
                q = dustPart.nuggetAmount;
            }
            int add = Math.min(q, i);
            if (add > 1) {
                i -= add;
                total -= add;
                dustPart.nuggetAmount -= add;
                if (dustPart.nuggetAmount <= 0) {
                    toRemove.add(dustPart);
                }
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("name", dustPart.content);
                tag.setInteger("nuggets", add);
                list.appendTag(tag);
            }
        }
        content.removeAll(toRemove);
        if (ensureMax){
            content.clear();
            total = 0;
        }
        if (list.tagCount() > 0) {
            ret.setTag("dusts", list);
            return ret;
        }
        return null;
    }

    public int wash(){
        int i = 0;
        List<DustPile.DustPart> toRemove = Lists.newArrayList();
        for (DustPile.DustPart dustPart : content) {
            if (dustPart.getContent().equals(GrinderRecipes.stoneDust)) {
                i += dustPart.getNuggetAmount();
                toRemove.add(dustPart);
            }
        }
        content.removeAll(toRemove);
        pure = true;
        return i;
    }

    public NBTTagCompound toNBT(){
        if (content.isEmpty())
            return null;
        NBTTagCompound ret = new NBTTagCompound();
        NBTTagList list = new NBTTagList();
        for (DustPart dustPart : content){
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("name", dustPart.content);
            tag.setInteger("nuggets", dustPart.nuggetAmount);
            list.appendTag(tag);
        }
        ret.setBoolean("dusts_scanned", scanned);
        ret.setTag("dusts", list);
        ret.setBoolean("dusts_clean", clean);
        ret.setBoolean("dusts_pure", pure);
        return ret;
    }

    public void readFromNBT(NBTTagCompound tagCompound){
        if (tagCompound == null || !tagCompound.hasKey("dusts"))
            return;
        NBTTagList tagList = tagCompound.getTagList("dusts", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            int q = tag.getInteger("nuggets");
            content.add(new DustPart(tag.getString("name")).addNuggets(q));
            total += q;
        }
        scanned = tagCompound.getBoolean("dusts_scanned");
        clean = tagCompound.getBoolean("dusts_clean");
        pure = tagCompound.getBoolean("dusts_pure");
    }

    public void scan(){
        scanned = true;
        clean = true;
        pure = true;
        for (DustPart dustPart : content){
            if (dustPart.getContent().equals(GrinderRecipes.scrap))
                clean = false;
            if (dustPart.getContent().equals(GrinderRecipes.stoneDust))
                pure = false;
        }
    }

    private void checkAdd(DustPart... dustParts){
        for (DustPart dustPart : dustParts){
            total += dustPart.nuggetAmount;
            boolean b = false;
            for (DustPart dustPart_ : content){
                if (dustPart.content.equals(dustPart_.content)){
                    dustPart_.nuggetAmount += dustPart.nuggetAmount;
                    b = true;
                }
            }
            if (!b)
                content.add(dustPart);
        }
    }

    public int getSize(){
        return total;
    }

    public static class DustPart{

        protected DustPart(String content){
            this.content = content;
        }

        private int nuggetAmount;
        private final String content;

        protected DustPart addNuggets(int i){
            nuggetAmount += i;
            return this;
        }

        public int getNuggetAmount() {
            return nuggetAmount;
        }

        public String getContent() {
            return content;
        }

        @Override
        public int hashCode() {
            return nuggetAmount;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof DustPart && content.equals(((DustPart) obj).content) && hashCode() == obj.hashCode();
        }

    }

    public static boolean sameContents(DustPile pile1, DustPile pile2){
        return (pile1 == null && pile2 == null) || !(pile1 == null || pile2 == null) && pile1.sameContents(pile2);
    }

    public boolean sameContents(DustPile otherPile){
        if (content.size() != otherPile.content.size() || getSize() != otherPile.getSize())
            return false;
        for (int i = 0; i < content.size(); i++) {
            if (!content.get(i).equals(otherPile.content.get(i)))
                return false;
        }
        return true;
    }

}
