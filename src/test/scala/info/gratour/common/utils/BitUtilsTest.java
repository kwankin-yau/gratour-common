package info.gratour.common.utils;

import junit.framework.TestCase;

/*******************************************************************************
 *  Copyright (c) 2019, 2021 lucendar.com.
 *  All rights reserved.
 *
 *  Contributors:
 *     KwanKin Yau (alphax@vip.163.com) - initial API and implementation
 *******************************************************************************/
public class BitUtilsTest extends TestCase {

    public void testShiftRight() {
        long v = 0b1110_1010;
        long v2 = BitUtils.shiftRight(v, 6, 2);
        assertEquals(v2, 0b1010);
        long v3 = BitUtils.shiftRight(v, 7, 3);
        assertEquals(v3, 0b1101);
        System.out.println("OK");
    }
}
