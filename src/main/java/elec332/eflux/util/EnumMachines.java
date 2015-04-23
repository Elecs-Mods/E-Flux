package elec332.eflux.util;

import cpw.mods.fml.common.registry.GameRegistry;
import elec332.eflux.blocks.machines.BlockMachine;
import elec332.eflux.test.power.CableTile;
import elec332.eflux.tileentity.TEGrinder;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 4-4-2015.
 */
public enum EnumMachines {
    GRINDER(TEGrinder.class),
    TESTCABLE(CableTile.class);



    //___DATA__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    private BlockMachine blockMachine;
    private int renderID = 0;

    private EnumMachines(Class<? extends TileEntity> tileClass, int renderID){
        this(tileClass);
        this.renderID = renderID;
    }

    private EnumMachines(Class<? extends TileEntity> tileClass){
        this.tileClass = tileClass;
    }

    public void init(){
        GameRegistry.registerTileEntity(this.tileClass, this.toString());
        this.blockMachine = new BlockMachine(this);
    }

    public TileEntity getTileEntity() {
        try {
            return this.tileClass.newInstance();
        } catch (Exception ex) {
            return null;
        }
    }

    public BlockMachine getBlockMachine() {
        return blockMachine;
    }

    public void setCreativeTab(CreativeTabs creativeTabs) {
        this.blockMachine.setCreativeTab(creativeTabs);
    }

    public int getRenderID() {
        return renderID;
    }

    public boolean hasComparatorInputOverride(){
        return IComparatorOverride.class.isAssignableFrom(this.tileClass);
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
