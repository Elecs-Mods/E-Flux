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
    public boolean scanned;

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

    public NBTTagCompound toNBT(){
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
    }

    public void scan(){
        scanned = true;
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

    }

}
