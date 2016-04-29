package elec332.eflux.util;

import com.google.common.base.Predicate;

/**
 * Created by Elec332 on 22-4-2016.
 */
public interface IRedstoneUpgradable {

    public Predicate<Mode> getModePredicate();

    public boolean isRedstonePowered();

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

}
