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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 * Testing of City.
 */
public class CityTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testClaimGrid() {
        System.out.println("Testing the claimGrid method.");

        City instance = new City();
        District district = mock(District.class);

        instance.claimGrid(district);

        verify(district).setOwner(0, 0, instance);
        verify(district).setOwner(0, 1, instance);
        verify(district).setOwner(0, 2, instance);
        verify(district).setOwner(0, 3, instance);
        verify(district).setOwner(0, 4, instance);
        verify(district).setOwner(0, 5, instance);
        verify(district).setOwner(0, 6, instance);
        verify(district).setOwner(0, 7, instance);
        verify(district).setOwner(0, 8, instance);
        verify(district).setOwner(0, 9, instance);
        verify(district).setOwner(0, 10, instance);
        verify(district).setOwner(0, 11, instance);
        verify(district).setOwner(0, 12, instance);
        verify(district).setOwner(0, 13, instance);
        verify(district).setOwner(0, 14, instance);
        verify(district).setOwner(0, 15, instance);

        verify(district).setOwner(1, 0, instance);
        verify(district).setOwner(1, 5, instance);
        verify(district).setOwner(1, 10, instance);
        verify(district).setOwner(1, 15, instance);

        verify(district).setOwner(2, 0, instance);
        verify(district).setOwner(2, 5, instance);
        verify(district).setOwner(2, 10, instance);
        verify(district).setOwner(2, 15, instance);

        verify(district).setOwner(3, 0, instance);
        verify(district).setOwner(3, 5, instance);
        verify(district).setOwner(3, 10, instance);
        verify(district).setOwner(3, 15, instance);

        verify(district).setOwner(4, 0, instance);
        verify(district).setOwner(4, 5, instance);
        verify(district).setOwner(4, 10, instance);
        verify(district).setOwner(4, 15, instance);

        verify(district).setOwner(5, 0, instance);
        verify(district).setOwner(5, 1, instance);
        verify(district).setOwner(5, 2, instance);
        verify(district).setOwner(5, 3, instance);
        verify(district).setOwner(5, 4, instance);
        verify(district).setOwner(5, 5, instance);
        verify(district).setOwner(5, 6, instance);
        verify(district).setOwner(5, 7, instance);
        verify(district).setOwner(5, 8, instance);
        verify(district).setOwner(5, 9, instance);
        verify(district).setOwner(5, 10, instance);
        verify(district).setOwner(5, 11, instance);
        verify(district).setOwner(5, 12, instance);
        verify(district).setOwner(5, 13, instance);
        verify(district).setOwner(5, 14, instance);
        verify(district).setOwner(5, 15, instance);

        verify(district).setOwner(6, 0, instance);
        verify(district).setOwner(6, 5, instance);
        verify(district).setOwner(6, 10, instance);
        verify(district).setOwner(6, 15, instance);

        verify(district).setOwner(7, 0, instance);
        verify(district).setOwner(7, 5, instance);
        verify(district).setOwner(7, 10, instance);
        verify(district).setOwner(7, 15, instance);

        verify(district).setOwner(8, 0, instance);
        verify(district).setOwner(8, 5, instance);
        verify(district).setOwner(8, 10, instance);
        verify(district).setOwner(8, 15, instance);

        verify(district).setOwner(9, 0, instance);
        verify(district).setOwner(9, 5, instance);
        verify(district).setOwner(9, 10, instance);
        verify(district).setOwner(9, 15, instance);

        verify(district).setOwner(10, 0, instance);
        verify(district).setOwner(10, 1, instance);
        verify(district).setOwner(10, 2, instance);
        verify(district).setOwner(10, 3, instance);
        verify(district).setOwner(10, 4, instance);
        verify(district).setOwner(10, 5, instance);
        verify(district).setOwner(10, 6, instance);
        verify(district).setOwner(10, 7, instance);
        verify(district).setOwner(10, 8, instance);
        verify(district).setOwner(10, 9, instance);
        verify(district).setOwner(10, 10, instance);
        verify(district).setOwner(10, 11, instance);
        verify(district).setOwner(10, 12, instance);
        verify(district).setOwner(10, 13, instance);
        verify(district).setOwner(10, 14, instance);
        verify(district).setOwner(10, 15, instance);

        verify(district).setOwner(11, 0, instance);
        verify(district).setOwner(11, 5, instance);
        verify(district).setOwner(11, 10, instance);
        verify(district).setOwner(11, 15, instance);

        verify(district).setOwner(12, 0, instance);
        verify(district).setOwner(12, 5, instance);
        verify(district).setOwner(12, 10, instance);
        verify(district).setOwner(12, 15, instance);

        verify(district).setOwner(13, 0, instance);
        verify(district).setOwner(13, 5, instance);
        verify(district).setOwner(13, 10, instance);
        verify(district).setOwner(13, 15, instance);

        verify(district).setOwner(14, 0, instance);
        verify(district).setOwner(14, 5, instance);
        verify(district).setOwner(14, 10, instance);
        verify(district).setOwner(14, 15, instance);

        verify(district).setOwner(15, 0, instance);
        verify(district).setOwner(15, 1, instance);
        verify(district).setOwner(15, 2, instance);
        verify(district).setOwner(15, 3, instance);
        verify(district).setOwner(15, 4, instance);
        verify(district).setOwner(15, 5, instance);
        verify(district).setOwner(15, 6, instance);
        verify(district).setOwner(15, 7, instance);
        verify(district).setOwner(15, 8, instance);
        verify(district).setOwner(15, 9, instance);
        verify(district).setOwner(15, 10, instance);
        verify(district).setOwner(15, 11, instance);
        verify(district).setOwner(15, 12, instance);
        verify(district).setOwner(15, 13, instance);
        verify(district).setOwner(15, 14, instance);
        verify(district).setOwner(15, 15, instance);
        
        verifyNoMoreInteractions(district);
    }
}
