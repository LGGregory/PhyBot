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
package ca.liamgregory.phybot.menu;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.function.Consumer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

/**
 *
 * @author Liam Gregory
 */
public class Menu {

    private ArrayList<Menu_Item> menuItems = new ArrayList<>();

    private transient ArrayList<String> emojiList = new ArrayList<>();

    private static final int MAXITEMS = 25;

    private EmbedBuilder embedBuilder = new EmbedBuilder();
    private MessageEmbed embed;

    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }

    public Menu(String title, String description) {
        this(title);
        embedBuilder.setDescription(description);
    }

    public Menu(String title) {
        embedBuilder.setTitle(title);

    }

    public void Display(MessageChannel channel, ArrayList<String> menus) {
        this.Display(channel, menus, null, null);
    }

    public void Display(MessageChannel channel, ArrayList<String> menus, Consumer<? super Message> success) {
        this.Display(channel, menus, success, null);
    }

    public void Display(MessageChannel channel, ArrayList<String> menus, Consumer<? super Message> success, Consumer<? super Throwable> failure) {
        channel.sendMessage(embedBuilder.build()).queue(message -> {
            menus.add(message.getId());
            menuItems.forEach((item) -> {
                message.addReaction(item.getEmoji()).complete();
            });

        }, failure);
    }

    public boolean watchedEmoji(String emoji){
        return emojiList.contains(emoji);
    }
    
    public Invokable_Command commandByEmoji(String emoji){
        for(Menu_Item item : menuItems){
            if(item.getEmoji().equals(emoji)){
                return item.getCommand();
            }
        }
        return null;
    }
    
    
    /**
     * Attempts to add a Menu_Item to the Menu
     *
     * @param item - the Menu_Item to add
     * @return the success of adding the item to the list
     */
    public boolean addItem(Menu_Item item) {
        if (menuItems.size() < MAXITEMS) {
            this.embedAdd(item);
            return menuItems.add(item) && this.emojiList.add(item.getEmoji());
        } else {
            return false;
        }
    }

    private void embedAdd(Menu_Item item) {
        embedBuilder.addField(item.getLine(), "", false);

    }

}
