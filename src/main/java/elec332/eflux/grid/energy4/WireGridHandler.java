package elec332.eflux.grid.energy4;

import com.google.common.collect.Sets;
import elec332.core.grid.AbstractGridHandler;
import elec332.core.util.ItemStackHelper;
import elec332.core.util.PlayerHelper;
import elec332.core.world.DimensionCoordinate;
import elec332.core.world.IMultiWorldPositionedObjectHolder;
import elec332.core.world.WorldHelper;
import elec332.eflux.api.energy.IWireType;
import elec332.eflux.tileentity.IWireConnector;
import elec332.eflux.util.TestWires;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 7-11-2017.
 */
public final class WireGridHandler extends AbstractGridHandler<WireNode> {

	public static final WireGridHandler INSTANCE = new WireGridHandler();

	private WireGridHandler(){
		removeWarningOverride = true;
		grids = Sets.newHashSet();
	}

	private Set<WireGrid> grids;

	public <T extends TileEntity & IWireConnector>void removeBlock(T connector){

	}

	public <T extends TileEntity & IWireConnector> void onActivated(T connector, EntityPlayer player, EnumHand hand, EnumFacing side, Vec3d hit){
		if (connector.getWorld().isRemote){
			return;
		}
		ItemStack stack = player.getHeldItem(hand);
		if (ItemStackHelper.isStackValid(stack) && stack.getItem() == Items.STICK){
			int connection = connector.getConnectionLocationFromHitVec(side, hit);
			if (connection == -1){
				return;
			}
			WireNode node = getObject(DimensionCoordinate.fromTileEntity(connector));
			if (node.wires.get(connection).size() >= connector.getMaxConnectionsPerPoint()){
				PlayerHelper.sendMessageToPlayer(player, "You can not add more connections to this node.");
				return;
			}
			NBTTagCompound tag = stack.getTagCompound();
			if (tag == null || !tag.hasKey("efluxlongval")){
				NBTTagCompound wr = new NBTTagCompound();
				wr.setLong("efluxlongval", connector.getPos().toLong());
				wr.setDouble("xH", hit.x);
				wr.setDouble("yH", hit.y);
				wr.setDouble("zH", hit.z);
				wr.setString("side", side.name());
				stack.setTagCompound(wr);
			} else {
				BlockPos parent = BlockPos.fromLong(tag.getLong("efluxlongval"));
				if (parent.equals(connector.getPos())){
					return;
				}
				if (!WorldHelper.chunkLoaded(connector.getWorld(), parent)){
					PlayerHelper.sendMessageToPlayer(player, "The starting location of the wire is currently not loaded.");
					return;
				}
				TileEntity jhyt = WorldHelper.getTileAt(connector.getWorld(), parent);
				if (!(jhyt instanceof IWireConnector)){
					PlayerHelper.sendMessageToPlayer(player, "Something has changed at your original location.");
					stack.setTagCompound(null);
					return;
				}
				IWireConnector otherConnector = (IWireConnector) jhyt;
				Vec3d otherVec = new Vec3d(tag.getDouble("xH"), tag.getDouble("yH"), tag.getDouble("zH"));
				EnumFacing otherHitSide = EnumFacing.byName(tag.getString("side"));
				int otherConnection = otherConnector.getConnectionLocationFromHitVec(otherHitSide, otherVec);
				if (otherConnection == -1){
					PlayerHelper.sendMessageToPlayer(player, "Something has changed at your original location.");
					stack.setTagCompound(null);
					return;
				}
				WireNode otherNode = getObject(new DimensionCoordinate(connector.getWorld(), parent));
				if (otherNode.wires.get(connection).size() >= otherConnector.getMaxConnectionsPerPoint()){
					PlayerHelper.sendMessageToPlayer(player, "You can not add more connections to this node.");
					return;
				}
				//We can finally start connecting the 2 -_-
				IWireType wireType = TestWires.TEST1;
				double gauge = 5;
				WireGrid grid = node.grids.get(connection);
				WireGrid otherGrid = otherNode.grids.get(otherConnection);
				Wire wire = null;//new Wire(connector.getPos(), hit, parent, otherVec, gauge, wireType);
				if (grid != null){
					grid.add(otherNode, otherConnection, wire, node, connection);
				} else if (otherGrid != null){
					otherGrid.add(node, connection, wire, otherNode, otherConnection);
				} else {
					WireGrid newGrid = new WireGrid(node, connection);
					newGrid.add(otherNode, otherConnection, wire, node, connection);
				}
				stack.setTagCompound(null);
			}
		}
	}

	@Override
	protected Supplier<IMultiWorldPositionedObjectHolder<WireNode>> getWorldPosObjHolder() {
		return WireNodeStorage.INSTANCE;
	}

	@Override
	protected void onObjectRemoved(WireNode wireNode, Set<DimensionCoordinate> updates) {
		boolean one = true;
		for (int i : wireNode.wires.keySet()){
			if (wireNode.wires.get(i).size() > 1){
				one = false;
				break;
			}
		}
		if (one){
			for (WireGrid grid : wireNode.grids.values()){
				grid.remove(wireNode);
			}
		} else {
			for (WireGrid grid : wireNode.grids.values()){
				for (WireNode node : grid.nodes){
					if (!updates.contains(node.getPosition()) && !wireNode.equals(node)) {
						add.add(node.getPosition());

					}
				}
				grid.dissolve();
				grids.remove(grid);
			}
		}
		for (int i : wireNode.wires.keySet()){
			for (Wire wire : wireNode.wires.get(i)){
				wire.drop();
			}
		}
	}

	@Override
	protected void internalAdd(WireNode wirenode) {
		//
	}

	@Override
	protected void removeObject(DimensionCoordinate dimensionCoordinate) {
	}

	@Override
	public void tick() {
	}



	@Override
	public boolean isValidObject(TileEntity tileEntity) {
		return tileEntity instanceof IWireConnector;
	}

	@Override
	protected WireNode createNewObject(TileEntity tileEntity) {
		return new WireNode(DimensionCoordinate.fromTileEntity(tileEntity));
	}

}
