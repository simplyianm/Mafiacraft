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

import net.voxton.mafiacraft.core.geo.Section;
import net.voxton.mafiacraft.core.player.MPlayer;

/**
 * Represents an entity that can own parcels of land. (chunks)
 */
public interface LandOwner {

    /**
     * Gets the type of owner this LandOwner is.
     *
     * @return
     */
    public OwnerType getOwnerType();

    /**
     * Gets the owner name.
     *
     * @return
     */
    public String getOwnerName();

    /**
     * Gets the string id of the owner of the chunk.
     *
     * @return
     */
    public String getOwnerId();

    /**
     * Gets the message that displays when you enter the person's land.
     *
     * @return
     */
    public String getEntryMessage();

    /**
     * Returns true if the given player can build in this section.
     *
     * @param player
     * @param section
     * @return
     */
    public boolean canBuild(MPlayer player, Section section);

    /**
     * Returns true if the given section can be claimed from this owner.
     *
     * @param section
     * @param futureOwner The entity that is trying to claim the land.
     * @return
     */
    public boolean canBeClaimed(Section section, LandOwner futureOwner);

}
