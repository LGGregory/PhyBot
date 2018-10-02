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
import java.util.Map;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class SendRules_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        if (!evt.getAuthor().isBot()) {
            MessageBuilder mbuild = new MessageBuilder();
            String gID = evt.getGuild().getId();
            if (Ref.guildRuleReplaceList.get(gID) != null && !Ref.guildRuleReplaceList.get(gID).isEmpty()) {
                boolean explicit = false;
                if (args.length > 0 && args[0].toLowerCase().startsWith("-e")) {
                    explicit = true;
                }
                mbuild.append("Rules for Server " + evt.getGuild().getName() + ": \n");
                for (Map.Entry<String, String> entry : Ref.guildRuleReplaceList.get(gID).entrySet()) {
                    mbuild.append("Rule for <");
                    if (explicit) {
                        mbuild.append(entry.getKey());
                    } else {
                        mbuild.append(Util.maskMid(entry.getKey()));
                    }
                    mbuild.append("> : ");
                    mbuild.append(entry.getValue());
                    mbuild.append("\n");
                }

            } else {
                mbuild.append("No rules for Server " + evt.getGuild().getName() + " found.");
            }
            evt.getAuthor().openPrivateChannel().queue((channel2) -> {
                channel2.sendMessage(mbuild.build()).queue();
            });
        }

    }

}
