/*******************************************************************************
 *  Copyright (c) 2017 Gratour.info
 *  All rights reserved.
 *******************************************************************************/
package info.gratour.common.utils;

/**
 *
 */
public class BitUtils {

    /**
     * 检测给定的值的某位是否置位。
     *
     * @param value    要检测的值。
     * @param bitIndex 位索引号，0-63。
     * @return 是否置位
     */
    public static boolean test(int value, int bitIndex) {
        return (value & (1 << bitIndex)) != 0;
    }

    /**
     * 检测给定的值的某位是否置位。
     *
     * @param value    要检测的值。
     * @param bitIndex 位索引号，0-63。
     * @return 是否置位
     */
    public static boolean test(long value, int bitIndex) {
        return (value & (1L << bitIndex)) != 0;
    }

    /**
     * 设置给定的值的某一位，返回新的值。32位版。
     *
     * @param value    给定的值。
     * @param bitIndex 位索引号，0-31。
     * @return 被置位后的新值。
     */
    public static int set(int value, int bitIndex) {
        int value2 = 1 << bitIndex;
        return value | value2;
    }

    /**
     * 设置给定的值的某一位，返回新的值。64位版。
     *
     * @param value    给定的值。
     * @param bitIndex 位索引号，0-63。
     * @return 被置位后的新值。
     */
    public static long set(long value, int bitIndex) {
        long value2 = 1L << bitIndex;
        return value | value2;
    }

    /**
     * 清除给定的值的某一位，返回新的值。32位版。
     *
     * @param value    给定的值。
     * @param bitIndex 位索引号，0-31。
     * @return 被清除位后的新值。
     */
    public static int clear(int value, int bitIndex) {
        int value2 = 1 << bitIndex;
        return value &= ~value2;
    }

    /**
     * 清除给定的值的某一位，返回新的值。64位版。
     *
     * @param value    给定的值。
     * @param bitIndex 位索引号，0-63。
     * @return 被清除位后的新值。
     */
    public static long clear(long value, int bitIndex) {
        long value2 = 1L << bitIndex;
        return value &= ~value2;
    }

    /**
     * 反转给定值的某一位（即如果该位为0则置为1，为1则置为0），返回新的值。
     * <p>
     * 如给定值0x03，反转0位，则返回0x02；而给定值0x02，反转0位则返回0x03。
     * <p>
     * 32位版。
     *
     * @param value    给定的值。
     * @param bitIndex 位索引号，0-31。
     * @return 被反转某一位后的新值。
     */
    public static int toggle(int value, int bitIndex) {
        return value ^ (1 << bitIndex);
    }

    /**
     * Byte swap a single short value.
     *
     * @param value  Value to byte swap.
     * @return       Byte swapped representation.
     */
    public static short swap (short value)
    {
        int b1 = value & 0xff;
        int b2 = (value >> 8) & 0xff;

        return (short) (b1 << 8 | b2);
    }



    /**
     * Byte swap a single int value.
     *
     * @param value  Value to byte swap.
     * @return       Byte swapped representation.
     */
    public static int swap (int value)
    {
        int b1 = (value) & 0xff;
        int b2 = (value >>  8) & 0xff;
        int b3 = (value >> 16) & 0xff;
        int b4 = (value >> 24) & 0xff;

        return b1 << 24 | b2 << 16 | b3 << 8 | b4;
    }



    /**
     * Byte swap a single long value.
     *
     * @param value  Value to byte swap.
     * @return       Byte swapped representation.
     */
    public static long swap (long value)
    {
        long b1 = (value) & 0xff;
        long b2 = (value >>  8) & 0xff;
        long b3 = (value >> 16) & 0xff;
        long b4 = (value >> 24) & 0xff;
        long b5 = (value >> 32) & 0xff;
        long b6 = (value >> 40) & 0xff;
        long b7 = (value >> 48) & 0xff;
        long b8 = (value >> 56) & 0xff;

        return b1 << 56 | b2 << 48 | b3 << 40 | b4 << 32 |
                b5 << 24 | b6 << 16 | b7 <<  8 | b8;
    }



    /**
     * Byte swap a single float value.
     *
     * @param value  Value to byte swap.
     * @return       Byte swapped representation.
     */
    public static float swap (float value)
    {
        int intValue = Float.floatToIntBits (value);
        intValue = swap (intValue);
        return Float.intBitsToFloat (intValue);
    }



    /**
     * Byte swap a single double value.
     *
     * @param value  Value to byte swap.
     * @return       Byte swapped representation.
     */
    public static double swap (double value)
    {
        long longValue = Double.doubleToLongBits (value);
        longValue = swap (longValue);
        return Double.longBitsToDouble (longValue);
    }



    /**
     * Byte swap an array of shorts. The result of the swapping
     * is put back into the specified array.
     *
     * @param array  Array of values to swap
     */
    public static void swap (short[] array)
    {
        for (int i = 0; i < array.length; i++)
            array[i] = swap (array[i]);
    }



    /**
     * Byte swap an array of ints. The result of the swapping
     * is put back into the specified array.
     *
     * @param array  Array of values to swap
     */
    public static void swap (int[] array)
    {
        for (int i = 0; i < array.length; i++)
            array[i] = swap (array[i]);
    }



    /**
     * Byte swap an array of longs. The result of the swapping
     * is put back into the specified array.
     *
     * @param array  Array of values to swap
     */
    public static void swap (long[] array)
    {
        for (int i = 0; i < array.length; i++)
            array[i] = swap (array[i]);
    }



    /**
     * Byte swap an array of floats. The result of the swapping
     * is put back into the specified array.
     *
     * @param array  Array of values to swap
     */
    public static void swap (float[] array)
    {
        for (int i = 0; i < array.length; i++)
            array[i] = swap (array[i]);
    }



    /**
     * Byte swap an array of doubles. The result of the swapping
     * is put back into the specified array.
     *
     * @param array  Array of values to swap
     */
    public static void swap (double[] array)
    {
        for (int i = 0; i < array.length; i++)
            array[i] = swap (array[i]);
    }

}
