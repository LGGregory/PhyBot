/*
 * Copyright 2018 Samwise210.
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
package ca.liamgregory.phybot.setup;

import ca.liamgregory.phybot.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Samwise210
 */
public class SetupListener extends ListenerAdapter {

    private static final HashMap<String, MessageEmbed> EMBEDS = new HashMap<>();
    private static final HashMap<String, ArrayList<Field>> FIELDS = new HashMap<>();
    private static final ArrayList<String> EMOJI = new ArrayList<>();

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        initEmbeds();
        Message msg = evt.getMessage();
        if (!(evt.getAuthor().isBot()) && msg.getContentRaw().equalsIgnoreCase(Ref.prefix + "setup")) {
            evt.getChannel().sendMessage(EMBEDS.get("main")).queue(message
                    -> {
                message.addReaction("⏮️").complete();
                

            }
            );
        }
    }

    private static void initEmbeds() {
        EMOJI.add(":smiley:");
        EMOJI.add(":track_previous:");
        EMOJI.add(":track_next:");
        if (EMBEDS.get("main") == null) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("PhyBot Configuration");
            FIELDS.put("main", new ArrayList<>());
            FIELDS.get("main").add(new Field("First Title", EMOJI.get(0) + " First Text", false));
            FIELDS.get("main").add(new Field("Second Title", EMOJI.get(1) + " Second Text", false));
            FIELDS.get("main").add(new Field("Third Title", EMOJI.get(2) + " Third Text", false));
            FIELDS.get("main").forEach((f) -> {
                eb.addField(f);
            });
            EMBEDS.put("main", eb.build());
        }

    }
}
