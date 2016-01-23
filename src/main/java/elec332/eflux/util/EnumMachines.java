package elec332.eflux.util;

import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.tileentity.energy.generator.CoalGenerator;
import elec332.eflux.tileentity.energy.machine.*;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Elec332 on 4-4-2015.
 */
public enum EnumMachines implements IEFluxBlockMachineData {

    CAPACITOR(Capacitor.class, BlockTextures.getCapacitorProvider()),
    COAL_GENERATOR(CoalGenerator.class, BlockTextures.getCoalGenProvider()),
    ASSEMBLY_TABLE(AssemblyTable.class, BlockTextures.getAssemblyTableProvider()),
    GROWTHLAMP(TileGrowthLamp.class, 3, Material.glass, BlockTextures.getGrowthLampProvider()),
    CHUNKMAIN(MainChunkLoaderTile.class, BlockTextures.getChunkMainProvider()),
    CHUNKSUB(ChunkLoaderSubTile.class, BlockTextures.getChunkSubProvider()),
    TESLACOIL(TileTeslaCoil.class, BlockTextures.getTeslaCoilProvider()),
    SCANNER(TileScanner.class, BlockTextures.getScannerProvider()),
    WASHER(TileWasher.class, BlockTextures.getWasherProvider()),
    RUBBLESIEVE(TileRubbleSieve.class, BlockTextures.getRubbleSieveProvider()),

    ;
    //___Data__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    public Class<? extends ItemBlock> itemBlockClass;
    public boolean hasTwoStates = false;
    private BlockMachine blockMachine;
    private int renderID = 3;
    private Material material = Material.rock;
    private IBlockTextureProvider textureProvider;

    private EnumMachines(Class<? extends TileEntity> tileClass, int renderID, Material material, IBlockTextureProvider textureProvider){
        this(tileClass, renderID, material);
        this.textureProvider = textureProvider;
    }

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
        this.itemBlockClass = ItemBlock.class;
    }

    private EnumMachines (Class<? extends TileEntity> tileClass, IBlockTextureProvider textureProvider){
        this(tileClass);
        this.textureProvider = textureProvider;
    }

    public void init(){
        this.blockMachine = new BlockMachine(this);
        GameRegistry.registerBlock(blockMachine, itemBlockClass, blockMachine.blockName);
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
    public IBlockTextureProvider getTextureProvider() {
        return textureProvider;
    }

    @Override
    public void setCreativeTab(CreativeTabs creativeTabs) {
        this.blockMachine.setCreativeTab(creativeTabs);
    }

    @Override
    public int getRenderID() {
        return renderID;
    }

    @Override
    public String getName() {
        return super.toString().toLowerCase();
    }

}
