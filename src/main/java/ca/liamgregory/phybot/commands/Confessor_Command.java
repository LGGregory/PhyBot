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

import ca.liamgregory.phybot.Ref;
import ca.liamgregory.phybot.Settings;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class Confessor_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        String res = null;
        if (args[0].startsWith("remove")) {
            String id;
            if(evt.getChannelType().equals(ChannelType.PRIVATE)){
                String[] split = args[0].split(" ", 2);
                id = Settings.getGuildIdByAbbreviation(split[1]);
            } else {
                id = evt.getGuild().getId();
            }
            
            
            Settings.getSettingsByGuildID(id).getProfessors().remove(evt.getAuthor().getId());
            System.out.println(Settings.getSettingsByGuildID(id).getProfessors().toString());
            res = "You have been removed from confessors.";
        
        
        
        } else {
            if (evt.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
                List<User> mentioned = evt.getMessage().getMentionedUsers();
                if (mentioned.size() == 1) {
                    if (!Settings.getSettingsByGuild(evt.getGuild()).getProfessors().contains(mentioned.get(0).getId())) {
                        Settings.getSettingsByGuild(evt.getGuild()).getProfessors().add(mentioned.get(0).getId());
                        res = "Added " + mentioned.get(0).getAsMention() + " to confessors for this server.";
                        mentioned.get(0).openPrivateChannel().queue((channel2) -> {
                            channel2.sendMessage(explain(evt.getGuild().getName())).queue();
                        });
                    } else {
                        res = "They're already a confessor for this guild, my dude.";
                    }
                } else {
                    res = "Incorrect number of parameters, my dude.";
                }
            } else {
                res = "You do not have sufficient priviledges to make that change.";
            }
            evt.getChannel().sendMessage(res).queue();
        }
    }

    private String explain(String guildName) {
        StringBuilder s = new StringBuilder();
        s.append("You have been added to the list of confessors for ");
        s.append(guildName + ".\n");
        s.append("When a confession arrives for this guild, you will recieve it as a DM.\n");
        s.append("To post it, respond >>profess ID, where ID is the number attached to the confession.\n");
        s.append("----NYI----\n");
        s.append("To disable yourself temporarily, send >>confessor silence X minutes/hours/days\n");
        s.append("To leave this list, send >>confessor remove\n");
        s.append("----NYI----");

        return s.toString();

    }

}
