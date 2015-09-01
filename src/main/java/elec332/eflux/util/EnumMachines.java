package elec332.eflux.util;

import cpw.mods.fml.common.registry.GameRegistry;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.tileentity.multiblock.TileMultiBlockPowerInlet;
import elec332.eflux.tileentity.energy.cable.AdvancedCable;
import elec332.eflux.tileentity.energy.cable.BasicCable;
import elec332.eflux.tileentity.energy.cable.NormalCable;
import elec332.eflux.tileentity.energy.generator.CoalGenerator;
import elec332.eflux.tileentity.energy.machine.*;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 4-4-2015.
 */
public enum EnumMachines {
    TESTCABLE(BasicCable.class),
    CAPACITOR(Capacitor.class),
    COAL_GENERATOR(CoalGenerator.class),
    ASSEMBLY_TABLE(AssemblyTable.class),
    GRINDER(TileGrinder.class),
    FURNACE(TileFurnace.class),
    COMPRESSOR(Compressor.class),
    ETCHINGMACHINE(EtchingMachine.class),
    GROWTHLAMP(TileGrowthLamp.class, 0, Material.glass),
    LASER(Laser.class),
    CHUNKMAIN(MainChunkLoaderTile.class, BlockTextures.getDefaultProvider("chunkmain_front")),
    CHUNKSUB(ChunkLoaderSubTile.class),
    NORMALCABLE(NormalCable.class),
    ADVANCEDCABLE(AdvancedCable.class),
    TESLACOIL(TileTeslaCoil.class),


    //Common MultiBlock parts
    POWERINLET(TileMultiBlockPowerInlet.class),

    //Machine-specific MultiBlock parts


    ;
    //___Data__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    private BlockMachine blockMachine;
    private int renderID = 0;
    private Material material = Material.rock;
    private IBlockTextureProvider textureProvider;

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
        this.textureProvider = BlockTextures.getDefaultProvider();
    }

    private EnumMachines (Class<? extends TileEntity> tileClass, IBlockTextureProvider textureProvider){
        this(tileClass);
        this.textureProvider = textureProvider;
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

    public IBlockTextureProvider getTextureProvider() {
        return textureProvider;
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
