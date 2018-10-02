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
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Liam Gregory
 */
public class Confession {

    public static HashMap<Long, Confession> confessions = new HashMap<>();

    public final String guildId;
    public final String messageContent;
    private boolean posted = false;
    public HashMap<String, String> confessorMessages = new HashMap<>();

    public boolean isPosted() {
        return posted;
    }

    public void post(String posterID) { //maybe move posting logic here?
        posted = true;
        
        
        
        if (confessorMessages != null && confessorMessages.size() > 1) {
            confessorMessages.entrySet()
                    .stream()
                    .filter((entry)
                            -> (!entry.getKey()
                            .equals(posterID)))
                    .forEachOrdered((entry) -> {
                        App.jda.getUserById(entry.getKey())
                                .openPrivateChannel()
                                .queue((channel) -> {
                                    channel
                                            .getMessageById(entry.getValue())
                                            .queue(message -> {
                                                message.delete().queue();
                                            });

                                });
                    });
        }
    }

    public Confession(String guildId, String messageContent) {
        this.guildId = guildId;
        this.messageContent = messageContent;

    }

    public static void saveConfessions() {

        try (Writer writer = new FileWriter(Ref.dataPath + "confessions.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(confessions, writer);
        } catch (IOException ex) {

        }
    }

    public static void loadConfessions() {
        try {

            JsonReader reader = new JsonReader(new FileReader(Ref.dataPath + "confessions.json"));
            Type confessionMapType = new TypeToken<HashMap<Long, Confession>>() {
            }.getType();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            confessions = gson.fromJson(reader, confessionMapType);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public String toJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        String json = gson.toJson(this);
        return json;
    }

    public Confession fromJson(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.fromJson(json, Confession.class);
    }

}
