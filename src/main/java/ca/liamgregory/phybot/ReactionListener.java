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

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.RichPresence.Timestamps;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;

/**
 *
 * @author Liam Gregory
 */
public class ReactionListener implements EventListener {

    @Override
    public void onEvent(Event evt) {
        if (evt instanceof MessageReactionAddEvent) {
            MessageReactionAddEvent mrae = (MessageReactionAddEvent) evt;
            mrae.getChannel().getMessageById(mrae.getMessageId()).queue(message -> handleMessage(message));
        }

    }

    public void handleMessage(Message message) {

        List<MessageReaction> reactions = message.getReactions();

        reactions.forEach((MessageReaction reaction) -> {
            
            if (reaction.getReactionEmote().getName().equals(Ref.guildPinSymbol.get(message.getGuild().getId()))
                    && reaction.getCount() >= Ref.guildPinCount.get(message.getGuild().getId())) {
                message.getChannel().getPinnedMessages().queue(list -> handlePinning(list, message, true));
                return;
            }
        });
    }

    public void handlePinning(List<Message> list, Message message, boolean pin) {
        if (list.contains(message)) {
            if (!pin) {
                message.unpin().queue();
            }
        } else {
            if (pin) {
                if (list.size() < Ref.MAXPINS) {
                    message.pin().queue();
                } else {
                    message.getChannel().sendMessage("Pins are full, my dude.").queue();
                }
            }
        }

    }

}
