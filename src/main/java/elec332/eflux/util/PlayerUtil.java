package elec332.eflux.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import java.util.UUID;

/**
 * Created by Elec332 on 25-5-2015.
 */
public class PlayerUtil {

    public static UUID getPlayerUUID(EntityPlayer player){
        return player.getGameProfile().getId();
    }

    public static void sendMessageToPlayer(EntityPlayer player, String s){
        try {
            player.addChatComponentMessage(new ChatComponentText(s));
        } catch (NullPointerException e){
            //Null player, whoops
        }
    }

}
