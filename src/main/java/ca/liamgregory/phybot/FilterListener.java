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

import java.io.File;
import java.util.Map;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Liam Gregory
 */
public class FilterListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        if (!evt.getAuthor().isBot()) {
            String gID;
            if (evt.getGuild() != null) {
                gID = evt.getGuild().getId();
            } else {
                gID = evt.getPrivateChannel().getId();
            }

            String messageText = evt.getMessage().getContentRaw();

            boolean rep = false;

            if (Ref.guildRuleReplaceList.containsKey(gID)) {

                for (Map.Entry<String, String> rule : Ref.guildRuleReplaceList.get(gID).entrySet()) {
                    if (messageText.contains(rule.getKey())) {
                        rep = true;
                        messageText = messageText.replaceAll(rule.getKey(), rule.getValue());
                    }
                }

            }

            if (rep) {
                evt.getMessage().delete().queue(success -> {
                }, failure -> {
                });
                if (evt.getMessage().getAttachments().size() > 0) {
                    String fName = evt.getMessage().getAttachments().get(0).getFileName();
                    File f = new File(fName);
                    evt.getMessage().getAttachments().get(0).download(f);
                    evt.getChannel().sendFile(f, new MessageBuilder("What " + evt.getAuthor().getAsMention() + " meant to say was:\n" + messageText).build()).queue();
                    f.delete();
                } else {
                    evt.getChannel().sendMessage("What " + evt.getAuthor().getAsMention() + " meant to say was:\n" + messageText).queue();
                }
            }

        }
    }

}
