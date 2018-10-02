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
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class SetPin_Command implements Generic_Command {

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        if (evt.getMember().getPermissions().contains(Permission.MANAGE_SERVER)) {
            if (args.length > 0) {

                String[] split = args[0].split(" ");
                if (split.length == 2) {
                    int count;
                    try {
                        count = Integer.parseInt(split[1]);
                        if (count < 1) {
                            throw new ArithmeticException();
                        }
                    } catch (ArithmeticException e) {
                        evt.getChannel().sendMessage("Invalid count, my dude.").queue();
                        return;
                    } catch (NumberFormatException e) {
                        evt.getChannel().sendMessage("Format error, my dude.").queue();
                        return;
                    }
                    String parsed = parseString(split[0]);
                    Ref.guildPinSymbol.put(evt.getGuild().getId(), parsed);
                    Ref.guildPinCount.put(evt.getGuild().getId(), count);
                    System.out.println(parsed + " : " + count);

                    evt.getChannel().sendMessage("Autopin set to " + count + " " + parsed + ((count == 1) ? " reaction." : " reactions.")).queue();

                }

            }
        } else {
            evt.getChannel().sendMessage("You don't have permissions to do that.").queue();
        }

    }

    public static String parseString(String input) {
        if (input.charAt(0) != '<') {
            return input;
        }
        return input.substring(input.indexOf(":"), input.lastIndexOf(":") + 1);
    }
}
