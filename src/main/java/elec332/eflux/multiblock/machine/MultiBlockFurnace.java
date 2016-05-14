package elec332.eflux.multiblock.machine;

import com.google.common.collect.Lists;
import elec332.core.util.PlayerHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.FurnaceRenderTile;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multiblock.EFluxMultiBlockProcessingMachine;
import elec332.eflux.recipes.old.EnumRecipeMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;

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
        setItemOutput(1, 1, 0);
        setTile();
        //middle = getBlockLocAtTranslatedPos(1, 1, 1);
        //getWorldObj().setBlockState(middle, BlockRegister.renderBlock.getStateFromMeta(0), 3);
        //((TileEntityInsideItemRenderer)WorldHelper.getTileAt(getWorldObj(), middle)).setMultiBlock(this, getMultiBlockFacing(), getStructureID());
    }

    @SideOnly(Side.CLIENT)
    private TileEntity frt;

    private void setTile(){
        if (getWorldObj().isRemote && frt == null){
            //System.out.println("Set renderer");
            FurnaceRenderTile frt = new FurnaceRenderTile();
            frt.setWorldObj(getWorldObj());
            frt.setPos(getBlockLocAtTranslatedPos(1, 1, 1));
            frt.setMultiBlock(this, getMultiBlockFacing(), "unknown");
            this.frt = frt;
            //getWorldObj().setTileEntity(frt.getPos(), frt);
            Minecraft.getMinecraft().renderGlobal.updateTileEntities(Collections.<TileEntity>emptyList(), Lists.newArrayList((TileEntity)frt));
        }
    }

    private void removeTile(){
        if (getWorldObj().isRemote && frt != null) {
            //System.out.println("Removed renderer");
            Minecraft.getMinecraft().renderGlobal.updateTileEntities(Lists.newArrayList(frt), Collections.<TileEntity>emptyList());
            frt = null;
        }
    }

    @Override
    public int getSlots() {
        return 8;
    }

    @Override
    public void tileEntityInvalidate() {
        super.tileEntityInvalidate();
        removeTile();
    }

    @Override
    public void tileEntityValidate() {
        super.tileEntityValidate();
        setTile();
    }

    @Override
    public int getRequiredPower(int startup) {
        return 300;
    }

    @Override
    public int getRequiredPowerAfterStartup() {
        return 500;
    }

    @Override
    public int updateProgressOnItem(int oldProgress, ItemStack stack, int slot, float f) {
        return oldProgress+(startup > .4f*startupTime ? (int)(EFlux.random.nextFloat()*2.2f*((float)startup/(float)startupTime)) : 0);
    }

    @Override
    public void onProcessComplete(ItemStack stack, int slot) {
        inventory.decrStackSize(slot, 1);
        ItemStack out = FurnaceRecipes.instance().getSmeltingResult(stack);
        if (out == null){
            out = ItemRegister.scrap.copy();
        }
        if (!canAddToOutput(out)){
            setBroken(true);
            onBroken();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        removeTile();
    }

    @Override
    public void tick(int startup) {
        super.tick(startup);
        getSaveDelegate().syncData();
    }

    @Override
    public boolean onAnyBlockActivatedSafe(EntityPlayer player, EnumHand hand, ItemStack stack, BlockPos pos, IBlockState state) {
        if (player instanceof EntityPlayerMP)
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                PlayerHelper.sendMessageToPlayer(player, ""+inventory.getStackInSlot(i));
            }

        return super.onAnyBlockActivatedSafe(player, hand, stack, pos, state);
    }

    @Override
    protected int getMaxStoredPower() {
        return 5000;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Items.IRON_INGOT);
    }

    @Override
    public float getAcceptance() {
        return 0.5f;
    }

    @Override
    public int getEFForOptimalRP() {
        return hasStartedUp() ? 30 : 22;
    }

    @Override
    public EnumRecipeMachine getMachine() {
        return EnumRecipeMachine.FURNACE;
    }

    @Override
    public int getOptimalRP() {
        return 15;
    }

    @Override
    public ResourceLocation getBackgroundImageLocation() {
        return new ResourceLocation("textures/gui/container/furnace.png");
    }

}
