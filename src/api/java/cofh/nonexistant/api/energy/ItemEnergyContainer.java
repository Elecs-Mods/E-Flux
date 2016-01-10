package cofh.nonexistant.api.energy;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Reference implementation of {@link IEnergyContainerItem}. Use/extend this or implement your own.
 * 
 * @author King Lemming
 * 
 */
public class ItemEnergyContainer extends Item implements IEnergyContainerItem {

	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public ItemEnergyContainer() {

	}

	public ItemEnergyContainer(int capacity) {

		this(capacity, capacity, capacity);
	}

	public ItemEnergyContainer(int capacity, int maxTransfer) {

		this(capacity, maxTransfer, maxTransfer);
	}

	public ItemEnergyContainer(int capacity, int maxReceive, int maxExtract) {

		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public ItemEnergyContainer setCapacity(int capacity) {

		this.capacity = capacity;
		return this;
	}

	public void setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
	}

	public void setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
	}

	public void setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
	}

	/* IEnergyContainerItem
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			return 0;
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
			container.stackTagCompound.setInteger("Energy", energy);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(ItemStack container) {

		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("Energy")) {
			return 0;
		}
		return container.stackTagCompound.getInteger("Energy");
	}*/

	/**
	 * Adds energy to a container item. Returns the quantity of energy that was accepted. This should always return 0 if the item cannot be externally charged.
	 *
	 * @param container  ItemStack to be charged.
	 * @param maxReceive Maximum amount of energy to be sent into the item.
	 * @param simulate   If TRUE, the charge will only be simulated.
	 * @return Amount of energy that was (or would have been, if simulated) received by the item.
	 */
	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		return 0;
	}

	/**
	 * Removes energy from a container item. Returns the quantity of energy that was removed. This should always return 0 if the item cannot be externally
	 * discharged.
	 *
	 * @param container  ItemStack to be discharged.
	 * @param maxExtract Maximum amount of energy to be extracted from the item.
	 * @param simulate   If TRUE, the discharge will only be simulated.
	 * @return Amount of energy that was (or would have been, if simulated) extracted from the item.
	 */
	@Override
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		return 0;
	}

	/**
	 * Get the amount of energy currently stored in the container item.
	 *
	 * @param container
	 */
	@Override
	public int getEnergyStored(ItemStack container) {
		return 0;
	}

	@Override
	public int getMaxEnergyStored(ItemStack container) {

		return capacity;
	}

}
