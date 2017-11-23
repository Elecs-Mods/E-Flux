package elec332.eflux.tileentity;

import com.google.common.collect.Sets;
import elec332.core.api.registration.RegisteredTileEntity;
import elec332.core.tile.TileBase;
import elec332.core.util.ItemStackHelper;
import elec332.core.world.WorldHelper;
import elec332.eflux.grid.energy4.Wire;
import elec332.eflux.grid.energy4.WireGridHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.Set;

/**
 * Created by Elec332 on 7-11-2017.
 */
@RegisteredTileEntity("htdjhcg")
public class TileWireConnector extends TileBase implements IWireConnector {

	public TileWireConnector(){
		//connectors = Sets.newHashSet();
		//connectors_ = Collections.unmodifiableSet(connectors);
	}

	@Override
	public boolean onBlockActivated(IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		WireGridHandler.INSTANCE.onActivated(this, player, hand, side, new Vec3d(hitX, hitY, hitZ));
		return super.onBlockActivated(state, player, hand, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockRemoved() {
		WireGridHandler.INSTANCE.removeBlock(this);
		super.onBlockRemoved();
	}
/*
	private void modifySet(BlockPos pos, boolean add){
		if (!world.isRemote){
			NBTTagCompound data = new NBTTagCompound();
			data.setBoolean("add", add);
			data.setLong("posE", pos.toLong());
			sendPacket(2, data);
		}
		if (add){
			connectors.add(pos);
		} else {
			connectors.remove(pos);
		}
	}

	private Set<Wire> connectors, connectors_;

	public Set<BlockPos> getConnectors(){
		return connectors_;
	}

	@Override
	public void onDataPacket(int id, NBTTagCompound tag) {
		if (id == 2){
			modifySet(BlockPos.fromLong(tag.getLong("posE")), tag.getBoolean("add"));
			return;
		}
		super.onDataPacket(id, tag);
	}
*/
	@Override
	public int getMaxDifferentConnections() {
		return 1;
	}

	@Override
	public int getConnectionLocationFromHitVec(EnumFacing side, Vec3d hit) {
		return side == EnumFacing.UP ? 1 : -1;
	}

	@Override
	public double getResistance() {
		return 0;
	}
}
