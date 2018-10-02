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

import java.util.List;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Liam Gregory
 */
public class SomeoneListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
        if (evt.getMessage().getContentRaw().toLowerCase().contains("@someone")) {
            List<Member> members = evt.getGuild().getMembers();
            Member m;
            do {
                m = members.get(Ref.getRng().nextInt(members.size()));
            } while (validUserCheck(m));
            String s = "Pinging " + m.getAsMention() + ".";
            evt.getChannel().sendMessage(s).queue();
        }

    }

    private boolean validUserCheck(Member member) {
        if (member.getUser().isBot()) {
            return false;
        }
        if (!(member.getOnlineStatus().equals(OnlineStatus.ONLINE)
                || (member.getOnlineStatus().equals(OnlineStatus.IDLE))
                || (member.getOnlineStatus().equals(OnlineStatus.DO_NOT_DISTURB)))) {
            return true;
        } else {
            return false;
        }
    }

}
