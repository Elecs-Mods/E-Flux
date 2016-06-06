package elec332.eflux.api.circuit;

/**
 * Created by Elec332 on 4-5-2015.
 */
public enum EnumCircuit {

    SMALL((byte) 0), NORMAL((byte) 1), ADVANCED((byte) 2);

    private EnumCircuit(byte i){
        if (i < 0){
            throw new IllegalArgumentException();
        }
        this.i = i;
    }

    private final byte i;

    public byte getCircuitLevel(){
        return i;
    }

    public static EnumCircuit fromLevel(byte level){
        return VALUES[level];
    }

    public static final EnumCircuit[] VALUES = values();

    public static final String[] NAMES;

    static {
        NAMES = new String[VALUES.length];
        for (int i = 0; i < VALUES.length; i++) {
            NAMES[i] = VALUES[i].toString().toLowerCase();
        }
    }

}
