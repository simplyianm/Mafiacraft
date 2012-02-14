/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft;

import com.crimsonrpg.mafiacraft.chat.ChatHandler;
import com.crimsonrpg.mafiacraft.geo.CityManager;
import com.crimsonrpg.mafiacraft.gov.GovernmentManager;
import com.crimsonrpg.mafiacraft.player.MPlayer;
import com.crimsonrpg.mafiacraft.player.PlayerManager;
import com.crimsonrpg.mafiacraft.vault.VaultHelper;
import java.util.List;
import org.bukkit.entity.Player;

/**
 * Mafiacraft API accessor static class.
 */
public class Mafiacraft {
    private static MafiacraftPlugin plugin;
    
    public static void setPlugin(MafiacraftPlugin mcp) {
        if (Mafiacraft.plugin == null) {
            Mafiacraft.plugin = mcp;
        }
    }

    public static MafiacraftPlugin getPlugin() {
        return plugin;
    }
    
    /**
     * Gets an MPlayer from a Player.
     * 
     * @param player
     * @return 
     */
    public static MPlayer getPlayer(Player player) {
        return getPlugin().getPlayerManager().getPlayer(player);
    }

    /**
     * Returns a list of all MPlayers currently online the server.
     * 
     * @return 
     */
    public static List<MPlayer> getOnlinePlayers() {
        return getPlugin().getPlayerManager().getPlayerList();
    }

    /**
     * Gets the chat handler.
     * 
     * @return 
     */
    public static ChatHandler getChatHandler() {
        return getPlugin().getChatHandler();
    }

    /**
     * Gets the city manager.
     * 
     * @return 
     */
    public static CityManager getCityManager() {
        return getPlugin().getCityManager();
    }

    /**
     * Gets the government manager.
     * 
     * @return 
     */
    public static GovernmentManager getGovernmentManager() {
        return getPlugin().getGovernmentManager();
    }

    /**
     * Gets the player manager.
     * 
     * @return 
     */
    public static PlayerManager getPlayerManager() {
        return getPlugin().getPlayerManager();
    }

    /**
     * Gets the vault helper.
     * 
     * @return 
     */
    public static VaultHelper getVaultHelper() {
        return getPlugin().getVaultHelper();
    }
}
