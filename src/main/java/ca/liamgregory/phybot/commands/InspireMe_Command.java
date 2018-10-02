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

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import ca.liamgregory.phybot.App;
import ca.liamgregory.phybot.Ref;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 *
 * @author Liam Gregory
 */
public class InspireMe_Command implements Generic_Command {

    private static final String inspiroBotURL = "http://inspirobot.me/api?generate=true";

    @Override
    public void execute(MessageReceivedEvent evt, String... args) {

        String response;
        MessageBuilder mbuild = new MessageBuilder();
        try {
            HttpRequest request = App.requestFactory.buildGetRequest(new GenericUrl(inspiroBotURL));
            response = request.execute().parseAsString();
            mbuild.append("Have some inspiration!\n");
            mbuild.append(response);
            Ref.inspirationGiven++;
        } catch (Exception e) {
            mbuild.append(e.getLocalizedMessage());
        }

        evt.getChannel().sendMessage(mbuild.build()).queue();

    }

}
