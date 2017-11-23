package elec332.eflux.items;

import elec332.core.world.WorldHelper;
import elec332.eflux.EFlux;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.energy.IEnergyObject;
import elec332.eflux.api.energy.WireConnectionMethod;
import elec332.eflux.api.util.ConnectionPoint;
import elec332.eflux.grid.energy4.Wire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 7-11-2017.
 */
public class ItemOverHeadWireCoil extends AbstractEFluxItem {

	public ItemOverHeadWireCoil(String name) {
		super(name);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseC(EntityPlayer player, EnumHand hand, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote){
			return EnumActionResult.SUCCESS;
		}
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound tag = stack.getTagCompound();
		TileEntity tile = WorldHelper.getTileAt(world, pos);
		IEnergyObject energyObject = tile == null ? null : tile.getCapability(EFluxAPI.ENERGY_CAP, null);
		if (energyObject == null){
			return EnumActionResult.SUCCESS;
		}
		Vec3d hitVec = new Vec3d(hitX, hitY, hitZ);
		ConnectionPoint cp = energyObject.getConnectionPoint(facing, hitVec);
		if (cp == null){
			return EnumActionResult.SUCCESS;
		}
		if (tag == null){
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
			tag.setLong("bpL", cp.getPos().toLong());
			if (cp.getSide() != null) {
				tag.setString("bpS", cp.getSide().getName());
			}
			tag.setInteger("bpN", cp.getSideNumber());
			tag.setDouble("xH", hitVec.x);
			tag.setDouble("yH", hitVec.y);
			tag.setDouble("zH", hitVec.z);
		} else {
			BlockPos bp = BlockPos.fromLong(tag.getLong("bpL"));
			EnumFacing bpf = tag.hasKey("bpS") ? EnumFacing.byName(tag.getString("bpS")) : null;
			int n = tag.getInteger("bpN");
			Vec3d otherHVec = new Vec3d(tag.getDouble("xH"), tag.getDouble("yH"), tag.getDouble("zH"));
			stack.setTagCompound(null);
			Wire wire = new Wire(new ConnectionPoint(bp, world, bpf, n), otherHVec, cp, hitVec, 0, null, WireConnectionMethod.OVERHEAD);
			EFlux.electricityGridHandler.addWire(wire);
		}
		return EnumActionResult.SUCCESS;
	}

}
