package elec332.eflux.init;

import elec332.core.multiblock.AbstractAdvancedMultiBlockStructure;
import elec332.core.multiblock.BlockStructure;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.core.world.location.BlockStateWrapper;
import elec332.eflux.EFlux;
import elec332.eflux.multiblock.machine.MultiBlockCompressor;
import elec332.eflux.multiblock.machine.MultiBlockFurnace;
import elec332.eflux.multiblock.machine.MultiBlockGrinder;
import elec332.eflux.multiblock.machine.MultiBlockLaser;
import elec332.eflux.tileentity.basic.TileEntityBlockMachine;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import static elec332.eflux.init.BlockRegister.*;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockRegister {

    private static final BlockStateWrapper powerInlet = new BlockStateWrapper(BlockMachineParts.POWERINLET.getBlock(), OreDictionary.WILDCARD_VALUE);
    private static final BlockStateWrapper air = new BlockStateWrapper((Block)null);

    public static void init(){
        BlockStructures.init();

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockPos bottomLeft, EnumFacing facing) {
                boolean b1 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 0, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b2 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 1, 1))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b3 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 2, 1))).getTileFacing() == facing;
                boolean b4 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).getTileFacing() == EnumFacing.DOWN;
                boolean b5 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).isOutputMode();
                boolean b6 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).getTileFacing() == EnumFacing.UP;
                boolean b7= ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).isInputMode();
                return b1 && b2 && b3 && b4 && b5 && b6 && b7;
            }

            @Override
            public BlockStructure getStructure() {
                return BlockStructures.compressor;
            }

            @Override
            public BlockStateWrapper getTriggerBlock() {
                return monitor;
            }

        }, "compressor", MultiBlockCompressor.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockPos bottomLeft, EnumFacing facing) {
                boolean b1 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 0, 1))).getTileFacing() == facing;
                boolean b2 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 3, 0, 1))).getTileFacing() == facing;
                boolean b3 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b4 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 3, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b5 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 2, 1))).getTileFacing() == facing.getOpposite();
                boolean b6 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 2, 1))).getTileFacing() == facing.getOpposite();
                boolean b7 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 3, 2, 1))).getTileFacing() == facing.getOpposite();
                //boolean b8 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 4, 1, 0))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b9 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 4, 1, 0))).isOutputMode();
                return b1 && b2 && b3 && b4 && b5 && b6 && b7 && /*b8 &&*/ b9;
            }

            @Override
            public BlockStructure getStructure() {
                return BlockStructures.laser;
            }

            @Override
            public BlockStateWrapper getTriggerBlock() {
                return monitor;
            }

        }, "laser", MultiBlockLaser.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockPos bottomLeft, EnumFacing facing) {
                boolean b1 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 0, 1, 1))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b2 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b3 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 2, 1))).getTileFacing() == facing;
                boolean b4 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).getTileFacing() == EnumFacing.DOWN;
                boolean b5 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).isOutputMode();
                boolean b6 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).getTileFacing() == EnumFacing.UP;
                boolean b7 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).isInputMode();
                return b1 && b2 && b3 && b4 && b5 && b6 && b7;
            }

            @Override
            public BlockStructure getStructure() {
                return BlockStructures.furnace;
            }

            @Override
            public BlockStateWrapper getTriggerBlock() {
                return heatResistantGlass;
            }

        }, "furnace", MultiBlockFurnace.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockPos bottomLeft, EnumFacing facing) {
                boolean b1 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 0, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b2 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 1, 1))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b4 = ((TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).getTileFacing() == EnumFacing.UP;
                boolean b5 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).isInputMode();
                return b1 && b2 && b4 && b5;
            }

            @Override
            public BlockStructure getStructure() {
                return BlockStructures.grinder;
            }

            @Override
            public BlockStateWrapper getTriggerBlock() {
                return frameNormal;
            }

        }, "grinder", MultiBlockGrinder.class);

    }

    public static class BlockStructures{

        public static BlockStructure compressor, laser, furnace, grinder;

        private static void init(){
            compressor = new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                @Override
                public BlockStateWrapper getBlockAtPos(int length, int width, int height) {
                    if (length == 1){
                        if (height == 0) {
                            if (width == 0)
                                return monitor;
                            if (width == 1)
                                return itemGate;
                            if (width == 2)
                                return powerInlet;
                        }
                        if (height == 1){
                            if (width == 1)
                                return air;
                            if (width == 2)
                                return motor;
                        }
                        if (height == 2 && width == 1){
                            return itemGate;
                        }
                    }
                    if ((length == 0 || length == 2) && width == 1 && height == 1)
                        return radiator;
                    return frameNormal;
                }
            });
            laser = new BlockStructure(5, 3, 3, new BlockStructure.IStructureFiller() {
                @Override
                public BlockStateWrapper getBlockAtPos(int length, int width, int height) {
                    if (height == 1){
                        if (width == 0 && (length == 2 || length == 3)){
                            return monitor;
                        } else if (width == 1){
                            if (length == 0)
                                return heatResistantGlass;
                            if (length == 1)
                                return laserLens;
                            if (length == 2)
                                return air;
                            if (length == 3)
                                return laserCore;
                            if(length == 4)
                                return powerInlet;
                        } else if (width == 2 && length > 0 && length < 4){
                            return radiator;
                        }
                    } else if (height == 0 && width == 1 && length == 4){
                        return itemGate;
                    }
                    return frameAdvanced;
                }
            });
            furnace = new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                @Override
                public BlockStateWrapper getBlockAtPos(int length, int width, int height) {
                    if (height == 0 && length == 1){
                        if (width == 1)
                            return itemGate;
                        if (width == 2)
                            return powerInlet;
                    } else if (height == 1){
                        if (width == 1) {
                            if (length == 1)
                                return air;
                            return heater;
                        }
                        if (width == 2 && length == 1)
                            return heater;
                        if (width == 0 && length == 1)
                            return heatResistantGlass;
                    } else if (height == 2 && width == 1 && length == 1){
                        return itemGate;
                    }
                    return frameBasic;
                }
            });
            grinder = new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                @Override
                public BlockStateWrapper getBlockAtPos(int length, int width, int height) {
                    if (length == 1){
                        if (height == 0) {
                            if (width == 0)
                                return monitor;

                            if (width == 2)
                                return powerInlet;
                        }
                        if (height == 1){
                            if (width == 1)
                                return air;
                        }
                        if (height == 2){
                            if (width == 1)
                                return itemGate;
                        }
                    }
                    if ((length == 0 || length == 2) && width == 1 && height == 1)
                        return radiator;
                    if (length == 2 && height == 0 && width == 0)
                        return dustStorage;
                    return frameNormal;
                }
            });
        }

    }

}
