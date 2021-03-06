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
package net.voxton.mafiacraft.core.action.action;

import net.voxton.mafiacraft.core.action.ActionType;
import net.voxton.mafiacraft.core.geo.Section;
import net.voxton.mafiacraft.core.econ.Transactable;
import net.voxton.mafiacraft.core.city.District;
import net.voxton.mafiacraft.core.city.CityManager;
import net.voxton.mafiacraft.core.city.City;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import net.voxton.mafiacraft.core.geo.MWorld;
import net.voxton.mafiacraft.core.player.MPlayer;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import net.voxton.mafiacraft.bukkit.BukkitImpl;
import net.voxton.mafiacraft.core.config.Config;
import net.voxton.mafiacraft.core.Mafiacraft;
import net.voxton.mafiacraft.core.MafiacraftCore;
import net.voxton.mafiacraft.core.locale.Locale;
import net.voxton.mafiacraft.core.locale.LocaleManager;
import net.voxton.mafiacraft.core.chat.MsgColor;
import org.junit.After;
import org.junit.Before;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.mockito.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Testing of the city action.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Mafiacraft.class, Config.class})
public class CityActionsTest {

    private MWorld world;

    private MWorld metroWorld;

    private City metroCity;

    private CityManager cityManager;

    private MPlayer aubhaze;

    private MPlayer albireox;

