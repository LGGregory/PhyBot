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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class SetRule_Command implements Generic_Command {

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
     */
    /**
     * @param args the command line arguments
     */
    public void execute(MessageReceivedEvent evt, String... args) {
        if (evt.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
            Guild guild = evt.getGuild();

            String s = args[0];
            if (s.indexOf(":") > 3 && s.indexOf(":") < s.length() - 4) {
                evt.getMessage().delete().queue(success -> {
                }, failure -> {
                });
                String s1 = s.substring(0, s.indexOf(":")).trim();
                String s2 = s.substring(s.indexOf(":") + 1).trim();

                String s1t = Util.maskMid(s1);

                Ref.guildRuleReplaceList.putIfAbsent(guild.getId(), new HashMap<>());
                Ref.guildRuleReplaceList.get(guild.getId()).put(s1, s2);

                String repConfirm = "New Rule: " + s1t + " will be replaced by " + s2;

                evt.getChannel().sendMessage(repConfirm).queue();

            } else {
                System.out.println("Invalid Format");
            }
        }
    }

}
