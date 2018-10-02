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
import static ca.liamgregory.phybot.commands.Util.rot13;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class RotMe_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {

        MessageChannel channel = evt.getChannel();

        if (Ref.rot13Messages.get(channel.getId()) != null) {

            int id = (Ref.rot13Messages.get(channel.getId()) != null) ? Ref.rot13Messages.get(channel.getId()).size() - 1 : 0;

            if (args.length > 0) {
                if (args[0].matches("\\d+")) {
                    int id2 = Integer.parseInt(args[0]);
                    if (id2 > id) {
                        evt.getAuthor().openPrivateChannel().queue((channel2) -> {
                            channel2.sendMessage("Invalid Entry Number").queue();
                        });
                    } else {
                        id = id2;
                    }
                }
            }

            String s = Ref.rot13Messages.get(channel.getId()).get(id);
            String rot13s = rot13(s);

            evt.getAuthor().openPrivateChannel().queue((channel2) -> {
                channel2.sendMessage(rot13s).queue();
            });

        } else {
            channel.sendMessage(
                    "Ain't no messages been rot13'd in this channel, my dude. Or Liam fucked up the database, the dumb fuck.")
                    .queue();

        }

    }

}
