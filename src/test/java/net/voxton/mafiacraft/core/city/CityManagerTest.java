/*
 * This file is part of Mafiacraft.
 * 
 * Mafiacraft is released under the Voxton License version 1.
 *
 * Mafiacraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition to this, you must also specify that this product includes 
 * software developed by Voxton.net and may not remove any code
 * referencing Voxton.net directly or indirectly.
 * 
 * Mafiacraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.core.city;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Testing of the city manager.
 */
public class CityManagerTest {

    @Test
    public void testGetSectionKeyAndReverse() {
        System.out.println("Testing the getSectionKey method and its inverse.");

        int x = -3;
        int y = 66;
        int z = 15;

        long key = CityManager.getSectionKey(x, y, z);

        int xr = CityManager.getXFromKey(key);
        int yr = CityManager.getYFromKey(key);
        int zr = CityManager.getZFromKey(key);

        assertEquals(x, xr);
        assertEquals(y, yr);
        assertEquals(z, zr);
    }

    @Test
    public void testGetSectionKeyAndReverse_2() {
        System.out.println(
                "Testing the getSectionKey method and its inverse, test 2.");

        int x = -1;
        int y = 66;
        int z = 15;

        long key = CityManager.getSectionKey(x, y, z);

        int xr = CityManager.getXFromKey(key);
        int yr = CityManager.getYFromKey(key);
        int zr = CityManager.getZFromKey(key);

        assertEquals(x, xr);
        assertEquals(y, yr);
        assertEquals(z, zr);
    }

    @Test
    public void testGetSectionKey() {
        System.out.println("Testing the getSectionKey method.");

        int x = -67;
        int y = 118;
        int z = 18;

        long expected = 0x3ffef6000ed00012L;
        long result = CityManager.getSectionKey(x, y, z);

        assertEquals(expected, result);
    }

    @Test
    public void testGetXFromKey() {
        System.out.println("Testing the getXFromKey method.");
        long key = 0x3ffef6000ed00012L;

        int expected = -67;
        long result = CityManager.getXFromKey(key);

        assertEquals(expected, result);
    }

    @Test
    public void testGetYFromKey() {
        System.out.println("Testing the getYFromKey method.");
        long key = 0x3ffef6000ed00012L;

        int expected = 118;
        long result = CityManager.getYFromKey(key);

        assertEquals(expected, result);
    }

    @Test
    public void testGetZFromKey() {
        System.out.println("Testing the getZFromKey method.");
        long key = 0x3ffef6000ed00012L;

        int expected = 18;
        long result = CityManager.getZFromKey(key);

        assertEquals(expected, result);
    }

}
