package elec332.eflux.init;

import elec332.core.multiblock.AbstractAdvancedMultiBlockStructure;
import elec332.core.multiblock.BlockStructure;
import elec332.core.util.BlockLoc;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.core.world.location.BlockData;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockMachinePart;
import elec332.eflux.multiblock.machine.MultiBlockCompressor;
import elec332.eflux.multiblock.machine.MultiBlockFurnace;
import elec332.eflux.multiblock.machine.MultiBlockGrinder;
import elec332.eflux.multiblock.machine.MultiBlockLaser;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockItemGate;
import elec332.eflux.util.EnumMachines;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import static elec332.eflux.init.BlockRegister.*;

/**
 * Created by Elec332 on 27-8-2015.
 */
public class MultiBlockRegister {

    private static final BlockData powerInlet = new BlockData(EnumMachines.POWERINLET.getBlock(), OreDictionary.WILDCARD_VALUE);
    private static final BlockData air = new BlockData(null);//{
    //    @Override
    //    public boolean equals(Object obj) {
    //        return super.equals(obj) || ((obj instanceof BlockData) && ((BlockData) obj).block == renderBlock);
    //    }
    //};

    public static void init(){

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockLoc bottomLeft, ForgeDirection facing) {
                boolean b1 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 0, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b2 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 1, 1))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b3 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 2, 1))).getTileFacing() == facing;
                boolean b4 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).getTileFacing() == ForgeDirection.DOWN;
                boolean b5 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).isOutputMode();
                return b1 && b2 && b3 && b4 && b5;
            }

            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
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
                        }
                        if ((length == 0 || length == 2) && width == 1 && height == 1)
                            return radiator;
                        return frameNormal;
                    }
                });
            }

            @Override
            public BlockData getTriggerBlock() {
                return monitor;
            }

        }, "compressor", MultiBlockCompressor.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockLoc bottomLeft, ForgeDirection facing) {
                boolean b1 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 0, 1))).getTileFacing() == facing;
                boolean b2 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 3, 0, 1))).getTileFacing() == facing;
                boolean b3 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b4 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 3, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b5 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 2, 1))).getTileFacing() == facing.getOpposite();
                boolean b6 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 2, 1))).getTileFacing() == facing.getOpposite();
                boolean b7 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 3, 2, 1))).getTileFacing() == facing.getOpposite();
                System.out.println(""+b1+b2+b3+b4+b5+b6+b7);
                return b1 && b2 && b3 && b4 && b5 && b6 && b7;
            }

            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(5, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
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
                        }
                        return frameAdvanced;
                    }
                });
            }

            @Override
            public BlockData getTriggerBlock() {
                return monitor;
            }

        }, "laser", MultiBlockLaser.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockLoc bottomLeft, ForgeDirection facing) {
                boolean b1 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 0, 1, 1))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b2 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b3 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 2, 1))).getTileFacing() == facing;
                boolean b4 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).getTileFacing() == ForgeDirection.DOWN;
                boolean b5 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 0))).isOutputMode();
                boolean b6 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).getTileFacing() == ForgeDirection.UP;
                boolean b7 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).isInputMode();
                return b1 && b2 && b3 && b4 && b5 && b6 && b7;
            }

            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
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
            }

            @Override
            public BlockData getTriggerBlock() {
                return heatResistantGlass;
            }

        }, "furnace", MultiBlockFurnace.class);

        EFlux.multiBlockRegistry.registerMultiBlock(new AbstractAdvancedMultiBlockStructure() {

            @Override
            public boolean canCreate(EntityPlayerMP player) {
                return true;
            }

            @Override
            public boolean areSecondaryConditionsMet(World world, BlockLoc bottomLeft, ForgeDirection facing) {
                boolean b1 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 0, 1, 1))).getTileFacing() == DirectionHelper.rotateLeft(facing);
                boolean b2 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 2, 1, 1))).getTileFacing() == DirectionHelper.rotateRight(facing);
                boolean b4 = ((BlockMachinePart.TileEntityBlockMachine) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).getTileFacing() == ForgeDirection.UP;
                boolean b5 = ((TileEntityMultiBlockItemGate) WorldHelper.getTileAt(world, getTranslatedPosition(bottomLeft, facing, 1, 1, 2))).isInputMode();
                return b1 && b2 && b4 && b5;
            }

            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
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

            @Override
            public BlockData getTriggerBlock() {
                return frameNormal;
            }

        }, "grinder", MultiBlockGrinder.class);

    }
}
