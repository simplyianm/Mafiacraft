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
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and the Voxton license along with Mafiacraft. 
 * If not, see <http://voxton.net/voxton-license-v1.txt>.
 */
package net.voxton.mafiacraft.geo;

import net.voxton.mafiacraft.MLogger;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.gov.Division;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.GeoUtils;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.voxton.mafiacraft.util.LocationSerializer;
import net.voxton.mafiacraft.util.StringSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * Represents a 16x16 section area.
 */
@SerializableAs("district")
public class District implements LandOwner, ConfigurationSerializable {

    private transient final int id;

    private String name;

    private final World world;

    private final int x;

    private final int z;

    private Location busStop;

    private DistrictType type;

    private String description;

    private TByteObjectHashMap<String> owners = new TByteObjectHashMap<String>();

    private double landCost;

    public District(World world, int x, int z) {
        this.id = GeoUtils.coordsToDistrictId(x, z);
        this.world = world;
        this.x = x;
        this.z = z;
    }

    /**
     * Gets the id of the district.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Gets this district's unique id.
     *
     * @return The unique id of the district.
     */
    public String getUid() {
        return getWorld().getName() + ";" + getId();
    }

    /**
     * Gets the name of the district
     *
     * @return The name of the district
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the district
     *
     * @param name The name to set
     * @return The district that the name was changed of
     */
    public District setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets the world this district is in.
     *
     * @return
     */
    public World getWorld() {
        return world;
    }

    /**
     * {@inheritDoc}
     *
     * @param chunk
     * @return
     */
    public boolean canBeClaimed(Chunk chunk, LandOwner owner) {
        LandOwner currentOwner = getOwner(chunk);
        if (currentOwner.equals(this) && getType().isClaim()) {
            return true;
        }
        if (currentOwner instanceof Division) {
            Division div = (Division) currentOwner;
            if (div.getGovernment().getPower() < div.getGovernment().getLand()) {
                return true;
            }

        }
        return false;
    }

    /**
     * Gets the X coordinate of this district.
     *
     * @return
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Z coordinate of this district.
     *
     * @return
     */
    public int getZ() {
        return z;
    }

    /**
     * Gets the city this district is part of.
     *
     * @return
     */
    public City getCity() {
        return Mafiacraft.getCityManager().getCityOf(this);
    }

    /**
     * Sets the city of this district.
     *
     * @param city
     * @return
     */
    public District setCity(City city) {
        if (city != null) {
            Mafiacraft.getCityManager().attachDistrict(this, city);
        } else {
            Mafiacraft.getCityManager().detachDistrict(this);
        }
        return this;
    }

    /**
     * Gets the type of this district.
     *
     * @return
     */
    public DistrictType getType() {
        if (type == null) {
            return getCityWorld().getDefaultDistrictType();
        }
        return type;
    }

    /**
     * Sets the type of district this district is.
     *
     * @param type
     * @return
     */
    public District setType(DistrictType type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the description of the district.
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the district.
     *
     * @param description
     * @return
     */
    public District setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Gets the owner of a chunk.
     *
     * @param chunk
     * @return The government assigned to the chunk, or null if the chunk is not
     * part of the district.
     */
    public LandOwner getOwner(Chunk chunk) {
        if (!contains(chunk)) {
            throw new IllegalArgumentException("Chunk out of bounds of district "
                    + getName() + "!");
        }
        return getOwner(getDistrictOrigin(chunk.getX()),
                getDistrictOrigin(chunk.getZ()));
    }

    /**
     * Gets the owner of the specified section.
     *
     * @param x
     * @param z
     * @return
     */
    public LandOwner getOwner(int x, int z) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        String ownerStr = owners.get(id);
        LandOwner owner = Mafiacraft.getLandOwner(ownerStr);
        if (owner == null) {
            owner = this;
        }
        return owner;
    }

    /**
     * Sets the owner of a section.
     *
     * @param chunk
     * @param owner
     * @return
     */
    public District setOwner(Chunk chunk, LandOwner owner) {
        if (!contains(chunk)) {
            throw new IllegalArgumentException("Chunk out of bounds of district "
                    + getName() + "!");
        }
        return setOwner(chunk.getX() % 0x10, chunk.getZ() % 0x10, owner);
    }

