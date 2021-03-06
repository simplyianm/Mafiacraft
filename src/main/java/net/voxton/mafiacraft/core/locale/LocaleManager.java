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
package net.voxton.mafiacraft.core.locale;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.voxton.mafiacraft.core.config.Config;
import net.voxton.mafiacraft.core.util.logging.MLogger;
import net.voxton.mafiacraft.core.Mafiacraft;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Manages all locales.
 *
 * TODO: initialize on creation
 */
public class LocaleManager {

    /**
     * Contains variables which are denoted as $(var) in the locale files.
     */
    private Map<String, String> vars = new HashMap<String, String>();

    /**
     * Map which contains all locales mapped to their names in lowercase form.
     */
    private Map<String, Locale> locales = new HashMap<String, Locale>();

    /**
     * Gets a Locale from its name.
     *
     * @param name The name of the locale.
     * @return The Locale object.
     */
    public Locale getLocale(String name) {
        name = name.toLowerCase();
        Locale locale = locales.get(name);
        if (locale == null) {
            //Load locale
            locale = new Locale(name);

            File localeFile = Mafiacraft.getSubFile("locale", name + ".yml");

            //Check if there is a locale there
            if (!localeFile.exists()) {
                InputStream localeStream = Mafiacraft.getImpl().getJarResource(
                        "locale/" + name + ".yml");

                //Check if there is a locale in the jar
                if (localeStream == null) {
                    //If not, we don't have a locale. Get English!
                    MLogger.log(Level.WARNING, "Unknown locale '" + name + "' "
                            + "could not be loaded. Defaulting it to English.");
                    return getLocale("en-us");
                }

                //If so, we do have a locale. Copy to the locale folder.
                if (!localeFile.exists()) {
                    try {
                        localeFile.getParentFile().mkdirs();
                        localeFile.createNewFile();
                    } catch (IOException ex) {
                        MLogger.log(Level.SEVERE, "Locale file not createable!",
                                ex);
                    }
                }

                OutputStream out = null;
                try {
                    out = new FileOutputStream(localeFile);
                } catch (FileNotFoundException ex) {
                }
                byte buf[] = new byte[1024];
                int len;

                try {
                    while ((len = localeStream.read(buf)) > 0) {
                        try {
                            out.write(buf, 0, len);
                        } catch (IOException ex) {
                            MLogger.log(Level.SEVERE,
                                    "Could not close locale stream!", ex);
                        }
                    }
                } catch (IOException ex) {
                    MLogger.log(Level.SEVERE, "Could not close read stream!", ex);
                }
                try {
                    out.close();
                } catch (IOException ex) {
                    MLogger.log(Level.SEVERE, "Could not close output stream!",
                            ex);
                }
                try {
                    localeStream.close();
                } catch (IOException ex) {
                    MLogger.log(Level.SEVERE, "Could not close locale stream!",
                            ex);
                }
            }

            YamlConfiguration localeYml = YamlConfiguration.loadConfiguration(
                    localeFile);

            //Add localizations
            for (String key : localeYml.getKeys(true)) {
                Object val = localeYml.get(key);
                if (val instanceof ConfigurationSection) {
                    continue;
                }
                locale.addLocalization(key, val.toString());
            }

            //Parse server-set variables
            for (Entry<String, String> entry : locale.getEntries()) {
                String current = entry.getValue();
                for (Entry<String, String> varEntry : vars.entrySet()) {
                    current.replaceAll("$(" + varEntry.getKey() + ")",
                            varEntry.getValue()); //Regex replacement
                }
                if (!current.equals(entry.getValue())) {
                    locale.addLocalization(entry.getKey(), current);
                }
            }

            //Store locale.
            locales.put(name, locale);
        }
        return locale;
    }

    /**
     * Gets a list of all locales.
     *
     * @return The list of locales.
     */
    public List<Locale> getLocales() {
        return new ArrayList<Locale>(locales.values());
    }

    /**
     * Gets the default locale.
     *
     * @return The locale.
     */
    public Locale getDefault() {
        return getLocale(Config.getString("locale.default"));
    }

    /**
     * Registers a variable into the system.
     *
     * @param name The name of the variable.
     * @param data The data of the variable.
     */
    public void registerVariable(String name, String data) {
        vars.put(name.toLowerCase(), data);
    }

}
