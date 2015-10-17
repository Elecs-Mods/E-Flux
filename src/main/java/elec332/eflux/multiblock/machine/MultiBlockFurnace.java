package elec332.eflux.multiblock.machine;

import elec332.core.player.PlayerHelper;
import elec332.core.util.BlockLoc;
import elec332.core.world.WorldHelper;
import elec332.eflux.init.BlockRegister;
import elec332.eflux.multiblock.EFluxMultiBlockProcessingMachine;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import elec332.eflux.tileentity.multiblock.TileEntityInsideItemRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Elec332 on 28-8-2015.
 */
public class MultiBlockFurnace extends EFluxMultiBlockProcessingMachine {

    public MultiBlockFurnace() {
        super();
        setStartupTime(1200);
    }

    @Override
    public void init() {
        super.init();
        middle = getBlockLocAtTranslatedPos(1, 1, 1);
        getWorldObj().setBlock(middle.xCoord, middle.yCoord, middle.zCoord, BlockRegister.renderBlock, 0, 3);
        ((TileEntityInsideItemRenderer)WorldHelper.getTileAt(getWorldObj(), middle)).setMultiBlock(this, getMultiBlockFacing(), getStructureID());
    }

    private BlockLoc middle;

    @Override
    public int getRequiredPower(int startup) {
        return 300;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 200;
    }

    @Override
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot) {
        return oldProgress+1;
    }

    @Override
    public void invalidate() {
        super.invalidate();
        getWorldObj().setBlockToAir(middle.xCoord, middle.yCoord, middle.zCoord);
    }

    @Override
    public boolean onAnyBlockActivatedSafe(EntityPlayer player) {
        if (player instanceof EntityPlayerMP)
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                PlayerHelper.sendMessageToPlayer(player, ""+inventory.getStackInSlot(i));
            }

        return super.onAnyBlockActivatedSafe(player);
    }

    @Override
    protected int getMaxStoredPower() {
        return 5000;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.iron_ingot);
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getEFForOptimalRP() {
        return 7;
    }

    @Override
    public EnumRecipeMachine getMachine() {
        return EnumRecipeMachine.FURNACE;
    }

    @Override
    protected int getOptimalRP() {
        return 5;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new ResourceLocation("textures/gui/container/furnace.png");
    }

    @Override
    public NBTTagCompound getWailaTag(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        return tag;
    }

}
