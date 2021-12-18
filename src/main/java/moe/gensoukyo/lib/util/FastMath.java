package moe.gensoukyo.lib.util;

/**
 * @author ChloePrime
 */
public class FastMath {
    /**
     * Fast Inverse Square Root
     * @param x independent variable x
     * @return 1 / sqrt(x)
     */
    public static float invSqrt(float x) {
        float x2 = 0.5F * x;
        int i = Float.floatToRawIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        return x * (1.5F - x2 * x * x);
    }

    /**
     * Fast Inverse Square Root
     * @param x independent variable x
     * @return 1 / sqrt(x)
     */
    public static double invSqrt(double x) {
        double x2 = 0.5 * x;
        long i = Double.doubleToRawLongBits(x);
        i = 0x5fe6eb50c7b537aaL - (i >> 1);
        x = Double.longBitsToDouble(i);
        return x * (1.5 - x2 * x * x);
    }

    /**
     * Fast Inverse Square Root
     * @param x independent variable x
     * @return sqrt(x)
     */
    public static float sqrt(float x) {
        return 1 / invSqrt(x);
    }

    /**
     * Fast Inverse Square Root
     * @param x independent variable x
     * @return sqrt(x)
     */
    public static double sqrt(double x) {
        return 1 / invSqrt(x);
    }
}
