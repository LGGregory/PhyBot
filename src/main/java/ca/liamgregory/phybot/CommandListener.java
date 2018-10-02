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
package ca.liamgregory.phybot;

import java.util.Random;
import ca.liamgregory.phybot.commands.*;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Liam Gregory
 */
public class CommandListener extends ListenerAdapter {

    private final String COMPLEAT = "compleat";
    private final String HELP = "help";
    private final String INSPIRE = "inspireme";

    private static Random rng = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {

        Message message = evt.getMessage();

        // Ensures message is a command
        if (!(evt.getAuthor().isBot()) && message.getContentRaw().startsWith(Ref.prefix)) {

            // Makes the command parseable
            String[] splitMessage = message.getContentRaw().split(" ", 2);
            splitMessage[0] = splitMessage[0].substring(Ref.prefix.length()).toLowerCase();

            switch (splitMessage[0]) {
                case "rot13":
                    message.delete().complete();
                    if (splitMessage.length > 1) {
                        new Rot13_Command().execute(evt, splitMessage[1]);
                    } else {
                        new SendString_Command().execute(evt, "You didn't enter anything to Rot13, my dude");
                    }
                    break;
                case COMPLEAT:
                    evt.getChannel().sendMessage(evt.getAuthor().getAsMention() + " demands Compleation!").queue();
                    break;
                case INSPIRE:
                    new InspireMe_Command().execute(evt);
                    break;
                case "depressme":
                    new DepressMe_Command().execute(evt);
                    break;
                case HELP:
                    new Help_Command().execute(evt);
                    break;
                case "rotme":
                    message.delete().complete();
                    if (splitMessage.length > 1) {
                        new RotMe_Command().execute(evt, splitMessage[1]);
                    } else {
                        new RotMe_Command().execute(evt);
                    }
                    break;
                case "eightball":
                    new EightBall_Command().execute(evt, splitMessage[1]);
                    break;
                case "echo":
                    new StrictEcho_Command().execute(evt);
                    break;
                case "listen":
                    /* Ref.listenChannel = evt.getChannel();
                    Ref.listener.start();*/ //Honestly, just implement this later.
                    break;
                case "setpin":
                    new SetPin_Command().execute(evt, splitMessage[1]);
                    break;
                case "setrule":
                    new SetRule_Command().execute(evt, splitMessage[1]);
                    break;
                case "savedb":
                    Ref.saveDB();
                    break;
                case "clearrule":
                    new ClearRule_Command().execute(evt, splitMessage[1]);
                    break;
                case "sendrules":
                    message.delete().complete();
                    if (splitMessage.length > 1) {
                        new SendRules_Command().execute(evt, splitMessage[1]);
                    } else {
                        new SendRules_Command().execute(evt);
                    }
                    break;
                case "pick":
                    if (splitMessage.length > 1) {
                        new Pick_Command().execute(evt, splitMessage[1]);
                    } else {
                        evt.getChannel().sendMessage("What am I supposed to be picking?").queue();
                    }
                    break;
                case "getname":
                    message.getChannel().sendMessage(evt.getAuthor().getName()).queue();
                    break;
                case "addsudoer":
                    new AddSudoer_Command().execute(evt);
                    break;
                case "removesudoer":
                    new RemSudoer_Command().execute(evt);
                    break;
                case "passthrough":
                    if (splitMessage.length > 1) {
                        new Passthrough_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;
                case "confess":
                    if (splitMessage.length > 1) {
                        new Confess_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;
                case "profess":
                    if (splitMessage.length > 1) {
                        new Profess_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;
                case "confessor":
                    if (splitMessage.length > 1) {
                        new Confessor_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;
                case "id":
                    if (splitMessage.length > 1) {
                        new IDME_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        new IDME_Command().execute(evt, "me");
                    }
                    break;
                case "del":
                case "delete":
                    if (splitMessage.length > 1) {
                        new Delete_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;
                case "setabbreviation":
                case "setabbr":
                case "setabbrev":
                    if (splitMessage.length > 1) {
                        new Abbreviation_Command().execute(evt, splitMessage[1].trim(), "set");
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;
                case "getabbreviation":
                case "getabbr":
                case "getabbrev":
                    new Abbreviation_Command().execute(evt, "", "get");
                    break;
                case "setchannel":
                    if (splitMessage.length > 1) {
                        new SetChannel_Command().execute(evt, splitMessage[1].trim());
                    } else {
                        paramError(evt.getChannel());
                    }
                    break;

            }

        }
    }

    public static void paramError(MessageChannel channel) {
        channel.sendMessage("Insufficient number of parameters.").queue();
    }

    public static void tellJoke(MessageChannel channel) {

        channel.sendMessage(
                "Placeholder:\n```There once was a lady named Bright\nWhose speed was much faster than light.\n"
                + "She set out one day\nIn a relative way,\nAnd returned on the previous night.```")
                .queue();

    }

}
