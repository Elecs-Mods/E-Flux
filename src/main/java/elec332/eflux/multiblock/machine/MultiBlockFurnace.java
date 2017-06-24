package elec332.eflux.multiblock.machine;

import com.google.common.collect.Lists;
import elec332.core.tile.TileBase;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.EFlux;
import elec332.eflux.client.FurnaceRenderTile;
import elec332.eflux.init.ItemRegister;
import elec332.eflux.multiblock.EFluxMultiBlockProcessingMachine;
import elec332.eflux.recipes.EnumRecipeMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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
    }

    @SideOnly(Side.CLIENT)
    private TileEntity frt;

    private void setTile(){
        if (getWorldObj().isRemote && frt == null){
            FurnaceRenderTile frt = new FurnaceRenderTile();
            TileBase.setWorld(frt, getWorldObj());
            frt.setPos(getBlockLocAtTranslatedPos(1, 1, 1));
            frt.setMultiBlock(this, getMultiBlockFacing(), "unknown");
            this.frt = frt;
            Minecraft.getMinecraft().renderGlobal.updateTileEntities(Collections.<TileEntity>emptyList(), Lists.newArrayList((TileEntity)frt));
        }
    }

    private void removeTile(){
        if (getWorldObj().isRemote && frt != null) {
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
        inventory.extractItem(slot, 1, false);
        ItemStack out = FurnaceRecipes.instance().getSmeltingResult(stack);
        if (!ItemStackHelper.isStackValid(out)){
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
