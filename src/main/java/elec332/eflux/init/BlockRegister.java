package elec332.eflux.init;

import elec332.core.world.location.BlockStateWrapper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.*;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import elec332.eflux.client.blocktextures.BlockTextures;
import elec332.eflux.client.blocktextures.IBlockTextureProvider;
import elec332.eflux.items.ChunkLoaderItemBlock;
import elec332.eflux.items.ItemEFluxSpawner;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import elec332.eflux.tileentity.basic.TileEntityHeater;
import elec332.eflux.tileentity.basic.TileEntityLaser;
import elec332.eflux.tileentity.multiblock.TileEntityDustStorage;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockFluidInlet;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockFluidOutlet;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockPowerInlet;
import elec332.eflux.util.EnumMachines;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by Elec332 on 24-2-2015.
 */
public final class BlockRegister {
    public static final BlockRegister instance = new BlockRegister();
    private BlockRegister(){
    }

    public static Block ores, oldCable, areaMover;
    public static BlockStateWrapper frameBasic, frameNormal, frameAdvanced, itemGate, laserLens, laserCore, heatResistantGlass, heater, monitor, radiator, motor, precisionMotor, dustStorage, powerInlet,
                                    fluidInlet, fluidOutlet;
    public static BlockStateWrapper oreCopper, oreZinc, oreSilver, oreTin;

    public void init(FMLInitializationEvent event){

        for (EnumMachines machine : EnumMachines.values()){
            switch (machine){
                case CHUNKMAIN:
                    machine.itemBlockClass = ChunkLoaderItemBlock.class;
                    break;
                case CHUNKSUB:
                    machine.itemBlockClass = ChunkLoaderItemBlock.class;
                    break;
                case COAL_GENERATOR:
                    machine.hasTwoStates = true;
                    break;
                case SPAWNER:
                    machine.renderingLayer = BlockRenderLayer.CUTOUT;
                    machine.itemBlockClass = ItemEFluxSpawner.class;
                    break;
            }
            machine.init();
        }

        for (BlockMachineParts machinePart : BlockMachineParts.values()){
            machinePart.init();
        }

        for (GlassTypes glassType : GlassTypes.values()){
            glassType.init();
        }

        ores = new BlockOres().register().setCreativeTab(EFlux.creativeTab);
        oreCopper = new BlockStateWrapper(ores, 0);
        oreTin = new BlockStateWrapper(ores, 1);
        oreSilver = new BlockStateWrapper(ores, 3);
        oreZinc = new BlockStateWrapper(ores, 2);

        Block frame = new BlockMachineFrame("blockFrame").register().setCreativeTab(EFlux.creativeTab);
        Block itemInlet = new BlockItemInlet().register().setCreativeTab(EFlux.creativeTab);

        frameBasic = new BlockStateWrapper(frame, 0);
        frameNormal = new BlockStateWrapper(frame, 1);
        frameAdvanced = new BlockStateWrapper(frame, 2);

        itemGate = newBlockStateWrapper(itemInlet);

        laserCore = BlockMachineParts.LASERCORE.getMultiBlockWrapper();
        heater = BlockMachineParts.HEATER.getMultiBlockWrapper();

        heatResistantGlass = GlassTypes.GLASS.getMultiBlockWrapper();
        laserLens = GlassTypes.LASERLENS.getMultiBlockWrapper();

        dustStorage = BlockMachineParts.DUSTSTORAGE.getMultiBlockWrapper();
        radiator = BlockMachineParts.RADIATOR.getMultiBlockWrapper();
        motor = BlockMachineParts.MOTOR.getMultiBlockWrapper();
        precisionMotor = BlockMachineParts.PRECISION_MOTOR.getMultiBlockWrapper();
        monitor = newBlockStateWrapper(new BlockMonitor().register().setCreativeTab(EFlux.creativeTab));
        powerInlet = BlockMachineParts.POWERINLET.getMultiBlockWrapper();

        fluidInlet = BlockMachineParts.FLUIDINLET.getMultiBlockWrapper();
        fluidOutlet = BlockMachineParts.FLUIDOUTLET.getMultiBlockWrapper();

        areaMover = new BlockAreaMover().register().setCreativeTab(EFlux.creativeTab);

        oldCable = new BlockCable("efluxCable").register();
    }

    private BlockStateWrapper newBlockStateWrapper(Block block){
        return new BlockStateWrapper(block, OreDictionary.WILDCARD_VALUE);
    }

    public enum BlockMachineParts implements IEFluxBlockMachineData {

