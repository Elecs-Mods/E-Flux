package elec332.eflux.util;

import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.items.AbstractTexturedItemBlock;
import elec332.eflux.tileentity.ender.TileEntityEnderChest;
import elec332.eflux.tileentity.energy.generator.TileEntityCoalGenerator;
import elec332.eflux.tileentity.energy.machine.*;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntityMainChunkLoader;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntitySubChunkLoader;
import elec332.eflux.tileentity.misc.TileEntityEFluxSpawner;
import elec332.eflux.tileentity.misc.TileEntityFeeder;
import elec332.eflux.tileentity.misc.TileEntityTank;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;

/**
 * Created by Elec332 on 4-4-2015.
 */
public enum EnumMachines implements IEFluxBlockMachineData {

    //CAPACITOR(TileEntityCapacitor.class),
    COAL_GENERATOR(TileEntityCoalGenerator.class),
    ASSEMBLY_TABLE(TileEntityAssemblyTable.class),
    GROWTHLAMP(TileEntityGrowthLamp.class, EnumBlockRenderType.MODEL, Material.GLASS),
    CHUNKMAIN(TileEntityMainChunkLoader.class),
    CHUNKSUB(TileEntitySubChunkLoader.class),
    TESLACOIL(TileEntityTeslaCoil.class),
    SCANNER(TileEntityScanner.class),
    WASHER(TileEntityWasher.class),
    RUBBLESIEVE(TileEntityRubbleSieve.class),
    ETCHINGMACHINE(TileEntityEtchingMachine.class),

    FEEDER(TileEntityFeeder.class),
    SPAWNER(TileEntityEFluxSpawner.class),

    TANK(TileEntityTank.class, EnumBlockRenderType.INVISIBLE, Material.ROCK),
    ENDERCHEST(TileEntityEnderChest.class)

    ;
    //___Data__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    public Class<? extends ItemBlock> itemBlockClass;
    public boolean hasTwoStates = false;
    private BlockMachine blockMachine;
    private EnumBlockRenderType renderID = EnumBlockRenderType.MODEL;
    private Material material = Material.ROCK;
    public BlockRenderLayer renderingLayer = BlockRenderLayer.SOLID;

    EnumMachines(Class<? extends TileEntity> tileClass, EnumBlockRenderType renderID, Material material){
        this(tileClass, renderID);
        this.material = material;
    }

    EnumMachines(Class<? extends TileEntity> tileClass, EnumBlockRenderType renderID){
        this(tileClass);
        this.renderID = renderID;
    }

    EnumMachines(Class<? extends TileEntity> tileClass){
        this.tileClass = tileClass;
        this.itemBlockClass = AbstractTexturedItemBlock.class;
    }

    public void init(){
        this.blockMachine = new BlockMachine(this);
        blockMachine.register();
    }

    @Override
    public boolean hasTwoStates() {
        return hasTwoStates;
    }

    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return itemBlockClass;
    }

    @Override
    public Class<? extends TileEntity> getTileClass(){
        return this.tileClass;
    }

    @Override
    public Material getBlockMaterial(){
        return this.material;
    }

    @Override
    public BlockMachine getBlock() {
        return blockMachine;
    }

    @Override
    public void setCreativeTab(CreativeTabs creativeTabs) {
        this.blockMachine.setCreativeTab(creativeTabs);
    }

    @Override
    public EnumBlockRenderType getRenderType() {
        return renderID;
    }

    @Override
    public String getName() {
        return super.toString().toLowerCase();
    }

    @Override
    public BlockRenderLayer getRenderingLayer() {
        return renderingLayer;
    }

}
