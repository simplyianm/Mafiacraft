/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crimsonrpg.mafiacraft.cmd;

import com.crimsonrpg.mafiacraft.Mafiacraft;
import com.crimsonrpg.mafiacraft.gov.GovType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author simplyianm
 */
public class Commands {
    public static void registerAll(Mafiacraft plugin) {
        plugin.getCommand("mafia").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                GovernmentCommand.parseCmd(cs, cmnd, string, strings, GovType.MAFIA);
                return true;
            }

        });
        plugin.getCommand("city").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                GovernmentCommand.parseCmd(cs, cmnd, string, strings, GovType.CITY);
                return true;
            }

        });
    }

}