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

import java.util.ArrayList;

import ca.liamgregory.phybot.Ref;
import static ca.liamgregory.phybot.commands.Util.rot13;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class Rot13_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {

        MessageChannel channel = evt.getChannel();

        if (args.length!=0) {
            MessageBuilder mbuild = new MessageBuilder();

            mbuild.append(evt.getAuthor().getAsMention() + " said:\n");

            String rot13String = rot13(args[0]);
            mbuild.append(rot13String + "\n");

            Ref.rot13Messages.putIfAbsent(channel.getId(), new ArrayList<>());
            Ref.rot13Messages.get(channel.getId()).add(rot13String);

            mbuild.append("Message ID: " + (Ref.rot13Messages.get(channel.getId()).size() - 1));

            channel.sendMessage(mbuild.build()).queue();
        }
    }

}
