package elec332.eflux.util;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Elec332 on 5-4-2015.
 */
//This is gonna go in ElecCore soon    -Elec332
public class Directions {
    public static int getNumberForDirection(ForgeDirection forgeDirection){
        switch (forgeDirection){
            case NORTH:
                return 0;
            case EAST:
                return 1;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
            default:
                return -1;
        }
    }

    public static ForgeDirection getDirectionFromNumber(int i){
        switch (i){
            case 0:
                return ForgeDirection.NORTH;
            case 1:
                return ForgeDirection.EAST;
            case 2:
                return ForgeDirection.SOUTH;
            case 3:
                return ForgeDirection.WEST;
            default:
                return ForgeDirection.UNKNOWN;
        }
    }
}
