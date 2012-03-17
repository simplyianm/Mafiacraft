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
package net.voxton.mafiacraft.core.geo;

import net.voxton.mafiacraft.core.city.District;
import net.voxton.mafiacraft.core.city.LandOwner;
import org.junit.*;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Section unit tests.
 */
public class SectionTest {
    
    public SectionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getX method, of class Section.
     */
    @Test
    public void testGetX() {
        System.out.println("Testing the getX method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 1;
        int result = instance.getX();
        assertEquals(expected, result);
    }

    /**
     * Test of getY method, of class Section.
     */
    @Test
    public void testGetY() {
        System.out.println("Testing the getY method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 2;
        int result = instance.getY();
        assertEquals(expected, result);
    }

    /**
     * Test of getZ method, of class Section.
     */
    @Test
    public void testGetZ() {
        System.out.println("Testing the getZ method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 3;
        int result = instance.getZ();
        assertEquals(expected, result);
    }

    /**
     * Test of getDistrictX method, of class Section.
     */
    @Test
    public void testGetDistrictX() {
        System.out.println("Testing the getDistrictX method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 0;
        int result = instance.getDistrictX();
        assertEquals(expected, result);
    }

    /**
     * Test of getDistrictY method, of class Section.
     */
    @Test
    public void testGetDistrictY() {
        System.out.println("Testing the getDistrictY method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 2;
        int result = instance.getDistrictY();
        assertEquals(expected, result);
    }

    /**
     * Test of getDistrictZ method, of class Section.
     */
    @Test
    public void testGetDistrictZ() {
        System.out.println("Testing the getDistrictZ method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 0;
        int result = instance.getDistrictZ();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginX method, of class Section.
     */
    @Test
    public void testGetOriginX() {
        System.out.println("Testing the getOriginX method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 1;
        int result = instance.getOriginX();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginX method, of class Section.
     */
    @Test
    public void testGetOriginX_zero() {
        System.out.println("Testing the getOriginX method with a zero.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 0, 0, 0);
        
        int expected = 0;
        int result = instance.getOriginX();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginX method, of class Section.
     */
    @Test
    public void testGetOriginX_neg() {
        System.out.println("Testing the getOriginX method, negative.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, -1, -2, -3);
        
        int expected = 15;
        int result = instance.getOriginX();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginY method, of class Section.
     */
    @Test
    public void testGetOriginY() {
        System.out.println("Testing the getOriginY method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 0;
        int result = instance.getOriginY();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginY method, of class Section.
     */
    @Test
    public void testGetOriginY_neg() {
        System.out.println("Testing the getOriginY method, negative.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, -1, -2, -3);
        
        int expected = 0;
        int result = instance.getOriginY();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginZ method, of class Section.
     */
    @Test
    public void testGetOriginZ() {
        System.out.println("Testing the getOriginZ method.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, 1, 2, 3);
        
        int expected = 3;
        int result = instance.getOriginZ();
        assertEquals(expected, result);
    }

    /**
     * Test of getOriginZ method, of class Section.
     */
    @Test
    public void testGetOriginZ_neg() {
        System.out.println("Testing the getOriginZ method, negative.");
        
        MWorld world = mock(MWorld.class);
        Section instance = new Section(world, -1, -2, -3);
        
        int expected = 13;
        int result = instance.getOriginZ();
        assertEquals(expected, result);
    }
    
}
