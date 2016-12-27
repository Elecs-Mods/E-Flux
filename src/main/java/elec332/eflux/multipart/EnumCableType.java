package elec332.eflux.multipart;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 17-12-2016.
 */
public enum EnumCableType implements IStringSerializable {

    BASIC(0, 10, 5),
    NORMAL(1, 50, 20),
    ADVANCED(2, 200, 50);

    EnumCableType(int meta, int maxEF, int maxRP){
        this.meta = meta;
        this.maxEF = maxEF;
        this.maxRP = maxRP;
        this.name = "eflux_"+toString().toLowerCase();
    }

    private final int meta, maxEF, maxRP;
    private final String name;

    public int getMeta(){
        return meta;
    }

    public int getMaxEF(){
        return maxEF;
    }

    public int getMaxRP() {
        return maxRP;
    }

    @Override
    @Nonnull
    public String getName() {
        return name;
    }

}
