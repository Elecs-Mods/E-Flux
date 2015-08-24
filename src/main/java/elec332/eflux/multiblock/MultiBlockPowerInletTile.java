package elec332.eflux.multiblock;

import elec332.core.multiblock.IMultiBlock;
import elec332.core.multiblock.IMultiBlockTile;
import elec332.core.multiblock.MultiBlockData;
import elec332.eflux.EFlux;
import elec332.eflux.items.Components;
import elec332.eflux.tileentity.BreakableReceiverTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 28-7-2015.
 */
public class MultiBlockPowerInletTile extends BreakableReceiverTile implements IMultiBlockTile {

    public MultiBlockPowerInletTile(){
        super();
        this.multiBlockData = new MultiBlockData(this, EFlux.multiBlockRegistry);
    }

    private MultiBlockData multiBlockData;

    @Override
    public boolean onBlockActivatedSafe(EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        return getMultiBlock() instanceof EFluxMultiBlockMachine?((EFluxMultiBlockMachine)getMultiBlock()).onAnyBlockActivated(player):super.onBlockActivatedSafe(player, side, hitX, hitY, hitZ);
    }

    @Override
    protected int getMaxStoredPower() {
        return getMultiBlock() instanceof EFluxMultiBlockMachine ? ((EFluxMultiBlockMachine)getMultiBlock()).getMaxStoredPower() : energyContainer.getMaxStoredEnergy();
    }

    /**
     * The amount returned here is NOT supposed to change, anf if it does,
     * do not forget that it will receive a penalty if the machine is not running at optimum RP
     *
     * @return the amount of requested EF
     */
    @Override
    public int getEFForOptimalRP() {
        return getMultiBlock() instanceof EFluxMultiBlockMachine ? ((EFluxMultiBlockMachine)getMultiBlock()).getEFForOptimalRP() : 0;
    }

    @Override
    public ItemStack getRandomRepairItem() {
        return new ItemStack(Components.component, 1, EFlux.random.nextInt(5));
    }

    @Override
    public float getAcceptance() {
        return getMultiBlock() instanceof EFluxMultiBlockMachine ? ((EFluxMultiBlockMachine)getMultiBlock()).getAcceptance() : 20.0f;
    }

    /**
     * @param direction the direction from which a connection is requested
     * @return weather the tile can connect and accept power from the given side
     */
    @Override
    public boolean canAcceptEnergyFrom(ForgeDirection direction) {
        return getFacing() == direction;
    }

    @Override
    public int getRequestedRP() {
        return getMultiBlock() instanceof EFluxMultiBlockMachine ? ((EFluxMultiBlockMachine)getMultiBlock()).requestedRP() : 0;
    }

    /**
     * An IMultiblock cannot be saved to NBT, every time this tile gets loaded after the tile unloaded, this gets re-assigned
     *
     * @param multiBlock The multiblock in which the tile belongs
     * @param facing     The facing of the multiblock -Save this value to NBT!
     * @param structure  The identifier of the multiblock-structure -Save this value to NBT aswell!
     */
    @Override
    public void setMultiBlock(IMultiBlock multiBlock, ForgeDirection facing, String structure) {
        multiBlockData.setMultiBlock(multiBlock, facing, structure);
    }

    /**
     * When an multiblock becomes invalid, this method will get called, use it
     * to set the multiblock to null, and make sure that #isValidMultiBlock() returns false after this method finished processing!
     */
    @Override
    public void invalidateMultiBlock() {
        multiBlockData.isValidMultiBlock();
    }

    /**
     * This returns if this tile is actually part of an multiblock or not
     *
     * @return weather the multiblock is valid, make sure to read/write the value from/to NBT!
     */
    @Override
    public boolean isValidMultiBlock() {
        return multiBlockData.isValidMultiBlock();
    }

    /**
     * This is used for saving, just like TileEntities, make sure this returns the name from the @link IMultiBlockStructure
     * HINT: Save this value to NBT aswell;
     *
     * @return the name of the structure
     */
    @Override
    public String getStructureIdentifier() {
        return multiBlockData.getStructureIdentifier();
    }

    /**
     * This value should return the same value as the value in #setMultiBlock
     *
     * @return The facing of the multiblock
     */
    @Override
    public ForgeDirection getFacing() {
        return multiBlockData.getFacing();
    }

    /**
     * Returns the multiblock this tile belongs too, can be null
     *
     * @return said multiblock
     */
    @Override
    public IMultiBlock getMultiBlock() {
        return multiBlockData.getMultiBlock();
    }

    @Override
    public String[] getProvidedData() {
        return new String[0];
    }
}
