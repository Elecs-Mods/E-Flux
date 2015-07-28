package elec332.eflux;

import elec332.core.main.ElecCore;
import elec332.core.util.BlockLoc;
import elec332.eflux.multiblock.IMultiBlock;
import elec332.eflux.multiblock.IMultiBlockTile;
import elec332.eflux.multiblock.MultiBlockRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 27-7-2015.
 */
public class TestTile extends TileEntity implements IMultiBlockTile{

    @Override
    public void validate() {
        super.validate();
        if (!worldObj.isRemote)
        ElecCore.tickHandler.registerCall(new Runnable() {
            @Override
            public void run() {
                //System.out.println("Validate, new: "+init+" Loc: "+new BlockLoc(TestTile.this));
                //if (!init)
                IMultiBlock.tileEntityValidate(TestTile.this, multiBlock, MultiBlockRegistry.instance);
                //init = true;
            }
        });

    }

    //private boolean init = false;

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if (!worldObj.isRemote)
        System.out.println("ChunkUnload");
        IMultiBlock.tileEntityChunkUnload(multiBlock, this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!worldObj.isRemote)
        System.out.println("InValidate");
        IMultiBlock.tileEntityInvalidate(multiBlock);
    }


    private IMultiBlock multiBlock;
    private ForgeDirection mbFacing = null;
    private boolean valid;
    private String structureID;

    @Override
    public void writeToNBT(NBTTagCompound p_145841_1_) {
        super.writeToNBT(p_145841_1_);
        if (mbFacing != null) {
            System.out.println(mbFacing+" "+valid+" "+structureID);
            p_145841_1_.setString("facing_E", mbFacing.toString());
            p_145841_1_.setBoolean("valid_E", valid);
            p_145841_1_.setString("structure_E", structureID);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound p_145839_1_) {
        super.readFromNBT(p_145839_1_);
        if (p_145839_1_.hasKey("facing_E")) {
            this.mbFacing = ForgeDirection.valueOf(p_145839_1_.getString("facing_E"));
            this.valid = p_145839_1_.getBoolean("valid_E");
            this.structureID = p_145839_1_.getString("structure_E");
        }
    }

    /**
     * An IMultiblock cannot be saved to NBT, every time this tile gets loaded after the tile unloaded, this gets re-assigned
     *
     * @param multiBlock The multiblock in which the tile belongs
     * @param facing     The facing of the multiblock -Save this value to NBT!
     */
    @Override
    public void setMultiBlock(IMultiBlock multiBlock, ForgeDirection facing, String structureID) {
        this.multiBlock = multiBlock;
        this.mbFacing = facing;
        this.valid = true;
        this.structureID = structureID;
    }

    /**
     * This returns if this tile is actually part of an multiblock or not
     *
     * @return weather the multiblock is valid, make sure to read/write the value from/to NBT!
     */
    @Override
    public boolean isValidMultiBlock() {
        return valid;
    }

    /**
     * This is used for saving, just like TileEntities, make sure this returns the name from the @link IMultiBlockStructure
     * HINT: Save this value to NBT aswell;
     *
     * @return the name of the structure
     */
    @Override
    public String getStructureIdentifier() {
        return structureID;
    }

    /**
     * This value should return the same value as the value in #setMultiBlock
     *
     * @return The facing of the multiblock
     */
    @Override
    public ForgeDirection getFacing() {
        return mbFacing;
    }

    /**
     * Returns the multiblock this tile belongs too, can be null
     *
     * @return said multiblock
     */
    @Override
    public IMultiBlock getMultiBlock() {
        return multiBlock;
    }
}