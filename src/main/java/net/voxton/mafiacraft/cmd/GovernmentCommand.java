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
package net.voxton.mafiacraft.cmd;

import net.voxton.mafiacraft.config.Config;
import net.voxton.mafiacraft.Mafiacraft;
import net.voxton.mafiacraft.geo.District;
import net.voxton.mafiacraft.gov.Division;
import net.voxton.mafiacraft.gov.GovType;
import net.voxton.mafiacraft.gov.Government;
import net.voxton.mafiacraft.gov.Position;
import net.voxton.mafiacraft.player.MPlayer;
import net.voxton.mafiacraft.player.MsgColor;
import net.voxton.mafiacraft.util.ValidationUtils;
import com.google.common.base.Joiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.voxton.mafiacraft.geo.MPoint;
import net.voxton.mafiacraft.geo.Section;
import net.voxton.mafiacraft.help.MenuType;
import net.voxton.mafiacraft.util.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Holds government-related commands.
 */
public final class GovernmentCommand {

    public static void parseCmd(CommandSender sender, Command cmd, String label,
            String[] args, GovType type) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MsgColor.ERROR + Mafiacraft.getDefaultLocale().
                    localize("command.general.ingame-only"));
            return;
        }

        MPlayer player = Mafiacraft.getPlayer(((Player) sender).getName());

        if (args.length < 1) {
            doHelp(player, type);
            return;
        }

        //Get the function we want to do.
        String function = args[0];
        List<String> largs = new ArrayList<String>(Arrays.asList(args));
        largs.remove(0);

        String result = null;

        if (largs.size() < 1) {
            if (function.equalsIgnoreCase("who")) {
                result = doWho(player);
            } else if (function.equalsIgnoreCase("claim")) {
                result = doClaim(player);
            } else if (function.equalsIgnoreCase("hq")) {
                result = doHq(player);
            } else if (function.equalsIgnoreCase("sethq")) {
                result = doSetHq(player);
            } else if (function.equalsIgnoreCase("leave")) {
                result = doLeave(player, type);
            } else if (function.equalsIgnoreCase("player")) {
                result = doPlayer(player);
            } else if (function.equalsIgnoreCase("accept")) {
                result = doAccept(player, type);
            } else {
                result = doHelp(player, function, type);
            }
        } else if (largs.size() < 2) {
            if (function.equalsIgnoreCase("who")) {
                result = doWho(player, largs.get(0));
            } else if (function.equalsIgnoreCase("invite")) {
                result = doInvite(player, largs.get(0));
            } else if (function.equalsIgnoreCase("createdivision")) {
                result = doCreateDivision(player, largs.get(0));
            } else if (function.equalsIgnoreCase("kick")) {
                result = doKick(player, largs.get(0));
            } else if (function.equalsIgnoreCase("player")) {
                result = doPlayer(player, largs.get(0));
            } else if (function.equalsIgnoreCase("found")) {
                result = doFound(player, largs.get(0), type);
            } else if (function.equalsIgnoreCase("promoteofficer")) {
                result = doPromoteOfficer(player, largs.get(0));
            } else if (function.equalsIgnoreCase("demoteofficer")) {
                result = doDemoteOfficer(player, largs.get(0));
            } else {
                result = doHelp(player, function, type);
            }
        } else if (largs.size() < 3) {
            if (function.equalsIgnoreCase("grant")) {
                result = doGrant(player, largs.get(0), largs.get(1));
            } else if (function.equalsIgnoreCase("setmanager")
                    || function.equalsIgnoreCase("setcapo")
                    || function.equalsIgnoreCase("setsergeant")) {
                result = doSetManager(player, largs.get(0), largs.get(1));
            } else {
                result = doHelp(player, function, type);
            }
        } else {
            if (function.equalsIgnoreCase("found")) {
                result = doFound(player, Joiner.on(' ').join(largs), type);
            } else {
                result = doHelp(player, function, type);
            }
        }

        if (result != null) {
            player.sendMessage(MsgColor.ERROR + result);
        }
    }

    public static String doHelp(MPlayer player, GovType type) {
        if (type.equals(GovType.MAFIA)) {
            MenuType.MAFIA.doHelp(player);
        } else if (type.equals(GovType.POLICE)) {
            MenuType.POLICE.doHelp(player);
        }
        return null;
    }

    public static String doHelp(MPlayer player, String arg, GovType type) {
        if (type.equals(GovType.MAFIA)) {
            MenuType.MAFIA.doHelp(player, arg);
        } else if (type.equals(GovType.POLICE)) {
            MenuType.POLICE.doHelp(player, arg);
        }
        return null;
    }

    public static String doAccept(MPlayer player, GovType type) {
        Integer inv = player.getSessionStore().getInt("gov-inv", -1);
        if (inv < 0) {
            return player.getLocale().localize(
                    "command.government.error.not-invited", type.getName());
        }

        Government gov = Mafiacraft.getGovernmentManager().getGovernment(inv);
        if (gov == null) {
            return player.getLocale().localize("command.government.error.invited-nonexistent"
                    + type.getName());
        }

        gov.addAffiliate(player);

        player.sendMessage(MsgColor.SUCCESS + player.getLocale().localize(
                "command.government.success.joined",
                type.getName(), gov.getName()));
        gov.broadcastMessage(player.getLocale().localize(
                "event.government.player-joined", player.getName(),
                gov.getType().getName()));
        return null;
    }

    /**
     * Found command.
     * 
     * @param player The player.
     * @param name The name to found as.
     * @param type The type of government.
     * @return The first error.
     */
    public static String doFound(MPlayer player, String name, GovType type) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return player.getLocale().localize("command.general.not-citizen");
        }

        if (!type.canFound()) {
            return player.getLocale().localize(
                    "command.government.error.found", type.getName());
        }

        double balance = player.getMoney();
        double cost = Config.getDouble("mafia.found");

        if (balance < cost) {
            return player.getLocale().localize(
                    "command.government.error.no-money.found", StringUtils.
                    formatCurrency(cost));
        }

        if (player.getGovernment() != null) {
            return player.getLocale().localize("command.government.error.in-gov");
        }

        name = name.trim();
        boolean result = ValidationUtils.validateName(name);
        if (!result) {
            return player.getLocale().localize(
                    "command.government.error.invalid-name", name);
        }

        if (Mafiacraft.getGovernmentManager().getGovernment(name) != null) {
            return player.getLocale().localize("command.government.error.exists");
        }

        //Found the government
        Government founded = Mafiacraft.getGovernmentManager().createGovernment(
                name, type);
        if (!founded.addAffiliate(player)) {
            return player.getLocale().localize("error.fatal.adding");
        }

        founded.setLeader(player);

        double startupCapital = Config.getDouble("mafia.startupcapital");
        founded.addMoney(startupCapital);

        player.sendMessage(MsgColor.SUCCESS
                + player.getLocale().localize(
                "command.government.success.founded", type.getName(), name));
        return null;
    }

    public static String doPlayer(MPlayer player) {
        return doPlayer(player, player.getName());
    }

    public static String doPlayer(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getPlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        player.sendMessage(MsgColor.INFO + "TODO: finish info for " + tgt.
                getDisplayName());
        player.sendMessage(MsgColor.INFO + "Position: " + tgt.getPosition());
        //TODO: add stats
        return null;
    }

    public static String doWho(MPlayer player) {
        return doWho(player, player.getGovernment().getName());
    }

    public static String doWho(MPlayer player, String govName) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov =
                Mafiacraft.getGovernmentManager().getGovernment(govName);
        if (gov == null) {
            return "The government you specified does not exist.";
        }

        player.sendMessage(MsgColor.INFO + "=== Info for " + gov.getName()
                + " ===");
        //TODO: add stats
        return null;
    }

    public static String doHq(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();

        if (gov == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            return "Your position in your " + gov.getType().getName()
                    + " is not high enough to use HQ teleport.";
        }

        MPoint hq = gov.getHq();
        if (hq == null) {
            return "Your " + gov.getType().getName()
                    + " does not have a HQ set.";
        }

        //Teleport.
        player.teleportWithCountdown(hq);
        return null;
    }

    public static String doInvite(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getPlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            System.out.println(player.getPosition());
            return "Your position in your " + gov.getType().getName()
                    + " is not high enough to invite people to your government.";
        }

        Government other = tgt.getGovernment();
        if (other != null) {
            if (other.equals(gov)) {
                return "The other player you specified is already in your "
                        + gov.getType().getName() + ".";
            }
            return "The player you specified is already affiliated with a government.";
        }

        gov.dispatchInvite(player, tgt);
        player.sendMessage(MsgColor.INFO
                + "An invite to the mafia has been dispatched to "
                + tgt.getName() + ".");
        return null;
    }

    public static String doKick(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        MPlayer tgt = Mafiacraft.getPlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not part of a government.";
        }

        if (!player.getPosition().isAtLeast(Position.WORKER)) {
            return "Your position in your " + gov.getType().getName()
                    + " is not high enough to kick people from your government.";
        }

        Government other = tgt.getGovernment();
        if (other == null) {
            return "That player is not in a " + gov.getType().getName() + ".";
        }

        if (!other.equals(gov)) {
            return "That player is not in your " + gov.getType().getName() + ".";
        }

        if (!tgt.getPosition().equals(Position.AFFILIATE)) {
            return "Only " + gov.getType().getLocale("affiliates")
                    + " may be kicked from a " + gov.getType().getName() + ".";
        }

        gov.removeMember(tgt);
        tgt.resetPower();
        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName()
                + " has been kicked out of the " + gov.getType().getName() + ".");
        return null;
    }

    public static String doGrant(MPlayer player, String division, String amt) {
        double amount;
        try {
            amount = Double.parseDouble(amt);
        } catch (NumberFormatException ex) {
            return "The amount you specified is an invalid number.";
        }

        if (!player.getPosition().isAtLeast(Position.OFFICER)) {
            return "You must be an officer or higher to do this.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        Division div = gov.getDivisionByName(division);
        if (div == null) {
            return "That division does not exist within your " + gov.getType().
                    getName() + ".";
        }

        if (!gov.transferWithCheck(div, amount)) {
            return "Your " + gov.getType().getName()
                    + " doesn't have enough money to perform this transaction.";
        }

        player.sendMessage(MsgColor.SUCCESS + amount + " " + Config.getString(
                "currency.namepl")
                + " have been granted to the " + gov.getType().getLocale(
                "division") + " " + div.getName() + ".");
        return null;
    }

    public static String doCreateDivision(MPlayer player, String name) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government!";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You do not have the proper rank to do this.";
        }

        name = name.trim();
        if (!ValidationUtils.validateName(name)) {
            return "invalid name";
        }

        if (!gov.canHaveMoreDivisions()) {
            return "The " + gov.getType().getName() + " has too many " + gov.
                    getType().getLocale("divisions")
                    + ". Make sure all of your " + gov.getType().getLocale(
                    "divisions") + " have greater than 5 players each.";
        }

        Division div = gov.createDivision().setManager(player.getName()).setName(
                name);

        gov.subtractMoney(Config.getDouble("prices.mafia.regimefound"));
        div.addMoney(Config.getDouble("mafia.regimestartup"));

        player.sendMessage(MsgColor.SUCCESS + "You have founded a " + gov.
                getType().getLocale("division") + " successfully.");
        return null;
    }

    public static String doSetManager(MPlayer player, String division,
            String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government!";
        }

        MPlayer manager = Mafiacraft.getOnlinePlayer(target);
        if (manager == null) {
            return "That player is either not online or doesn't exist.";
        }

        Division div = gov.getDivisionByName(division);
        if (div == null) {
            return "A " + gov.getType().getLocale("division")
                    + " with the name '" + division
                    + "' does not exist in your " + gov.getType().getName()
                    + ".";
        }

        div.setManager(manager);
        player.sendMessage(MsgColor.SUCCESS + "The capo for the regime ");
        return null;
    }

    public static String doClaim(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You aren't allowed to claim land for your government.";
        }

        Section section = player.getSection();
        District district = player.getDistrict();

        if (!district.canBeClaimed(section, gov)) {
            return "Your government isn't allowed to claim the given district.";
        }

        double price = district.getLandCost();
        if (!gov.hasEnough(price)) {
            return "Your government does not have enough money to purchase this land.";
        }

        gov.claim(section);
        gov.subtractMoney(price);

        player.sendMessage(MsgColor.SUCCESS
                + "You have successfully claimed the section " + district.
                getSectionName(section) + " for your " + gov.getType().getName()
                + ".");
        return null;
    }

    public static String doSetHq(MPlayer player) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You aren't allowed to set the HQ of your government.";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a government.";
        }

        Section section = player.getSection();
        if (!Mafiacraft.getSectionOwner(section).equals(gov)) {
            return "The HQ must be specified within HQ land.";
        }

        double needed = Config.getDouble("prices.gov.sethq");
        if (!gov.hasEnough(needed)) {
            return "Your " + gov.getType().getName()
                    + " does not have enough money to set its HQ. (Costs "
                    + needed + ")";
        }

        gov.setHq(player.getPoint()).subtractMoney(needed);
        player.sendMessage(MsgColor.SUCCESS + "Your " + gov.getType().getName()
                + " HQ has been set to your current location.");
        return null;
    }

    public static String doLeave(MPlayer player, GovType type) {
        Government gov = player.getGovernment();
        if (gov == null) {
            return "You aren't in a " + type.getName();
        }

        gov.removeMemberAndSucceed(player);

        player.sendMessage(MsgColor.SUCCESS + "You have left " + gov.getName()
                + ".");
        gov.broadcastMessage(MsgColor.INFO + player.getName() + " has left the "
                + gov.getType().getName() + ".");
        return null;
    }

    public static String doPromoteOfficer(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You do not have the proper rank to do this.";
        }

        if (!gov.canHaveMore(Position.OFFICER)) {
            return "Your government cannot have any more officers.";
        }

        MPlayer tgt = Mafiacraft.getOnlinePlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        if (!gov.isMember(tgt)) {
            return "You cannot promote a member that is not part of the " + gov.
                    getType().getName() + ".";
        }

        if (tgt.getPosition().isAtLeast(Position.OFFICER)) {
            return "That member is already either equal to or higher than an officer in rank.";
        }

        gov.removeMemberAndSucceed(tgt).addOfficer(tgt);

        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName()
                + " has been promoted to officer within your government.");
        return null;
    }

    public static String doDemoteOfficer(MPlayer player, String target) {
        if (!player.hasPermission("mafiacraft.citizen")) {
            return "You must be a citizen to use this command. "
                    + "Apply for citizen on the website at " + MsgColor.URL
                    + "http://voxton.net/" + ".";
        }

        Government gov = player.getGovernment();
        if (gov == null) {
            return "You are not in a government.";
        }

        if (!player.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "You do not have the proper rank to do this.";
        }

        MPlayer tgt = Mafiacraft.getOnlinePlayer(target);
        if (tgt == null) {
            return "The player you specified is either not online or does not exist.";
        }

        if (!gov.isMember(tgt)) {
            return "You cannot demote a member that is not part of the " + gov.
                    getType().getName() + ".";
        }

        if (tgt.getPosition().isAtLeast(Position.VICE_LEADER)) {
            return "That member is higher than an officer in rank.";
        }

        gov.removeMemberAndSucceed(tgt).addAffiliate(tgt);

        player.sendMessage(MsgColor.SUCCESS + "The player " + tgt.getName()
                + " has been demoted from officer within your government.");
        return null;
    }

}
