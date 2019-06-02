package Scheduling;

//import org.jetbrains.annotations.Contract;

public class Constant {

    private static final double NBR = 1.0;
    private static final double NLR = 1.0;
    private static final double NBG = 1.0;
    private static final double NLG = 1.0;
    private static final double NLB = 1.0;
    private static final int BMAX = 50;
    private static final int BMIN = 0;
    private static final int CMAX = 20;
    private static final int DMAX = 30;
    private static final int BIN = 0;

    public static double getNLB() {
        return NLB;
    }

//    @Contract(pure = true)
    public static int getBMIN() {
        return BMIN;
    }

//    @Contract(pure = true)
    public static int getBIN() {
        return BIN;
    }

//    @Contract(pure = true)
    public static int getCMAX() {
        return CMAX;
    }

//    @Contract(pure = true)
    public static int getDMAX() {
        return DMAX;
    }

//    @Contract(pure = true)
    public static int getBMAX() {
        return BMAX;
    }

//    @Contract(pure = true)
    public static double getNLG() {
        return NLG;
    }

//    @Contract(pure = true)
    public static double getNBG() {
        return NBG;
    }

//    @Contract(pure = true)
    public static double getNLR() {
        return NLR;
    }

//    @Contract(pure = true)
    public static double getNBR() {
        return NBR;
    }
}
