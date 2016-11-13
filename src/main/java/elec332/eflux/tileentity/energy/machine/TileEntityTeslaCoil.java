package elec332.eflux.tileentity.energy.machine;

import elec332.core.api.registration.RegisteredTileEntity;
import elec332.eflux.tileentity.TileEntityBreakableMachine;
import elec332.eflux.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
@RegisteredTileEntity("TileEntityEFluxTeslaCoil")
public class TileEntityTeslaCoil extends TileEntityBreakableMachine implements ITickable {

    public static final DamageSource teslaCoilDamageSource = new DamageSource("TeslaCoil").setDamageBypassesArmor().setDamageAllowedInCreativeMode();

    @Override
    public void update() {
        if (timeCheck()) {
            @SuppressWarnings("unchecked")
            List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, Utils.getAABBAroundBlock(getPos(), 2, 2, 2, 2, 2, 2));
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
    protected int getMaxStoredPower() {
        return 100000;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.GOLD_INGOT);
    }

    @Override
    public float getAcceptance() {
        return 0.7f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 30;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(EnumFacing direction) {
        return direction == EnumFacing.DOWN || direction == EnumFacing.UP;
    }

    @Override
    public int getRequestedRP() {
        return 40;
    }

}
