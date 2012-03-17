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
package net.voxton.mafiacraft.core.task;

import com.kenai.crontabparser.CronTabExpression;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import net.voxton.mafiacraft.core.util.logging.MLogger;

/**
 * The task schedule. Let's hope it doesn't use up much ram.
 */
public class TaskSchedule {

    private final CronTabExpression expr;

    private TaskSchedule(CronTabExpression expr) {
        this.expr = expr;
    }

    /**
     * Checks if the given time fits the task schedule.
     *
     * @param calendar The time as a {@link java.util.Calendar}.
     * @return True if the time matches now.
     */
    public boolean fitsTime(Calendar calendar) {
        return expr.matches(calendar);
    }

    /**
     * Gets a schedule from its crontab string.
     *
     * A representation of a crontab string, as used for scheduling with the
     * *nix job scheduler {@link Cron http://en.wikipedia.org/wiki/Cron}.
     * <br/><br/>
     *
     * <h3> Summary </h3>
     *
     * A {@code CronTabExpression} may represent only one crontab string. Each
     * crontab string consists of five fields, as specified below*.
     *
     * <pre>
     * .---------------- minute (0 - 59)
     * |  .------------- hour (0 - 23)
     * |  |  .---------- day of month (1 - 31)
     * |  |  |  .------- month (1 - 12) OR jan,feb,mar,apr ...
     * |  |  |  |  .---- day of week (0 - 7) (Sunday=0 or 7)  OR sun,mon,tue,wed,thu,fri,sat
     * |  |  |  |  |
     * * * * * *</pre> (* Figure originally from <a
     * href="http://en.wikipedia.org/wiki/Cron"> Wikipedia</a>. Used under <a
     * href="http://en.wikipedia.org/wiki/Wikipedia:Text_of_Creative_Commons_Attribution-ShareAlike_3.0_Unported_License">
     * Creative Commons Attribution-ShareAlike License</a> and available here
     * under same license.)
     *
     *
     * <h3> Standard Entries </h3>
     *
     * Fields may contain one of the following.
     *
     * <ul> <li> An asterisk (*), which matches any value. </li> <li> A range of
     * numbers. Ranges are represented by two numbers separated by a hyphen.
     * Ranges are inclusive.<br/> For example, 8-11 for an "hours" entry matches
     * hours 8, 9, 10 and 11. </li> <li> A list of numbers. A list is a set of
     * numbers (or ranges) separated by commas.<br/> Examples: "1,2,5,9",
     * "0-4,8-12". </li> <li> A range with a step value. A step entry is a range
     * (or an asterisk) followed by "/&lt;step number>". This type of entry
     * matches any value in the range that is exactly divisible by the step
     * number. <br/> For example, "0-23/2" can be used in the hours field to
     * specify command execution every other hour (equivalent to the list entry
     * "0,2,4,6,8,10,12,14,16,18,20,22").<br/> Using an asterisk for the range
     * will match every valid value for the field that is exactly visible by the
     * step number.<br/> For example "*\/3" in the hours field will match every
     * third hour. </li> <li> <b>month and day-of-week fields only:</b> A range
     * or list value, with month and day names (respectively) substituted for
     * numeric values. Names are case insensitive, and may be three-letter
     * abbreviations or full names. <br/> Examples: "mon", "apr", "Friday",
     * "AUGUST", "mon-fri", "jan, feb, dec" </li> </ul> <b>Note:</b> The day can
     * be specified in either of two fields: day of month, and day of week. If
     * both fields are restricted (i.e., aren't *), the expression will match
     * any time at which either field is matched.<br/> For example, "30 4 1,15 *
     * 5" would match 4:30 am on the 1st and 15th of each month, as well as
     * every Friday.<br/><br/>
     *
     *
     * <h3>Special Entries</h3>
     *
     * Instead of the first five fields, one of eight special strings may be
     * used: **<br/>
     *
     * <table> <tbody> <tr> <th> Entry </th> <th> Description </th> <th>
     * Equivalent To </th> </tr>
     *
     * <tr> <td>
     * <code>@yearly</code> </td> <td> Run once a year </td> <td>
     * <code>0 0 1 1 *</code> </td> </tr>
     *
     * <tr> <td>
     * <code>@annually</code> </td> <td> (same as
     * <code>@yearly</code>) </td> <td>
     * <code>0 0 1 1 *</code> </td> </tr>
     *
     * <tr> <td>
     * <code>@monthly</code> </td> <td> Run once a month </td> <td>
     * <code>0 0 1 * *</code> </td> </tr> <tr> <td>
     * <code>@weekly</code> </td> <td> Run once a week </td> <td>
     * <code>0 0 * * 0</code> </td> </tr>
     *
     * <tr> <td>
     * <code>@daily</code> </td> <td> Run once a day </td> <td>
     * <code>0 0 * * *</code> </td> </tr>
     *
     * <tr> <td>
     * <code>@midnight</code> </td> <td> (same as
     * <code>@daily</code>) </td> <td>
     * <code>0 0 * * *</code> </td> </tr>
     *
     * <tr> <td>
     * <code>@hourly</code> </td> <td> Run once an hour </td> <td>
     * <code>0 * * * *</code> </td> </tr> </tbody> </table><br/> (** Table
     * originally from <a href="http://en.wikipedia.org/wiki/Cron">
     * Wikipedia</a>. Used under <a
     * href="http://en.wikipedia.org/wiki/Wikipedia:Text_of_Creative_Commons_Attribution-ShareAlike_3.0_Unported_License">
     * Creative Commons Attribution-ShareAlike License</a> and available here
     * under same license.)
     *
     * @param schedule The string schedule as specified above.
     * @return The TaskSchedule parsed from this.
     */
    public static TaskSchedule fromCronString(String schedule) {
        try {
            CronTabExpression expr = CronTabExpression.parse(schedule);
            return new TaskSchedule(expr);
        } catch (ParseException ex) {
            MLogger.log(Level.WARNING, "Invalid schedule encountered: "
                    + schedule, ex);
            return null;
        }
    }

}
