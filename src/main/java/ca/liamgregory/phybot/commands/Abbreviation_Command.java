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

import ca.liamgregory.phybot.Settings;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class Abbreviation_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        String res = "";
        if (!evt.getChannelType().equals(ChannelType.PRIVATE)) {
            if (args[1].equals("set")) {

                if (evt.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
                    Settings.addGuildIdAbbreviation(args[0], evt.getGuild().getId());
                    res = "Abbreviation for " + evt.getGuild().getName() + " set as " + args[0];
                } else {
                    res = "You don't have permission to do that.";
                }
            } else {
                String abbr;
                if ((abbr = Settings.getSettingsByGuildID(evt.getGuild().getId()).getAbbreviation()) != null) {
                    res = "The abbreviation for " + evt.getGuild().getName() + " is " + abbr + ".";
                } else {
                    res = "There is no abbreviation for " + evt.getGuild().getName() + ".";
                }
            }
        } else {
            res = "Private channels can't have abbreviations.";
        }

        evt.getChannel().sendMessage(res).queue();
    }
}
