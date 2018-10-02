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

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.Event;

/**
 *
 * @author Liam Gregory
 */
public class Menu_Item {
    
    
    private String emoji;
    private String text;
    private Invokable_Command command;

    public Menu_Item(String emoji, String text, Invokable_Command command) {
        this.emoji = emoji;
        this.text = text;
        this.command = command;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getText() {
        return text;
    }

    public String getLine() {
        return emoji + " " + text;
    }

    public Invokable_Command getCommand(){
        return command;
    }
}
