package elec332.eflux.tileentity.energy.machine;

import elec332.eflux.tileentity.BreakableReceiverTile;
import elec332.eflux.util.Utils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by Elec332 on 21-7-2015.
 */
public class TileTeslaCoil extends BreakableReceiverTile {

    public static final DamageSource teslaCoilDamageSource = new DamageSource("TeslaCoil").setDamageBypassesArmor().setDamageAllowedInCreativeMode();

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (timeCheck()) {
            @SuppressWarnings("unchecked")
            List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, Utils.getAABBAroundBlock(myLocation(), 2, 2, 2, 2, 2, 2));
            for (EntityLivingBase entity : entities) {
                float hp = entity.getHealth();
                int energy = (int) (hp * 120);
                if (storedPower >= energy){
                    smite(entity);
                    storedPower -= energy;
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
        return new ItemStack(Items.gold_ingot);
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
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return direction == ForgeDirection.DOWN || direction == ForgeDirection.UP;
    }

    /**
     * @param direction The requested direction
     * @return The Redstone Potential at which the machine wishes to operate
     */
    @Override
    public int requestedRP(ForgeDirection direction) {
        return 40;
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }
}
