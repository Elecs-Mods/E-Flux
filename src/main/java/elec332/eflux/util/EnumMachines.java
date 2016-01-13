package elec332.eflux.util;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import elec332.eflux.blocks.BlockMachine;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.tileentity.energy.generator.CoalGenerator;
import elec332.eflux.tileentity.energy.machine.*;
import elec332.eflux.tileentity.energy.machine.chunkLoader.ChunkLoaderSubTile;
import elec332.eflux.tileentity.energy.machine.chunkLoader.MainChunkLoaderTile;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockPowerInlet;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Elec332 on 4-4-2015.
 */
public enum EnumMachines {

    CAPACITOR(Capacitor.class, BlockTextures.getCustomProvider("cap_side", "cap_top", "def_cap")),
    COAL_GENERATOR(CoalGenerator.class, BlockTextures.getDefaultProvider("coalGeneratorFront")),
    ASSEMBLY_TABLE(AssemblyTable.class, BlockTextures.getCustomTFProvider("at_top", "at_front")),
    GROWTHLAMP(TileGrowthLamp.class, 3, Material.glass, BlockTextures.getCustomTBProvider(BlockTextures.defaultBackTexture, "gl_facing")),
    CHUNKMAIN(MainChunkLoaderTile.class, BlockTextures.getDefaultProvider("chunkmain_front")),
    CHUNKSUB(ChunkLoaderSubTile.class, BlockTextures.getDefaultProvider("cs_front")),
    TESLACOIL(TileTeslaCoil.class, BlockTextures.getCustomSidedProvider("teslacoil_side")),
    SCANNER(TileScanner.class, BlockTextures.getDefaultProvider("scannerFront")),
    WASHER(TileWasher.class, BlockTextures.getDefaultProvider("washer_front")),
    RUBBLESIEVE(TileRubbleSieve.class),

    //Common MultiBlock parts
    POWERINLET(TileEntityMultiBlockPowerInlet.class, BlockTextures.getDefaultProvider("powerinlet_front")),

    //Machine-specific MultiBlock parts

    ;
    //___Data__//////////////////////////////////////////////////////////

    private Class<? extends TileEntity> tileClass;
    public Class<? extends ItemBlock> itemBlockClass;
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
        GameRegistry.registerTileEntity(this.tileClass, this.toString());
        this.blockMachine = new BlockMachine(this);
        GameRegistry.registerBlock(blockMachine, itemBlockClass, blockMachine.blockName);
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