        LASERCORE(BlockTextures.getDefaultProvider("laserCoreFront")),
        HEATER(TileEntityHeater.class, BlockTextures.getDefaultProvider("heaterFront")),
        DUSTSTORAGE(TileEntityDustStorage.class, BlockTextures.getCustomProvider("dustStorage", "default_side", "default_side")),
        RADIATOR(BlockTextures.getDefaultProvider("radiator")),
        MOTOR(BlockTextures.getDefaultProvider("motor")),
        PRECISION_MOTOR(BlockTextures.getDefaultProvider("precisionMotor")),
        FLUIDINLET(TileEntityMultiBlockFluidInlet.class, BlockTextures.getDefaultProvider("???")),
        FLUIDOUTLET(TileEntityMultiBlockFluidOutlet.class, BlockTextures.getDefaultProvider("???")),

        POWERINLET(TileEntityMultiBlockPowerInlet.class, BlockTextures.getDefaultProvider("powerinlet_front")),

        ;

        private BlockMachineParts(IBlockTextureProvider textureProvider){
            this(TileEntityBlockMachine.class, textureProvider);
        }

        private BlockMachineParts(Class<? extends TileEntity> tileClass, IBlockTextureProvider textureProvider){
            this.tileClass = tileClass;
            this.textureProvider = textureProvider;
        }

        private final Class<? extends TileEntity> tileClass;
        private final IBlockTextureProvider textureProvider;

        public void init(){
            this.block = new BlockMachine(this).register();
            //GameRegistry.registerBlock(block, getName());
            this.bsw = new BlockStateWrapper(block, OreDictionary.WILDCARD_VALUE);
        }

        private Block block;
        private BlockStateWrapper bsw;

        @Override
        public boolean hasTwoStates() {
            return false;
        }

        @Override
        public Class<? extends ItemBlock> getItemBlockClass() {
            return ItemBlock.class;
        }

        @Override
        public Class<? extends TileEntity> getTileClass() {
            return tileClass;
        }

        @Override
        public Material getBlockMaterial() {
            return Material.ROCK;
        }

        @Override
        public Block getBlock() {
            return block;
        }

        public BlockStateWrapper getMultiBlockWrapper(){
            return bsw;
        }

        @Override
        public IBlockTextureProvider getTextureProvider() {
            return textureProvider;
        }

        @Override
        public void setCreativeTab(CreativeTabs creativeTabs) {
            block.setCreativeTab(creativeTabs);
        }

        @Override
        public EnumBlockRenderType getRenderType() {
            return EnumBlockRenderType.MODEL;
        }

        @Override
        public String getName() {
            return super.toString().toLowerCase();
        }

        @Override
        public BlockRenderLayer getRenderingLayer() {
            return BlockRenderLayer.SOLID;
        }
    }

    private enum GlassTypes implements IEFluxBlockMachineData {
        GLASS(TileEntityBlockMachine.class, BlockTextures.getHeatGlassProvider()),
        LASERLENS(TileEntityLaser.class, BlockTextures.getLaserLensProvider())
        ;

        private GlassTypes(Class<? extends TileEntity> tileClass, IBlockTextureProvider textureProvider){
            this.textureProvider = textureProvider;
            this.tileClass = tileClass;
        }

        private final IBlockTextureProvider textureProvider;
        private final Class<? extends TileEntity> tileClass;

        public void init(){
            this.block = new BlockMachineGlass(this).register();
            //GameRegistry.registerBlock(block, getName());
            this.bsw = new BlockStateWrapper(block, OreDictionary.WILDCARD_VALUE);
        }

        private Block block;
        private BlockStateWrapper bsw;

        @Override
        public boolean hasTwoStates() {
            return false;
        }

        @Override
        public Class<? extends ItemBlock> getItemBlockClass() {
            return ItemBlock.class;
        }

        @Override
        public Class<? extends TileEntity> getTileClass() {
            return tileClass;
        }

        @Override
        public Material getBlockMaterial() {
            return Material.GLASS;
        }

        @Override
        public Block getBlock() {
            return block;
        }

        public BlockStateWrapper getMultiBlockWrapper(){
            return bsw;
        }

        @Override
        public IBlockTextureProvider getTextureProvider() {
            return textureProvider;
        }

        @Override
        public void setCreativeTab(CreativeTabs creativeTabs) {
            block.setCreativeTab(creativeTabs);
        }

        @Override
        public EnumBlockRenderType getRenderType() {
            return EnumBlockRenderType.MODEL;
        }

        @Override
        public String getName() {
            return super.toString().toLowerCase();
        }

        @Override
        public BlockRenderLayer getRenderingLayer() {
            return BlockRenderLayer.CUTOUT;
        }

    }

}
