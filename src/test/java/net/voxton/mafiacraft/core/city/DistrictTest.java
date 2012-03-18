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

import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.gov.Government;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Testing of Districts.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class})
public class DistrictTest {

    private MWorld world;

    private LandOwner owner;

    @Before
    public void setUp() {
        mockStatic(Mafiacraft.class);
        
        //The mock land owner
        owner = mock(Government.class);
        when(Mafiacraft.getLandOwner(anyString())).thenReturn(owner);

        world = mock(MWorld.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetOwner() {
        System.out.println("Testing the setOwner method.");

        int x = 4;
        int z = 5;

        District district = new District(world, x, z);
        district.setOwner(x, z, owner);

        LandOwner expected = owner;
        LandOwner result = district.getOwner(x, z);

        assertEquals(expected, result);
    }

}
