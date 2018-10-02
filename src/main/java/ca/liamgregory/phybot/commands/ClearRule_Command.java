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
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class ClearRule_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        evt.getMessage().delete().queue(success -> {
        }, failure -> {
        });
        String s = "Replacement rule not found.";
        if (args.length > 0 && args[0].length() > 2) {
            if (Ref.guildRuleReplaceList.get(evt.getGuild().getId()).containsKey(args[0])) {

                s = "Removed replacement rule for " + Util.maskMid(args[0]);
                Ref.guildRuleReplaceList.get(evt.getGuild().getId()).remove(args[0]);
            }
        }
        evt.getChannel().sendMessage(s).queue();
    }

}
