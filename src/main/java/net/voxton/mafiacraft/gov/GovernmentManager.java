/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.voxton.mafiacraft.gov;

import net.voxton.mafiacraft.MafiacraftPlugin;
import net.voxton.mafiacraft.geo.City;
import net.voxton.mafiacraft.player.MPlayer;
import gnu.trove.map.TIntIntMap;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Manages government-related activities.
 */
public class GovernmentManager {

    /**
     * Holds all governments mapped to their ids.
     */
    private TIntObjectMap<Government> governments = new TIntObjectHashMap<Government>();

    /**
     * Holds all divisions mapped to their ids.
     */
    private TIntObjectMap<Division> divisions = new TIntObjectHashMap<Division>();

    /**
     * Holds all police mappings to their respective cities.
     */
    private TIntIntMap policeMap = new TIntIntHashMap();

    /**
     * Maps governments to divisions. One to many.
     */
    private Map<Government, List<Division>> govDivMap = new HashMap<Government, List<Division>>();

    /**
     * Maps divisions to governments. Many to one.
     */
    private Map<Division, Government> divGovMap = new HashMap<Division, Government>();

    /**
     * Hook to the main plugin.
     */
    private MafiacraftPlugin mc;

    /**
     * Constructor.
     * 
     * @param mc The MafiacraftPlugin object.
     */
    public GovernmentManager(MafiacraftPlugin mc) {
        this.mc = mc;

        ConfigurationSerialization.registerClass(Government.class);
        ConfigurationSerialization.registerClass(Division.class);
    }

    /**
     * Gets a list of all governments.
     * 
     * @return A list of all governments.
     */
    public List<Government> getGovernments() {
        return new ArrayList<Government>(governments.valueCollection());
    }

    /**
     * Gets the government associated with the id.
     *
     * @param id The id of the government.
     * @return The Government associated with the id.
     */
    public Government getGovernment(int id) {
        return governments.get(id);
    }

    /**
     * Gets a government by its name. Case insensitive.
     *
     * @param name
     * @return
     */
    public Government getGovernment(String name) {
        for (Government government : getGovernments()) {
            if (government.getName().equalsIgnoreCase(name)) {
                return government;
            }
        }
        return null;
    }

    /**
     * Gets the government of the specified city.
     *
     * @param city
     * @return
     */
    public Government getPolice(City city) {
        return getGovernment(policeMap.get(city.getId()));
    }

    /**
     * Creates a new government.
     *
     * @param name The name of the government.
     * @return
     */
    public Government createGovernment(String name, GovType type) {
        int id = getNextGovernmentId();
        Government government = new Government(id);
        governments.put(id, government);
        government.setName(name).setType(type);
        return government;
    }

    /**
     * Get the government of a player.
     *
     * @param player
     * @return
     */
    public Government getGovernment(MPlayer player) {
        for (Government gov : getGovernments()) {
            if (gov.isMember(player)) {
                return gov;
            }
        }
        return null;
    }

    /**
     * Sets the city government of a city.
     *
     * @param city
     * @param government
     */
    public void setPolice(City city, Government government) {
        government.setType(GovType.POLICE);
        policeMap.put(city.getId(), government.getId());
    }

    /**
     * Gets a list of divisions of a certain government.
     *
     * @param gov The government.
     * @return A list of divisions.
     */
    public List<Division> getDivisions(Government gov) {
        return new ArrayList<Division>(getActualDivisionList(gov));
    }

    /**
     * Gets the next available and unused government id.
     *
     * @return The government id.
     */
    private int getNextGovernmentId() {
        int id = 0;
        for (int i = 1; getGovernment(i) != null; ++i) {
            id = i;
        }
        return id;
    }

    /**
     * Gets THE list of divisions pertaining to the given government. INTERNAL
     * USE ONLY!!
     *
     * @param gov The government.
     * @return The list of divisions.
     */
    private List<Division> getActualDivisionList(Government gov) {
        List<Division> divs = govDivMap.get(gov);
        if (divs == null) {
            divs = new ArrayList<Division>();
            govDivMap.put(gov, divs);
        }
        return divs;
    }

    ///////////
    // DIVISION METHODS
    ///////////
    /**
     * Gets a division.
     *
     * @param id
     * @return
     */
    public Division getDivision(int id) {
        return divisions.get(id);
    }

    /**
     * Gets a list of all divisions on the server.
     *
     * @return
     */
    public List<Division> getDivisions() {
        return new ArrayList<Division>(divisions.valueCollection());
    }

    /**
     * Creates a division. (No validation)
     *
     * @param gov
     * @return
     */
    public Division createDivision(Government gov) {
        int id = getNextDivisionId();
        Division div = new Division(id);
        insertDivision(div).bindDivision(div, gov);
        return div;
    }

    /**
     * Inserts a division into the maps.
     *
     * @param div The division to insert.
     * @return This manager.
     */
    public GovernmentManager insertDivision(Division div) {
        divisions.put(div.getId(), div);
        return this;
    }

    /**
     * Binds a division to a government.
     * 
     * @param div The division to make a binding for.
     * @param gov The government to bind with.
     * @return This GovernmentManager.
     */
    public GovernmentManager bindDivision(Division div, Government gov) {
        getActualDivisionList(gov).add(div);
        divGovMap.put(div, gov);
        return this;
    }

    /**
     * Removes all references to the given division from the Government manager.
     *
     * @param div
     * @return
     */
    public boolean removeDivision(Division div) {
        Government gov = div.getGovernment();
        if (gov == null) {
            return false;
        }

        divisions.remove(div.getId());
        getActualDivisionList(gov).remove(div);
        divGovMap.remove(div);

        return true;
    }

    /**
     * Gets the government of the specified division.
     *
     * @param div The government of the division.
     * @return The division.
     */
    public Government getGovernmentOf(Division div) {
        return divGovMap.get(div);
    }

    /**
     * Gets the next available ID for a division.
     *
     * @return The next available District id.
     */
    private int getNextDivisionId() {
        int id = 0;
        for (int i = 1; getDivision(i) != null; ++i) {
            id = i;
        }
        return id;
    }

    //////////////
    // LOADING
    //////////////
    /**
     * Loads everything in this government manager.
     * 
     * @return This government manager.
     */
    public GovernmentManager load() {
        return loadGovernments().loadDivisions().loadPoliceMappings().
                loadDivGovMappings();
    }

    public GovernmentManager loadGovernments() {
        return this;
    }

    public GovernmentManager loadDivisions() {
        return this;
    }

    public GovernmentManager loadPoliceMappings() {
        return this;
    }

    public GovernmentManager loadDivGovMappings() {
        return this;
    }
    //////////////
    // SAVING
    //////////////

    /**
     * Saves everything in this government manager.
     * 
     * @return This government manager.
     */
    public GovernmentManager save() {
        return saveGovernments().saveDivisions().savePoliceMappings().
                saveDivGovMappings();
    }

    public GovernmentManager saveGovernments() {
        return this;
    }

    public GovernmentManager saveDivisions() {
        return this;
    }

    public GovernmentManager savePoliceMappings() {
        return this;
    }

    public GovernmentManager saveDivGovMappings() {
        return this;
    }

}
