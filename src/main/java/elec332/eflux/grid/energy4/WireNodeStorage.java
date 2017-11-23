package elec332.eflux.grid.energy4;

import elec332.core.api.data.IExternalSaveHandler;
import elec332.core.util.NBTTypes;
import elec332.core.world.DefaultMultiWorldPositionedObjectHolder;
import elec332.core.world.DimensionCoordinate;
import elec332.core.world.IMultiWorldPositionedObjectHolder;
import elec332.core.world.PositionedObjectHolder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.SerializationException;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 8-11-2017.
 */
public enum WireNodeStorage implements IExternalSaveHandler, Supplier<IMultiWorldPositionedObjectHolder<WireNode>> {

	INSTANCE;

	WireNodeStorage(){
	}

	private IMultiWorldPositionedObjectHolder<WireNode> wireData;

	@Override
	public String getName() {
		return "ElectricityWireData";
	}

	@Override
	public IMultiWorldPositionedObjectHolder<WireNode> get() {
		if (wireData == null){
			throw new RuntimeException();
		}
		return wireData;
	}

	@Override
	public void load(ISaveHandler saveHandler, WorldInfo info, NBTTagCompound tag) {
		if (tag == null){
			tag = new NBTTagCompound();
		}
		IMultiWorldPositionedObjectHolder<WireNode> newD = new DefaultMultiWorldPositionedObjectHolder<>();
		NBTTagList list = tag.getTagList("worldboundholders", NBTTypes.COMPOUND.getID());
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound wbT = list.getCompoundTagAt(i);
			int world = wbT.getInteger("worldid");
			PositionedObjectHolder<WireNode> posH = newD.getOrCreate(world);
			NBTTagList objects = wbT.getTagList("worldobjects", NBTTypes.COMPOUND.getID());
			for (int j = 0; j < objects.tagCount(); j++) {
				NBTTagCompound object = objects.getCompoundTagAt(j);
				DimensionCoordinate coord = DimensionCoordinate.fromNBT(object.getCompoundTag("objLoc"));
				if (world != coord.getDimension()){
					throw new SerializationException("De");
				}
				WireNode node = new WireNode(coord);
				node.deserializeNBT(object.getCompoundTag("objData"));
				posH.put(node, coord.getPos());
			}
		}
		this.wireData = newD;
	}

	@Nullable
	@Override
	public NBTTagCompound save(ISaveHandler saveHandler, WorldInfo info) {
		NBTTagCompound ret = new NBTTagCompound();
		NBTTagList newD = new NBTTagList();
		wireData.getUnModifiableView().forEach(new BiConsumer<Integer, PositionedObjectHolder<WireNode>>() {
			@Override
			public void accept(Integer world, PositionedObjectHolder<WireNode> objHolder) {
				NBTTagCompound wbT = new NBTTagCompound();
				wbT.setInteger("worldid", world);
				NBTTagList objects = new NBTTagList();
				objHolder.getChunks().forEach(chunkPos -> objHolder.getObjectsInChunk(chunkPos).forEach(
						(pos, wireNode) -> {
							NBTTagCompound data = new NBTTagCompound();
							data.setTag("objLoc", wireNode.getPosition().serializeNBT());
							data.setTag("objData", wireNode.serializeNBT());
							objects.appendTag(data);
						}
				));
				wbT.setTag("worldobjects", objects);
				newD.appendTag(wbT);
			}
		});
		ret.setTag("worldboundholders", newD);
		return ret;
	}

}
