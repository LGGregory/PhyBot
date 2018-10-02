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
public class Pick_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        String[] choices = args[0].split(",");
        int choiceCount = choices.length;
        int chosen = Ref.getRng().nextInt(choiceCount);
        evt.getChannel().sendMessage("I chose:").queue();
        evt.getChannel().sendMessage(choices[chosen].trim()).queue();
    }

}
