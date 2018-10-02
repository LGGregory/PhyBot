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
package ca.liamgregory.phybot.menu.roles;

import ca.liamgregory.phybot.GuildSettings;
import ca.liamgregory.phybot.Ref;
import ca.liamgregory.phybot.Settings;
import ca.liamgregory.phybot.menu.MenuTest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.EventListener;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 *
 * @author Liam Gregory
 */
public class RolesListener extends ListenerAdapter {

    private static ArrayList<String> menus = new ArrayList<>();

    public RolesListener() {

        Settings.settingsByGuild.entrySet().stream().map((entry) -> {
            entry.getValue().menus = new HashMap<>();
            return entry;
        }).forEachOrdered((entry) -> {
            entry.getValue().menus.put("roles", MenuTest.roleMenu());
        });

    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent evt) {
        // consider adding !evt.getReaction().getReactionEmote().isEmote() to filter out non-custom emoji
        if(menus.contains(evt.getMessageId())){
            
        }
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {

        if (!(evt.getAuthor().isBot()) && evt.getMessage().getContentRaw().startsWith(Settings.settingsByGuild.get(evt.getGuild().getId()).getPrefix() + "roles")) {

            Settings.settingsByGuild.get(evt.getGuild().getId()).menus.get("roles").Display(evt.getChannel(), menus);

        }
    }

}
