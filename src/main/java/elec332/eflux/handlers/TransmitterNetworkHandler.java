package elec332.eflux.handlers;
/*
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import elec332.eflux.util.TransmitterNetwork;

import java.util.IdentityHashMap;

/**
 * Created by Elec332 on 16-4-2015.
 *//*
public class TransmitterNetworkHandler {
    //public static final TransmitterNetworkHandler instance = new TransmitterNetworkHandler();
    private TransmitterNetworkHandler(){
        this.networks = new IdentityHashMap<TransmitterNetwork, Boolean>();
        FMLCommonHandler.instance().bus().register(this);
    }

    private IdentityHashMap<TransmitterNetwork, Boolean> networks;

    public void registerNetwork(TransmitterNetwork network){
        networks.put(network, true);
    }

    public void unregisterNetwork(TransmitterNetwork network){
        networks.remove(network);
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event){
        if (event.phase == TickEvent.Phase.END){
            for (TransmitterNetwork network : this.networks.keySet()){
                network.tickNetwork();
            }
        }
    }
}
*/