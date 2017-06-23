package elec332.eflux.blocks;

import elec332.core.client.model.loading.INoBlockStateJsonBlock;
import elec332.core.tile.BlockTileBase;
import elec332.core.tile.IActivatableMachine;
import elec332.core.util.BlockStateHelper;
import elec332.core.util.UniversalUnlistedProperty;
import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.blocks.data.IEFluxBlockMachineData;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Created by Elec332 on 30-4-2015.
 */
public class BlockMachine extends BlockTileBase implements INoBlockStateJsonBlock.RotationImpl {

    public BlockMachine(IEFluxBlockMachineData machine){
        super(machine.getBlockMaterial(), machine.getTileClass(), machine.getName(), EFlux.ModID.toLowerCase());
        setCreativeTab(EFlux.creativeTab);
        setDefaultState(BlockStateHelper.FACING_NORMAL.setDefaultMetaState(this));
        this.machine = machine;
        this.layer = machine.getRenderingLayer();
        this.has2States = machine.hasTwoStates();
    }

    public static final IUnlistedProperty<Boolean> ACTIVATED_PROPERTY;

    private final IEFluxBlockMachineData machine;
    private final boolean has2States;
    private final BlockRenderLayer layer;

    public IEFluxBlockMachineData getMachine(){
        return this.machine;
    }

    @Override
    public boolean hasTextureOverrideJson(IBlockState state) {
        return has2States;
    }

    @Override
    public void addAdditionalData(IBlockState state, Map<String, String> dataMap) {
        if (has2States){
            dataMap.put("activated", "" + ((IExtendedBlockState) state).getValue(ACTIVATED_PROPERTY));
        }
    }

    @Override
    public BlockMachine register() {
        GameRegistry.register(this);
        Class<? extends ItemBlock> itemBlockClass = machine.getItemBlockClass();
        try {
            GameRegistry.register(itemBlockClass.getConstructor(Block.class).newInstance(this), getRegistryName());
        } catch (Exception e){
            throw new RuntimeException("Error registering ItemBlock: "+itemBlockClass.getCanonicalName());
        }
        return this;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return machine.getRenderType();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return layer == BlockRenderLayer.SOLID;
    }

    @SuppressWarnings("unused")
    public static EnumFacing getFacing(IBlockAccess iba, BlockPos pos){
        return getFacing(WorldHelper.getBlockState(iba, pos));
    }

    public static EnumFacing getFacing(IBlockState state){
        return state.getValue(BlockStateHelper.FACING_NORMAL.getProperty());
    }

    @Override
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return layer;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return BlockStateHelper.FACING_NORMAL.getStateForMeta(this, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return BlockStateHelper.FACING_NORMAL.getMetaForState(state);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{BlockStateHelper.FACING_NORMAL.getProperty()}, new IUnlistedProperty[]{ACTIVATED_PROPERTY});
    }

    @Override
    @Nonnull
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        if (!has2States){
            return state;
        }
        TileEntity tile = WorldHelper.getTileAt(world, pos);
        return ((IExtendedBlockState)state).withProperty(ACTIVATED_PROPERTY, tile instanceof IActivatableMachine && ((IActivatableMachine) tile).isActive());
    }

    static {
        ACTIVATED_PROPERTY = new UniversalUnlistedProperty<>("activated", Boolean.class);
    }

}
