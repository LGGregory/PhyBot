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
import ca.liamgregory.phybot.Ref;
import ca.liamgregory.phybot.Settings;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class Profess_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {

        try {
            Long.parseLong(args[0]);
        } catch (NumberFormatException ex) {
            evt.getChannel().sendMessage("Not a valid number.").queue();
        }

        Long id = Long.parseLong(args[0]);
        Confession conf = Confession.confessions.get(id);
        if (!conf.isPosted()) {
            if (Settings.getSettingsByGuildID(conf.guildId).getProfessors().contains(evt.getAuthor().getId())) {

                MessageBuilder mbuild = new MessageBuilder();
                mbuild.append("__**Confession #" + Settings.getSettingsByGuildID(conf.guildId).getConfessionCount() + "**__\n");
                mbuild.append(Confession.confessions.get(id).messageContent);
                Settings.getConfessionChannelByGuild(App.jda.getGuildById(conf.guildId)).sendMessage(mbuild.build()).queue(success -> {

                    Confession.confessions.get(id).post(evt.getAuthor().getId());

                    Settings.getSettingsByGuildID(conf.guildId).increaseConfessionCount();
                });
            }
        }
    }

}
