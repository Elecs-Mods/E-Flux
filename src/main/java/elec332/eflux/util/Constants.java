package elec332.eflux.util;

/**
 * Created by Elec332 on 5-11-2017.
 */
public class Constants {

	public static final int AC_LINE_FREQUENCY = 60;

	public static final int MAX_AC_RMS_VOLTAGE = 765 * 1000;
	public static final int MAX_DC_GROUND_VOLTAGE = 800 * 1000;
	public static final int MAX_DC_LINE_LINE_VOLTAGE = MAX_DC_GROUND_VOLTAGE * 2;

	public static final float AC_POWER_FACTOR = 0.97f; //YES, this is a thing, look it up (value is an average though)...
	public static final double DIELECTRIC_CONSTANT_VACUUM =  8.854 * Math.pow(10, -12);
	private static final double SQRT_2 = Math.sqrt(2);

	public static final int MC_TICKS_SECOND = 20;

}
