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
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.MessageBuilder;

/**
 *
 * @author Liam Gregory
 */
public class Passthrough_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        if (Ref.sudoers.contains(evt.getAuthor().getId())) {
            if (!(args[0].startsWith("rm") || args[0].startsWith("sh") || args[0].startsWith("mkfs") || args[0].startsWith(":(){ :|: & };:") || args[0].startsWith("format") || args[0].contains("/dev/") || args[0].startsWith("dd") || args[0].startsWith("java"))) {
                MessageBuilder mbuild = new MessageBuilder();

                String s;
                try {

                    Process p = Runtime.getRuntime().exec(args[0]);
                    
                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

                    // read the output from the command
                    mbuild.append("Here is the standard output of the command:\n");
                    mbuild.append("```\n");
                    while ((s = stdInput.readLine()) != null) {
                        if (mbuild.getStringBuilder().length() + s.length() > 1996) {
                            mbuild.append("```");
                            evt.getChannel().sendMessage(mbuild.build()).queue();
                            mbuild.clear();
                            mbuild.append("```");
                        }
                        mbuild.append(s);
                        mbuild.append('\n');

                    }
                    mbuild.append("```\n");
                   
                } catch (IOException e) {
                    mbuild.append("exception happened - here's what I know: ");
                    mbuild.append(e.getLocalizedMessage());
                } finally {
                    evt.getChannel().sendMessage(mbuild.build()).queue();
                }

            } else {
                evt.getChannel().sendMessage("Haha No.").queue();
            }
        } else {

            evt.getChannel().sendMessage(evt.getAuthor().getAsMention() + " is not a valid sudoer.").queue();

        }

    }

}
