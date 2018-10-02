/*
 * Copyright 2018 Samwise210.
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

import static ca.liamgregory.phybot.Settings.guildAbbreviations;
import ca.liamgregory.phybot.menu.roles.RolesListener;
import ca.liamgregory.phybot.setup.SetupListener;
import java.util.Scanner;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

import static java.util.concurrent.TimeUnit.*;

/**
 *
 * @author Liam Gregory
 */
public class App {

    public static JDA jda;
    public static boolean running = true;
    public static HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ScheduledFuture<?> saverHandle;
    private static Runnable saver = new Runnable() {
        @Override
        public void run() {
            Ref.saveDB();
        }
    };

    public static void main(String... args) throws Exception {
        Ref.initRef();

        jda = new JDABuilder(AccountType.BOT).setGame(Game.playing(Ref.game)).setToken(Ref.token)
                .addEventListener(new openListener()).buildBlocking();
        jda.addEventListener(new CommandListener(), new ReactionListener(), new FilterListener(), new SomeoneListener(), new SetupListener());

        Settings.loadSettings();

        jda.addEventListener(new RolesListener());
        saverHandle = scheduler.scheduleAtFixedRate(saver, 10, 10, MINUTES);

        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        String input;
        while (running) {
            input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                exit();
            } else if (input.equalsIgnoreCase("save")) {
                Ref.saveDB();
            }
        }

    }

    public static void exit() {
        jda.shutdown();
        scheduler.shutdownNow();
        Ref.saveDB();
        running = false;
    }

}

class openListener implements EventListener {

    @Override
    public void onEvent(Event e) {
        if (e instanceof ReadyEvent) {
            System.out.println("API is ready!");
        }

    }

}
