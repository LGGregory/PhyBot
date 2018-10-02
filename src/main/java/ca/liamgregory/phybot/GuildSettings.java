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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import ca.liamgregory.phybot.menu.Menu;

/**
 *
 * @author Liam Gregory
 */
public class GuildSettings {

    /**
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @param abbreviation the abbreviation to set
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    private final String GUILD_ID;

    /**
     * @return the confessionCount
     */
    public long getConfessionCount() {
        return confessionCount;
    }

    /**
     * Increase confessionCount by 1.
     */
    public void increaseConfessionCount() {
        this.confessionCount++;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix the prefix to set
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the listenChannel
     */
    public String getListenChannel() {
        return listenChannel;
    }

    /**
     * @param listenChannel the listenChannel to set
     */
    public void setListenChannel(String listenChannel) {
        this.listenChannel = listenChannel;
    }

    /**
     * @return the confessionChannel
     */
    public String getConfessionChannel() {
        return confessionChannel;
    }

    /**
     * @param confessionChannel the confessionChannel to set
     */
    public void setConfessionChannel(String confessionChannel) {
        this.confessionChannel = confessionChannel;
    }

    /**
     * @return the pinSymbol
     */
    public String getPinSymbol() {
        return pinSymbol;
    }

    /**
     * @param pinSymbol the pinSymbol to set
     */
    public void setPinSymbol(String pinSymbol) {
        this.pinSymbol = pinSymbol;
    }

    /**
     * @return the pinCount
     */
    public int getPinCount() {
        return pinCount;
    }

    /**
     * @param pinCount the pinCount to set
     */
    public void setPinCount(int pinCount) {
        this.pinCount = pinCount;
    }

    /**
     * @return the professors
     */
    public ArrayList<String> getProfessors() {
        return professors;
    }

    /**
     * @return the rules
     */
    public HashMap<String, String> getRules() {
        return rules;
    }

    /**
     * @param rules the rules to set
     */
    public void setRules(HashMap<String, String> rules) {
        this.rules = rules;
    }

    /**
     * 
     */
    public HashMap<String, Menu> menus = new HashMap<>();
    
    private String prefix = ">>";
    private String abbreviation = null;

    private String listenChannel;

    private long confessionCount = 0;
    private String confessionChannel;

    private String pinSymbol;
    private int pinCount;

    private ArrayList<String> professors;
    private HashMap<String, String> rules = new HashMap<>();

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this);
        return json;
    }

    public GuildSettings fromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, GuildSettings.class);

    }

    public GuildSettings(String id) {
        this.GUILD_ID = id;

    }

    public GuildSettings(String id, boolean rebuild) {
        this(id);
        if (rebuild) {
            pinCount = Ref.guildPinCount.getOrDefault(GUILD_ID, Ref.guildDefaultPinRequired);
            pinSymbol = Ref.guildPinSymbol.get(GUILD_ID);
            rules = Ref.guildRuleReplaceList.getOrDefault(GUILD_ID, new HashMap<>());
            professors = new ArrayList<>();

        }
    }

}
