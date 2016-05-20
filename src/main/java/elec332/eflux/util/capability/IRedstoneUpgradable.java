package elec332.eflux.util.capability;

import com.google.common.base.Predicate;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

/**
 * Created by Elec332 on 22-4-2016.
 */
public interface IRedstoneUpgradable {

    default public Predicate<Mode> getModePredicate(){
        return IRedstoneUpgradable.DEFAULT_MODE;
    }

    default public boolean isRedstonePowered(TileEntity tile) {
        return tile.hasCapability(RedstoneCapability.CAPABILITY, null) && tile.getCapability(RedstoneCapability.CAPABILITY, null).isPowered(tile.getWorld(), tile.getPos());
    }

    public enum Mode {

        HIGH {
            @Override
            public boolean isPowered(boolean hasRedstone) {
                return hasRedstone;
            }
        }, LOW {
            @Override
            public boolean isPowered(boolean hasRedstone) {
                return !hasRedstone;
            }
        }, IGNORED {
            @Override
            public boolean isPowered(boolean hasRedstone) {
                return true;
            }
        };

        public abstract boolean isPowered(boolean hasRedstone);

    }

    static final Predicate<Mode> DEFAULT_MODE = new Predicate<Mode>() {
        @Override
        public boolean apply(@Nullable Mode input) {
            return true;
        }
    };

}
