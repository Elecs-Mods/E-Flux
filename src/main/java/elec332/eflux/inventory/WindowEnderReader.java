package elec332.eflux.inventory;

import elec332.core.inventory.widget.WidgetButton;
import elec332.core.inventory.widget.WidgetButtonArrow;
import elec332.core.inventory.widget.slot.WidgetSlot;
import elec332.core.inventory.window.Window;
import elec332.core.util.BasicItemHandler;
import elec332.core.util.ItemStackHelper;
import elec332.eflux.api.EFluxAPI;
import elec332.eflux.api.ender.IEnderNetworkComponent;
import elec332.eflux.api.ender.internal.IEnderNetworkItem;
import elec332.eflux.endernetwork.EnderNetworkManager;
import elec332.eflux.tileentity.multiblock.TileEntityMultiBlockEnderReader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Created by Elec332 on 3-12-2016.
 */
public class WindowEnderReader extends Window implements WidgetButton.IButtonEventListener {

    public WindowEnderReader(TileEntityMultiBlockEnderReader tile) {
        super();
        valid = new int[0];
        this.tile = tile;
        inventory = new BasicItemHandler(1){

            @Override
            protected void onContentsChanged(int slot) {
                checkInitialBtn();
            }

            @Override
            public boolean isStackValidForSlot(int slot, @Nonnull ItemStack stack) {
                return !ItemStackHelper.isStackValid(stack) || (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null) || stack.getItem() instanceof IEnderNetworkItem);
            }
        };/*

        inventory = new BasicInventory("", 1){

            @Override
            public void setInventorySlotContents(int slotID, ItemStack stack) {
                super.setInventorySlotContents(slotID, stack);
                checkInitialBtn();
            }

            @Override
            public boolean isItemValidForSlot(int id, ItemStack stack) {
                return !ItemStackHelper.isStackValid(stack) || (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null) || stack.getItem() instanceof IEnderNetworkItem);
            }

        };*/
        maxID = EnderNetworkManager.get(tile.getWorld()).get(tile.getNetworkID()).getMaxID();
    }

    private final TileEntityMultiBlockEnderReader tile;

    private WidgetButtonArrow up, down;
    private WidgetButton set, clear;
    private BasicItemHandler inventory;

    private int freq;
    private final int maxID;
    private int[] valid;

    @Override
    protected void initWindow() {
        addPlayerInventoryToContainer();
        addWidget(new WidgetSlot(inventory, 0, 50, 32));
        up = addWidget(new WidgetButtonArrow(20, 15, WidgetButtonArrow.Direction.UP).addButtonEvent(this));
        down = addWidget(new WidgetButtonArrow(20, 55, WidgetButtonArrow.Direction.DOWN).addButtonEvent(this));
        set = addWidget(new WidgetButton(80, 24, 0, 0, 30, 15, this)).setDisplayString("set");
        clear = addWidget(new WidgetButton(80, 44, 0, 0, 30, 15, this)).setDisplayString("clear");

        checkInitialBtn();
    }

    private void checkInitialBtn(){
        if (!ItemStackHelper.isStackValid(inventory.getStackInSlot(0))){
            deactivateAll();
            valid = new int[0];
            freq = 0;
        } else {
            checkUpDown();
            boolean b = false;
            ItemStack stack = inventory.getStackInSlot(0);
            if (ItemStackHelper.isStackValid(stack)) {
                if (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                    IEnderNetworkComponent component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                    if (component != null) {
                        valid = EnderNetworkManager.get(tile.getWorld()).get(tile.getNetworkID()).getFrequencies(component.getRequiredCapability());
                        freq = Math.max(component.getFrequency(), 0);
                        b = true;
                        if (!clear.isActive()){
                            clear.setActive(true);
                        }
                        checkFreq();
                        checkUpDown();
                    }
                } else if (stack.getItem() instanceof IEnderNetworkItem){
                    b = true;
                    valid = new int[0];
                    freq = 0;
                    if (!clear.isActive()){
                        clear.setActive(true);
                    }
                    if (!set.isActive()){
                        set.setActive(true);
                    }
                    up.setActive(false);
                }
            }
            if (!b){
                deactivateAll();
            }
        }
    }

    private void deactivateAll(){
        if (up.isActive()){
            up.setActive(false);
        }
        if (down.isActive()){
            down.setActive(false);
        }
        if (set.isActive()){
            set.setActive(false);
        }
        if (clear.isActive()){
            clear.setActive(false);
        }
    }

    private void checkFreq(){
        if (!set.isActive()){
            set.setActive(true);
        }
        if (!isValidFreq(freq)&& set.isActive()){
            set.setActive(false);
        }
    }

    private void checkUpDown(){
        if (freq >= (maxID - 1)) {
            up.setActive(false);
        } else {
            up.setActive(true);
        }
        if (freq <= 0) {
            down.setActive(false);
        } else {
            down.setActive(true);
        }
    }

    @Override
    public void onButtonClicked(WidgetButton button) {
        if (button == up){
            freq++;
            if (freq >= (maxID - 1)){
                up.setActive(false);
                freq = maxID - 1;
            }
            if (!down.isActive()){
                down.setActive(true);
            }
            checkFreq();
        } else if (button == down){
            freq--;
            if (freq <= 0){
                down.setActive(false);
                freq = 0;
            }
            if (!up.isActive()){
                up.setActive(true);
            }
            checkFreq();
        } else if (button == set){
            ItemStack stack = inventory.getStackInSlot(0);
            if (ItemStackHelper.isStackValid(stack)){
                if (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                    IEnderNetworkComponent component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                    if (component != null) {
                        if (isValidFreq(freq)){
                            component.setUUID(tile.getNetworkID());
                            component.setFrequency(freq);
                        }
                    }
                } else if (stack.getItem() instanceof IEnderNetworkItem){
                    ((IEnderNetworkItem) stack.getItem()).setNetworkID(tile.getNetworkID(), stack);
                }
            }
        } else if (button == clear){
            ItemStack stack = inventory.getStackInSlot(0);
            if (ItemStackHelper.isStackValid(stack)){
                if (stack.hasCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null)) {
                    IEnderNetworkComponent component = stack.getCapability(EFluxAPI.ENDER_COMPONENT_CAPABILITY, null);
                    if (component != null) {
                        component.setUUID(null);
                        component.setFrequency(-1);
                    }
                    freq = 0;
                    checkUpDown();
                } else if (stack.getItem() instanceof IEnderNetworkItem){
                    ((IEnderNetworkItem) stack.getItem()).setNetworkID(tile.getNetworkID(), stack);
                }
            }
        }
    }

    private boolean isValidFreq(int freq){
        for (int i : valid){
            if (i == freq){
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        ItemStack ret = super.slotClick(slotId, dragType, clickTypeIn, player);
        detectAndSendChanges();
        return ret;
    }

    @Override
    public void onWindowClosed(EntityPlayer player) {
        if (!player.getEntityWorld().isRemote &&  ItemStackHelper.isStackValid(inventory.getStackInSlot(0))){
            player.entityDropItem(inventory.getStackInSlot(0), 1);
            inventory.clear();
        }
        super.onWindowClosed(player);
    }

}
