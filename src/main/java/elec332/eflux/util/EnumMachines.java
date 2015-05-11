package elec332.eflux.util;

import cpw.mods.fml.common.registry.GameRegistry;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.tileentity.energy.cable.BasicCable;
import elec332.eflux.tileentity.energy.generator.CoalGenerator;
import elec332.eflux.tileentity.energy.machine.AssemblyTable;
import elec332.eflux.tileentity.energy.machine.Capacitor;
import elec332.eflux.tileentity.energy.machine.TEGrinder;
import elec332.eflux.tileentity.energy.machine.furnace.T1Furnace;
import elec332.eflux.tileentity.energy.machine.grinder.TileGrinder;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 4-4-2015.
 */
public enum EnumMachines {
    GRINDER(TEGrinder.class),
    TESTCABLE(BasicCable.class),
    CAPACITOR(Capacitor.class),
    COAL_GENERATOR(CoalGenerator.class),
    ASSEMBLY_TABLE(AssemblyTable.class),
    FURNACE_TIER1(T1Furnace.class),
    GRINDER_TEST_ONEBLOCK(TileGrinder.class)


    ;
    //___DATA__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    private BlockMachine blockMachine;
    private int renderID = 0;
    private Material material = Material.rock;

    private EnumMachines(Class<? extends TileEntity> tileClass, int renderID, Material material){
        this(tileClass, renderID);
        this.material = material;
    }

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
        blockMachine.register();
    }

    public Class<? extends TileEntity> getTileClass(){
        return this.tileClass;
    }

    public Material getBlockMaterial(){
        return this.material;
    }

    public BlockMachine getBlock() {
        return blockMachine;
    }

    public void setCreativeTab(CreativeTabs creativeTabs) {
        this.blockMachine.setCreativeTab(creativeTabs);
    }

    public int getRenderID() {
        return renderID;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