    @Before
    public void setUp() {
        //Locale setup
        mockStatic(Mafiacraft.class);
        when(Mafiacraft.getSubFile("locale", "en-us.yml")).thenReturn(new File(
                "./target/plugins/Mafiacraft/locale/en-us.yml"));
        LocaleManager manager = new LocaleManager();
        BukkitImpl impl = mock(BukkitImpl.class);
        InputStream stream =
                MafiacraftCore.class.getResourceAsStream("/locale/en-us.yml");
        when(Mafiacraft.getImpl()).thenReturn(impl);
        when(impl.getJarResource("locale/en-us.yml")).thenReturn(stream);
        mockStatic(Config.class);
        when(Config.getString("locale.default")).thenReturn("en-us");
        Locale locale = manager.getDefault();
        when(Mafiacraft.getDefaultLocale()).thenReturn(locale);
        when(Mafiacraft.getLocaleManager()).thenReturn(manager);
        when(Mafiacraft.getLocales()).thenReturn(manager.getLocales());
        //Locale setup end.

        //Mock the city manager
        cityManager = mock(CityManager.class);

        //Mock the cityworld
        world = mock(MWorld.class);

        //Mock the real world
        metroCity = mock(City.class);
        metroWorld = mock(MWorld.class);
        when(metroWorld.getCapital()).thenReturn(metroCity);

        //Aubhaze has no permissions.
        aubhaze = mock(MPlayer.class);
        when(aubhaze.hasPermission(anyString())).thenReturn(false);
        when(aubhaze.getLocale()).thenReturn(locale);
        when(aubhaze.getWorld()).thenReturn(world);

        //AlbireoX has all permissions.
        albireox = mock(MPlayer.class);
        when(albireox.hasPermission(anyString())).thenReturn(true);
        when(albireox.getLocale()).thenReturn(locale);
        when(albireox.getWorld()).thenReturn(world);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDoFound_notCitizen() {
        System.out.println("Testing found subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doFound(aubhaze, "test");

        assertEquals(expected, result);
    }

    @Test
    public void testDoFound_capitalEstablished() {
        System.out.println("Testing found subcommand of a player in a world "
                + "with a capital already established.");

        when(albireox.getWorld()).thenReturn(metroWorld);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.capital-established");
        String result = ActionType.CITY.doFound(albireox, "test");

        assertEquals(expected, result);
    }

    @Test
    public void testDoFound_noMoney() {
        System.out.println("Testing found subcommand of a player with not "
                + "enough money to found a city.");

        when(albireox.getMoney()).thenReturn(5000.0);
        when(Config.getDouble("city.foundcost")).thenReturn(10000000.0); //$10,000,000.00

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.no-money.found", "$10,000,000.00");
        String result = ActionType.CITY.doFound(albireox, "5000");

        assertEquals(expected, result);
    }

    @Test
    public void testDoFound_invalidName() {
        System.out.println("Testing found subcommand with a bad name.");

        when(albireox.getMoney()).thenReturn(50000000000.0);
        when(Config.getDouble("city.foundcost")).thenReturn(10000000.0); //$10,000,000.00

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.invalid-name");
        String result = ActionType.CITY.doFound(albireox, "The Name Of No Gods");

        assertEquals(expected, result);
    }

    @Test
    public void testDoFound_nameTaken() {
        System.out.println(
                "Testing found subcommand with an already taken name.");

        when(albireox.getMoney()).thenReturn(50000000000.0);
        when(Config.getDouble("city.foundcost")).thenReturn(10000000.0); //$10,000,000.00
        when(Config.getInt("strings.maxnamelength")).thenReturn(15);

        when(cityManager.cityExists("TakenName")).thenReturn(
                Boolean.TRUE);

        when(Mafiacraft.getCityManager()).thenReturn(cityManager);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.name-taken");
        String result = ActionType.CITY.doFound(albireox, "TakenName");

        assertEquals(expected, result);
    }

    @Test
    public void testDoFound_valid() {
        System.out.println(
                "Testing found subcommand with valid circumstances.");

        when(albireox.getMoney()).thenReturn(50000000000.0);
        when(Config.getDouble("city.foundcost")).thenReturn(10000000.0); //$10,000,000.00
        when(Config.getInt("strings.maxnamelength")).thenReturn(15);

        when(cityManager.cityExists("AValidName")).thenReturn(
                Boolean.FALSE);

        when(Mafiacraft.getCityManager()).thenReturn(cityManager);

        //Specific to this
        Section section = mock(Section.class);
        when(albireox.getSection()).thenReturn(section);

        District district = mock(District.class);
        when(section.getDistrict()).thenReturn(district);
        //End

        String expected = null;
        String result = ActionType.CITY.doFound(albireox, "AValidName");

        assertEquals(expected, result);

        //Message sent?
        verify(albireox).sendMessage(MsgColor.SUCCESS + Mafiacraft.
                getDefaultLocale().localize("action.city.founded"));

        //City founded?
        verify(cityManager).foundCity(albireox, "AValidName", district);
        verify(albireox).transferMoney(any(Transactable.class), eq(10000000.00));
    }

    @Test
    public void testDoSetSpawn_notCitizen() {
        System.out.println(
                "Testing setspawn subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doSetSpawn(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoSetSpawn_notInCity() {
        System.out.println(
                "Testing setspawn subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doSetSpawn(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoSpawn_notInCity() {
        System.out.println(
                "Testing spawn subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doSpawn(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoAnnex_notCitizen() {
        System.out.println("Testing annex subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doAnnex(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoUnannex_notCitizen() {
        System.out.println("Testing unannex subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doUnannex(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoUnannex_notInCity() {
        System.out.println(
                "Testing unannex subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doUnannex(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoRename_notCitizen() {
        System.out.println("Testing rename subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doRename(aubhaze, "test");

        assertEquals(expected, result);
    }

    @Test
    public void testDoRename_notInCity() {
        System.out.println(
                "Testing rename subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doRename(aubhaze, "test");

        assertEquals(expected, result);
    }

    @Test
    public void testDoFunds_notCitizen() {
        System.out.println("Testing funds subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doFunds(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoFunds_notInCity() {
        System.out.println(
                "Testing funds subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doFunds(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoDisband_notCitizen() {
        System.out.println("Testing disband subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doDisband(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoDisband_notInCity() {
        System.out.println(
                "Testing disband subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doDisband(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoBus_notCitizen() {
        System.out.println("Testing bus subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doBus(aubhaze, "there");

        assertEquals(expected, result);
    }

    @Test
    public void testDoBus_notInCity() {
        System.out.println(
                "Testing bus subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doBus(aubhaze, "asdf");

        assertEquals(expected, result);
    }

    @Test
    public void testDoDeposit_notCitizen() {
        System.out.println("Testing deposit subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doDeposit(aubhaze, "test");

        assertEquals(expected, result);
    }

    @Test
    public void testDoDeposit_notInCity() {
        System.out.println(
                "Testing deposit subcommand of a player not in a city.");

        when(aubhaze.getMoney()).thenReturn(300.00);
        when(aubhaze.hasEnough(anyDouble())).thenReturn(true);
        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doDeposit(aubhaze, "10");

        assertEquals(expected, result);
    }

    @Test
    public void testDoWithdraw_notCitizen() {
        System.out.println(
                "Testing withdraw subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doWithdraw(aubhaze, "test");

        assertEquals(expected, result);
    }

    @Test
    public void testDoWithdraw_notInCity() {
        System.out.println(
                "Testing withdraw subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doWithdraw(aubhaze, "20");

        assertEquals(expected, result);
    }

    @Test
    public void testDoClaim_notCitizen() {
        System.out.println("Testing claim subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doClaim(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoClaim_notInCity() {
        System.out.println(
                "Testing claim subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doClaim(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoUnclaim_notCitizen() {
        System.out.println("Testing unclaim subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doUnclaim(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoUnclaim_notInCity() {
        System.out.println(
                "Testing unclaim subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doUnclaim(aubhaze);

        assertEquals(expected, result);
    }

    @Test
    public void testDoMakePolice_notCitizen() {
        System.out.println(
                "Testing make police subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doMakePolice(aubhaze, "asdf", "derp");

        assertEquals(expected, result);
    }

    @Test
    public void testDoMakePolice_notInCity() {
        System.out.println(
                "Testing make police subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doMakePolice(aubhaze, "bob", "marley");

        assertEquals(expected, result);
    }

    @Test
    public void testDoSetChief_notCitizen() {
        System.out.println(
                "Testing set chief subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doSetChief(aubhaze, "asdf");

        assertEquals(expected, result);
    }

    @Test
    public void testDoSetChief_notInCity() {
        System.out.println(
                "Testing set chief subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doSetChief(aubhaze, "person");

        assertEquals(expected, result);
    }

    @Test
    public void testDoSetAssistant_notCitizen() {
        System.out.println(
                "Testing set assistant subcommand of a player not citizen.");

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.general.not-citizen");
        String result = ActionType.CITY.doSetAssistant(aubhaze, "asdf");

        assertEquals(expected, result);
    }

    @Test
    public void testDoSetAssistant_notInCity() {
        System.out.println(
                "Testing set assistant subcommand of a player not in a city.");

        when(aubhaze.hasPermission("mafiacraft.citizen")).thenReturn(
                Boolean.TRUE);

        String expected = Mafiacraft.getDefaultLocale().localize(
                "action.city.not-in");
        String result = ActionType.CITY.doSetAssistant(aubhaze, "Asdf");

        assertEquals(expected, result);
    }

}
