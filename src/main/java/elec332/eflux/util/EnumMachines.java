package elec332.eflux.util;

import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.tileentity.ender.TileEntityEnderChest;
import elec332.eflux.tileentity.energy.generator.TileEntityCoalGenerator;
import elec332.eflux.tileentity.energy.machine.*;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntitySubChunkLoader;
import elec332.eflux.tileentity.energy.machine.chunkLoader.TileEntityMainChunkLoader;
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

    CAPACITOR(TileEntityCapacitor.class, BlockTextures.getCapacitorProvider()),
    COAL_GENERATOR(TileEntityCoalGenerator.class, BlockTextures.getCoalGenProvider()),
    ASSEMBLY_TABLE(TileEntityAssemblyTable.class, BlockTextures.getAssemblyTableProvider()),
    GROWTHLAMP(TileEntityGrowthLamp.class, EnumBlockRenderType.MODEL, Material.GLASS, BlockTextures.getGrowthLampProvider()),
    CHUNKMAIN(TileEntityMainChunkLoader.class, BlockTextures.getChunkMainProvider()),
    CHUNKSUB(TileEntitySubChunkLoader.class, BlockTextures.getChunkSubProvider()),
    TESLACOIL(TileEntityTeslaCoil.class, BlockTextures.getTeslaCoilProvider()),
    SCANNER(TileEntityScanner.class, BlockTextures.getScannerProvider()),
    WASHER(TileEntityWasher.class, BlockTextures.getWasherProvider()),
    RUBBLESIEVE(TileEntityRubbleSieve.class, BlockTextures.getRubbleSieveProvider()),
    ETCHINGMACHINE(TileEntityEtchingMachine.class, BlockTextures.getEtchingMachineProvider()),

    FEEDER(TileEntityFeeder.class, BlockTextures.getFeederProvider()),
    SPAWNER(TileEntityEFluxSpawner.class, BlockTextures.getSpawnerProvider()),

    TANK(TileEntityTank.class, EnumBlockRenderType.INVISIBLE, Material.ROCK, BlockTextures.getTankProvider()),
    ENDERCHEST(TileEntityEnderChest.class, BlockTextures.getEnderChestProvider())

    ;
    //___Data__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    public Class<? extends ItemBlock> itemBlockClass;
    public boolean hasTwoStates = false;
    private BlockMachine blockMachine;
    private EnumBlockRenderType renderID = EnumBlockRenderType.MODEL;
    private Material material = Material.ROCK;
    private IBlockTextureProvider textureProvider;
    public BlockRenderLayer renderingLayer = BlockRenderLayer.SOLID;

    private EnumMachines(Class<? extends TileEntity> tileClass, EnumBlockRenderType renderID, Material material, IBlockTextureProvider textureProvider){
        this(tileClass, renderID, material);
        this.textureProvider = textureProvider;
    }

    private EnumMachines(Class<? extends TileEntity> tileClass, EnumBlockRenderType renderID, Material material){
        this(tileClass, renderID);
        this.material = material;
    }

    private EnumMachines(Class<? extends TileEntity> tileClass, EnumBlockRenderType renderID){
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
        //GameRegistry.registerBlock(blockMachine, itemBlockClass, blockMachine.blockName);
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
    public IBlockTextureProvider getTextureProvider() {
        return textureProvider;
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
