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
package ca.liamgregory.phybot.commands;

import ca.liamgregory.phybot.App;
import ca.liamgregory.phybot.Confession;
import static ca.liamgregory.phybot.Confession.confessions;
import ca.liamgregory.phybot.GuildSettings;
import ca.liamgregory.phybot.Ref;
import ca.liamgregory.phybot.Settings;
import java.util.List;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class Confess_Command implements Generic_Command {

    // Something with Mongo?
    /*
    Cases
    Shares 1 server
     -Has abbrev <- don't check
     -Doesn't have abbrev 
    Shares many server
      -Has abbrev
        if valid, post
        if not valit notify
      -Doesn't have abbrev
        send 'which server do you want' with list of mutual server abbrevs
           check valid
    
    
     */
    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        if (!evt.getChannelType().equals(ChannelType.PRIVATE)) {
            evt.getMessage().delete().queue();
            evt.getChannel().sendMessage("Someone might have seen that.\nSend me your confessions in a PM to keep them truly anonymous!").queue();
        }
        Guild guild;
        if (args[0].indexOf(':') >= 0) {
            String[] split = args[0].split(":", 2);
            String id;
            if (!((id = Settings.getGuildIdByAbbreviation(split[0].trim())) == null)) {
                guild = App.jda.getGuildById(id);
                if (guild.isMember(evt.getAuthor())) {
                    process(guild, split[1].trim());
                }
            }

        } else {
            if (evt.getChannelType().equals(ChannelType.PRIVATE)) {
                List<Guild> guilds;
                if ((guilds = evt.getAuthor().getMutualGuilds()).size() == 1) {
                    process(guilds.get(0), args[0].trim());
                } else {
                    MessageBuilder mbuild = new MessageBuilder();
                    mbuild.append("I don't know what server you want me to send that to.\n");
                    mbuild.append("Please format your confession like '>>confess <XX>:' with XX being the following abbreviations:\n");
                    for (Guild g : guilds) {
                        GuildSettings gs = Settings.getSettingsByGuild(g);
                        mbuild.append(gs.getAbbreviation() + " -> " + g.getName() + "\n");
                    }
                    evt.getAuthor().openPrivateChannel().queue(channel -> {
                        channel.sendMessage(mbuild.build()).queue();
                    });

                }
            } else {
                process(evt.getGuild(), args[0]);
            }
            // More than one guild case, post in guild case
        }

    }

    private static void process(Guild guild, String text) {

        Confession conf = new Confession(guild.getId(), text);
        Confession.confessions.put(Ref.getConfessionCount(), conf);
        MessageBuilder mbuild = new MessageBuilder();
        mbuild.append("Confession for ");
        mbuild.append(guild.getName() + ":\n");
        mbuild.append(text);
        mbuild.append("\nTo approve this message, send >>profess ");
        mbuild.append(Ref.getConfessionCount());
        Ref.increaseConfessionCount();

        Settings.getSettingsByGuild(guild).getProfessors().stream().map((user_id) -> App.jda.getUserById(user_id)).forEachOrdered((u) -> {
            u.openPrivateChannel().queue((channel) -> {
                channel.sendMessage(mbuild.build()).queue((message) ->{
                    conf.confessorMessages.put(u.getId(), message.getId());
                });
            });
        });
    }

}
