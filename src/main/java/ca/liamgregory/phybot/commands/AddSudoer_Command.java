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
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class AddSudoer_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        if (evt.getAuthor().getName().startsWith("Samwise210") || Ref.sudoers.contains(evt.getAuthor().getId())) {
            for (User author : evt.getMessage().getMentionedUsers()) {
                if (!Ref.sudoers.contains(author.getId())) {
                    Ref.sudoers.add(author.getId());
                    evt.getChannel().sendMessage(author.getAsMention() + " added to Sudoers.").queue();
                } else {
                    evt.getChannel().sendMessage(author.getAsMention() + " already in Sudoers, my dude.").queue();
                }
            }
        }

    }

}
