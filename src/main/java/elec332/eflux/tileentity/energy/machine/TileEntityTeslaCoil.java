package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import elec332.eflux.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
@RegisteredTileEntity("TileEntityEFluxTeslaCoil")
public class TileEntityTeslaCoil extends TileEntityBreakableMachine implements ITickable {

    @SuppressWarnings("all")
    public static final DamageSource teslaCoilDamageSource = new DamageSource("TeslaCoil").setDamageBypassesArmor().setDamageAllowedInCreativeMode();

    @Override
    public void update() {
        if (timeCheck()) {
            @SuppressWarnings("unchecked")
            List<EntityLivingBase> entities = getWorld().getEntitiesWithinAABB(EntityLivingBase.class, Utils.getAABBAroundBlock(getPos(), 2, 2, 2, 2, 2, 2));
            for (EntityLivingBase entity : entities) {
                float hp = entity.getHealth();
                int energy = (int) (hp * 120);
                if (energyContainer.drainPower(energy)){
                    smite(entity);
                } else break;
            }
        }
    }

    private static void smite(EntityLivingBase target){
        target.setHealth(0.0f);
        target.onDeath(teslaCoilDamageSource);
        target.setDead();
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.GOLD_INGOT);
    }

    @Override
    public int getWorkingVoltage() {
        return 240;
    }

    @Override
    public float getAcceptance() {
        return 0.7f;
    }

    @Override
    public int getMaxRP() {
        return 50;
    }

    @Override
    public double getResistance() {
        return 1;
    }

    @Nonnull
    @Override
    public ConnectionPoint getConnectionPoint(int post) {
        return post == 0 ? cp1 : cp2;
    }

    @Nullable
    @Override
    public ConnectionPoint getConnectionPoint(EnumFacing side, Vec3d hitVec) {
        return side == EnumFacing.UP ? cp1 : (side == EnumFacing.DOWN ? cp2 : null);
    }

    @Override
    protected void createConnectionPoints() {
        cp1 = new ConnectionPoint(pos, world, EnumFacing.UP, 0);
        cp2 = new ConnectionPoint(pos, world, EnumFacing.DOWN, 0);
    }

    private ConnectionPoint cp1, cp2;

}
