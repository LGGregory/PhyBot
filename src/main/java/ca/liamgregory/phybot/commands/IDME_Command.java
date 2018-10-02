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

import ca.liamgregory.phybot.App;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.PermissionUtil;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class IDME_Command implements Generic_Command{

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {
        String id = null;
        
        //Determine what id is wanted
        String comm = args[0].toLowerCase();
        if(comm.startsWith("guild")){
            id = evt.getGuild().getId();
        } else if (comm.startsWith("channel")){
            id = evt.getChannel().getId();
        } else if (comm.startsWith("me")){
            id = evt.getAuthor().getId();
        } else if (comm.startsWith("user") || evt.getMessage().getMentionedUsers().size()>0){
            //Find first user id
            if(!comm.startsWith("user")) comm = "user " + comm;
                
            id = evt.getMessage().getMentionedUsers().get(0).getId();
            
        }
        
        //Tell user what's being sent
        String intro = (comm.equals("me"))?"Your snowflake id is ":"The requested "+comm+"'s snowflake id is ";
        String statement = intro + id;
        
        //If Bot can write in channel
        if(PermissionUtil.checkPermission(evt.getTextChannel(), evt.getGuild().getMember(App.jda.getSelfUser()), Permission.MESSAGE_WRITE)){
            //Post there
            evt.getChannel().sendMessage(statement).queue();
        } else {
            //If not, send in a pm to the author
            evt.getAuthor().openPrivateChannel().queue((channel2) -> {
                channel2.sendMessage(statement).queue();
            });
        }
            
    }
    
    
}
