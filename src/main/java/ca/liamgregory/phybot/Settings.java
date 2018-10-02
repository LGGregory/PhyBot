/*
 * Copyright 2018 Liam Gregory.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.liamgregory.phybot;

import java.util.HashMap;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map.Entry;

/**
 *
 * @author Liam Gregory
 */
public class Settings {

    public static final String SETTINGSFILE = System.getProperty("file.separator") + "settings.json";

    public static HashMap<String, GuildSettings> settingsByGuild = new HashMap<>();
    public static HashMap<String, String> guildAbbreviations = new HashMap<>();

    public static MessageChannel getConfessionChannelByGuild(Guild guild) {
        return guild.getTextChannelById(getSettingsByGuild(guild).getConfessionChannel());
    }

    public static void loadSettings() {
        for (Guild g : App.jda.getGuilds()) {
            File file = null;
            JsonReader reader;
            Gson gson = new Gson();

            //Look for previous settings
            file = new File(Ref.guildPath + g.getId() + SETTINGSFILE);
            if (file.exists()) {
                try {
                    // Load settings from file
                    reader = new JsonReader(new FileReader(file));
                    settingsByGuild.put(g.getId(), gson.fromJson(reader, GuildSettings.class));

                } catch (FileNotFoundException ex) {
                }

            } else {
                // Build new settings object
                settingsByGuild.put(g.getId(), new GuildSettings(g.getId(), true));
                System.out.println("Rebuilt settings for " + g.getId());
            }

            guildAbbreviations.put(settingsByGuild.get(g.getId()).getAbbreviation(), g.getId());

        }

    }

    public static void saveSettings() {

        for (Guild g : App.jda.getGuilds()) {
            try {
                Files.createDirectories(Paths.get(Ref.guildPath + g.getId()));
                try (Writer writer = new FileWriter(Ref.guildPath + g.getId() + SETTINGSFILE)) {
                    Gson gson = new GsonBuilder().create();
                    gson.toJson(settingsByGuild.get(g.getId()), writer);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static GuildSettings getSettingsByAbbreviation(String abbr) {
        return settingsByGuild.get(getGuildIdByAbbreviation(abbr));
    }

    public static GuildSettings getSettingsByGuildID(String guildID) {
        return settingsByGuild.get(guildID);
    }

    public static GuildSettings getSettingsByGuild(Guild guild) {
        return getSettingsByGuildID(guild.getId());
    }

    public static void buildAbbreviations() {
        for (Entry<String, GuildSettings> entry : settingsByGuild.entrySet()) {
            guildAbbreviations.put(entry.getKey(), entry.getValue().getAbbreviation());
        }
    }

    public static void addGuildIdAbbreviation(String abbr, String GUILD_ID) {
        settingsByGuild.get(GUILD_ID).setAbbreviation(abbr);
        guildAbbreviations.put(abbr, GUILD_ID);
    }

    public static String getGuildIdByAbbreviation(String abbr) {
        return guildAbbreviations.get(abbr);
    }
}
