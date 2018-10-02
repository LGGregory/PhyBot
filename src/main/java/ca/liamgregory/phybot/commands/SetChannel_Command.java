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
public class SetChannel_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        String res = null;
        if (!evt.getChannelType().equals(ChannelType.PRIVATE)) {
            if (evt.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
                switch (args[0]) {
                    case "confession":
                        Settings.getSettingsByGuild(evt.getGuild()).setConfessionChannel(evt.getChannel().getId());
                        res = evt.getChannel().getName() + " set as Confession channel for " + evt.getGuild().getName() + ".";
                        break;
                    case "listen":
                        res = "Now listening to this channel.";
                        break;
                    default:
                        res = "I don't know what channel you want to specify this one as.";
                        break;
                }

            } else {
                res = "You do not have sufficient priviledges to do that.";

            }

        } else {
            res = "Private channels are automatically all special channels for each user.";
        }
        evt.getChannel().sendMessage(res).queue();
    }

}
