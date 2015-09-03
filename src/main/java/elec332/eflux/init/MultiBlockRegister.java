package elec332.eflux.init;

import elec332.core.multiblock.AbstractAdvancedMultiBlockStructure;
import elec332.core.multiblock.BlockData;
import elec332.core.multiblock.BlockStructure;
import elec332.core.multiblock.IMultiBlockStructure;
import elec332.core.util.BlockLoc;
import elec332.core.util.DirectionHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.BlockMachinePart;
import elec332.eflux.multiblock.MultiBlockCompressor;
import elec332.eflux.multiblock.MultiBlockFurnace;
import elec332.eflux.multiblock.MultiBlockLaser;
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
                return b1 && b2 && b3 && b4;
            }

            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
                        if (length == 1){
                            if (height == 0) {
                                if (width == 1)
                                    return itemOutlet;
                                if (width == 2)
                                    return powerInlet;
                            }
                            if (height == 1){
                                if (width == 1)
                                    return null;
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
                return powerInlet;
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
                                    return null;
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
                return b1 && b2 && b3 && b4;
            }

            @Override
            public BlockStructure getStructure() {
                return new BlockStructure(3, 3, 3, new BlockStructure.IStructureFiller() {
                    @Override
                    public BlockData getBlockAtPos(int length, int width, int height) {
                        if (height == 0 && length == 1){
                            if (width == 1)
                                return itemOutlet;
                            if (width == 2)
                                return powerInlet;
                        } else if (height == 1){
                            if (width == 1) {
                                if (length == 1)
                                    return null;
                                return heater;
                            }
                            if (width == 2 && length == 1)
                                return heater;
                            if (width == 0 && length == 1)
                                return heatResistantGlass;
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
    }
}