    /**
     * Sets the owner of a section.
     *
     * @param x
     * @param z
     * @param owner
     * @return
     */
    public District setOwner(int x, int z, LandOwner owner) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        owners.put(id, owner.getOwnerId());
        return this;
    }

    /**
     * Gets the map of the owners of the district.
     *
     * @return The owners map.
     */
    private TByteObjectHashMap<String> getOwners() {
        return owners;
    }

    /**
     * Sets the owners of this district.
     *
     * @param owners The owners to set.
     * @return This District.
     */
    private District setOwners(TByteObjectHashMap<String> owners) {
        this.owners = owners;
        return this;
    }

    /**
     * Removes the owner of a section.
     *
     * @param chunk
     * @return
     */
    public District removeOwner(Chunk chunk) {
        if (!contains(chunk)) {
            throw new IllegalArgumentException("Chunk out of bounds of district "
                    + getName() + "!");
        }
        return removeOwner(chunk.getX() % 0x10, chunk.getZ() % 0x10);
    }

    /**
     * Removes an owner of a section.
     *
     * @param x
     * @param z
     * @return
     */
    public District removeOwner(int x, int z) {
        byte id = GeoUtils.coordsToSectionId(x, z);
        owners.remove(id);
        return this;
    }

    /**
     * Resets all ownerships in the district.
     *
     * @return
     */
    public District resetOwnerships() {
        owners = new TByteObjectHashMap<String>();
        return this;
    }

    /**
     * Gets the user-friendly name of the section.
     *
     * @param chunk
     * @return
     */
    public String getSectionName(Chunk chunk) {
        short idUnsigned = (short) (getSectionId(chunk) + 127);
        return getName() + '-' + idUnsigned;
    }

    /**
     * Gets the id of the specified section.
     *
     * @param chunk
     * @return
     */
    public byte getSectionId(Chunk chunk) {
        if (!contains(chunk)) {
            return -1;
        }
        return GeoUtils.coordsToSectionId(chunk.getX(), chunk.getZ());
    }

    /**
     * Checks if the district contains the specified location.
     *
     * @param location
     * @return
     */
    public boolean contains(Location location) {
        return contains(location.getChunk());
    }

    /**
     * Checks if the district contains the specified chunk.
     *
     * @param c
     * @return
     */
    public boolean contains(Chunk c) {
        int sx = x << 4;
        int sz = z << 4;
        MLogger.logVerbose("Checking if chunk " + c.getX() + ", " + c.getZ()
                + " is within the bounds of " + sx + ", " + sz + ".", 5);
        return (c.getX() >= sx)
                && (c.getX() < (sx + 0x10))
                && (c.getZ() >= sz)
                && (c.getZ() < (sz + 0x10));
    }

    public boolean canBuild(MPlayer player, Chunk chunk) {
        if (getType().canBuild()) {
            return true;
        }

        City c = getCity();
        if (c != null && c.isMember(player)) {
            return true;
        }

        return false;
    }

    public String getOwnerName() {
        String n = getName();
        if (n == null) {
            n = "??";
        }
        return ("District " + n).trim();
    }

    public String getOwnerId() {
        return "R" + id;
    }

    /**
     * Gets the bus stop of the district.
     *
     * @return
     */
    public Location getBusStop() {
        return busStop;
    }

    /**
     * Sets the bus stop of the district.
     *
     * @param busStop
     * @return This district
     */
    public District setBusStop(Location busStop) {
        this.busStop = busStop;
        return this;
    }

    /**
     * Gets a list of all players in the district.
     *
     * @return
     */
    public List<MPlayer> getPlayers() {
        List<MPlayer> players = new ArrayList<MPlayer>();
        for (MPlayer player : Mafiacraft.getOnlinePlayers()) {
            if (this.contains(player.getChunk())) {
                players.add(player);
            }
        }
        return players;
    }

    public OwnerType getOwnerType() {
        return OwnerType.DISTRICT;
    }

    /**
     * Detaches the district from whatever city it was part of. It completely
     * resets the district, too.
     *
     * @return This district
     */
    public District detachFromCity() {
        if (getCity() == null) {
            return this;
        }

        reset().setType(DistrictType.ANARCHIC);
        return this;
    }

    /**
     * Completely resets the district to how it was before, other than
     * buildings.
     *
     * @return
     */
    public District reset() {
        setCity(null);
        setName(null);
        setBusStop(null);
        resetOwnerships();
        setType(DistrictType.UNEXPLORED);
        return this;
    }

    /**
     * Gets a chat friendly name of the district.
     *
     * @return
     */
    public String getNameInChat() {
        if (name != null) {
            City c = getCity();
            return "district " + name + ((c == null) ? "" : " of " + c.
                    getOwnerName());
        }
        return "an unexplored district";
    }

    public double getLandCost() {
        return landCost;
    }

    public void setLandCost(double landCost) {
        this.landCost = landCost;
    }

    /**
     * Gets the CityWorld this district is in.
     *
     * @return
     */
    public CityWorld getCityWorld() {
        return Mafiacraft.getCityManager().getCityWorld(world);
    }

    public String getEntryMessage() {
        String desc = getDescription();
        String descAppend = "";
        if (desc != null) {
            descAppend = " - " + desc;
        }
        return MsgColor.INFO + getOwnerName() + descAppend;
    }

    /**
     * Gets the origin
     *
     * @param coord The coordinate to get the origin of.
     * @return
     */
    private static int getDistrictOrigin(int coord) {
        return (coord >= 0) ? (coord & ~0xf) : -((Math.abs(coord + 1) & ~0xf)
                + 16);
    }

    ////////////
    // SERIALIZATION
    ////////////
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();

        data.put("world", getWorld().getName());
        data.put("x", getX());
        data.put("z", getZ());

        data.put("name", getName());
        data.put("desc", getDescription());

        data.put("bus", LocationSerializer.serializeFull(getBusStop()));
        data.put("type", getType().name());
        data.put("landcost", getLandCost());

        String ownersStr = null;
        try {
            ownersStr = StringSerializer.toString(getOwners());
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE, "Owners could not be serialized!", ex);
        }

        data.put("owners", ownersStr);

        return data;
    }

    public static District deserialize(Map<String, Object> data) {
        String worldS = data.get("world").toString();
        World world = Bukkit.getWorld(worldS);
        if (world == null) {
            MLogger.log(Level.SEVERE, "Invalid world named '" + worldS
                    + "' encountered when deserializing a district!");
        }

        int x = 0;
        String xS = data.get("x").toString();
        try {
            x = Integer.parseInt(xS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid x value '" + xS
                    + "' encountered when deserializing a district!", ex);
        }

        int z = 0;
        String zS = data.get("z").toString();
        try {
            z = Integer.parseInt(zS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid z value '" + zS
                    + "' encountered when deserializing a district!", ex);
        }

        District district = new District(world, x, z);

        String name = data.get("name").toString();
        String desc = data.get("desc").toString();

        String ownerStr = data.get("owners").toString();

        TByteObjectHashMap<String> owners = null;
        try {
            owners = StringSerializer.fromString(ownerStr,
                    TByteObjectHashMap.class);
        } catch (IOException ex) {
            MLogger.log(Level.SEVERE, "IO Exception encountered when deserializing the owner string: '"
                    + ownerStr + "'!", ex);
        } catch (ClassNotFoundException ex) {
            MLogger.log(Level.SEVERE, "Class not found for the given serialized string: '"
                    + ownerStr + "'!", ex);
        }

        String typeStr = data.get("type").toString();
        DistrictType type = null;
        try {
            type = DistrictType.valueOf(typeStr);
        } catch (IllegalArgumentException ex) {
            MLogger.log(Level.SEVERE, "Illegal district type encountered in deserialization: '"
                    + typeStr + "'!", ex);
        }

        Map<String, Object> busS = (Map<String, Object>) data.get("bus");
        Location bus = LocationSerializer.deserializeFull(busS);

        String landCostS = data.get("landcost").toString();
        double landCost = 0.0d;

        try {
            landCost = Double.parseDouble(landCostS);
        } catch (NumberFormatException ex) {
            MLogger.log(Level.SEVERE, "Invalid land cost '" + landCostS
                    + "' encountered when deserializing a District!", ex);
        }

        //Info
        district.setName(name).setDescription(desc);
        district.setType(type).setOwners(owners);
        district.setBusStop(bus).setLandCost(landCost);

        return district;
    }

}
