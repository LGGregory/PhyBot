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

//MongoDB
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.bson.Document;

public class Ref {

    /**
     * Gets the number of confessions made so far
     *
     * @return the confessionCount
     */
    public static long getConfessionCount() {
        return confessionCount;
    }

    /**
     * Increases the number of confessions made by one. *
     */
    public static void increaseConfessionCount() {
        confessionCount = confessionCount + 1;
    }

    public static MessageChannel listenChannel;

    private final static Random rng = new Random();

    public static final String appPath = clearDirectory(System.getProperty("user.dir"));
    public static final String dataPath = appPath + System.getProperty("file.separator") + "Data" + System.getProperty("file.separator");
    public static final String guildPath = dataPath + "Guilds" + System.getProperty("file.separator");
    public static final String dataFile = dataPath + "data.ser";
    public static final String tokenFile = dataPath + "token.ser";

    public static MongoClient mongoClient;
    public static ValidationOptions settingsVal = new ValidationOptions().validator(Filters.exists("guild_id"));

    /**
     * Autopin info
     */
    public static final int MAXPINS = 50;
    public static final Integer guildDefaultPinRequired = 5;
    public static HashMap<String, String> guildPinSymbol = new HashMap<>();
    public static HashMap<String, Integer> guildPinCount = new HashMap<>();
    // End Autopin

    public static ArrayList<String> sudoers = new ArrayList<>();

    public static HashMap<String, HashMap<String, String>> guildRuleReplaceList = new HashMap<>();

    public static ArrayList<Pair<String, String>> commandList = new ArrayList<>();

    public static HashMap<String, ArrayList<String>> rot13Messages = new HashMap<String, ArrayList<String>>();

    public static String token;
    public static String mongoURL;
    public static final String prefix = ">>";
    public static final String game = "type >>help";

    private static Long confessionCount;
    public static String professor = "209886024350826507";

    public static final String unknownCommand = "I don't understand that, my dude";

    public static long inspirationGiven = 0;

    public static final ArrayList<String> depressingTruths = new ArrayList<String>();

    public static final ArrayList<String> eightBall = new ArrayList<String>();
    public static Thread listener;

    public static void initRef() {
        loadPrivate();
        loadDB();

        mongoClient = MongoClients.create(mongoURL);
        MongoDatabase database = mongoClient.getDatabase("phybot");
        MongoCollection<Document> coll = database.getCollection("settings");

        initStatic();

    }

    public static void saveDB() {
        FileOutputStream fos = null;
        File file;

        try {
            file = new File(dataFile);
            fos = new FileOutputStream(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(rot13Messages);
            oos.writeObject(inspirationGiven);
            oos.writeObject(guildPinSymbol);
            oos.writeObject(guildPinCount);
            oos.writeObject(guildRuleReplaceList);
            oos.writeObject(sudoers);
            oos.writeObject(confessionCount);
            
            oos.close();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            System.out.println("Database stored in " + dataFile + " at " + dtf.format(now));
        } catch (IOException e) {
        }

        //Other data
        Settings.saveSettings();
        Confession.saveConfessions();

    }

    public static void loadDB() {
        FileInputStream fis = null;
        File file = null;
        System.out.println(dataFile);
        try {
            file = new File(dataFile);
            if (!file.exists()) {
                return;
            }
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            rot13Messages = (HashMap<String, ArrayList<String>>) ois.readObject();
            inspirationGiven = (Long) ois.readObject();
            guildPinSymbol = (HashMap<String, String>) ois.readObject();
            guildPinCount = (HashMap<String, Integer>) ois.readObject();
            guildRuleReplaceList = (HashMap<String, HashMap<String, String>>) ois.readObject();
            sudoers = (ArrayList<String>) ois.readObject();
            confessionCount = (Long) ois.readObject();
            if (confessionCount == null) {
                confessionCount = 0L;
            }

            
            ois.close();
            System.out.println("Database restored from  " + dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Confession.loadConfessions();
    }

    public static void loadPrivate() {
        File file = null;
        System.out.println(tokenFile);
        try {
            file = new File(tokenFile);
            if (!file.exists()) {
                System.out.println("Problem here.");
                return;
            }

            Scanner scanner = new Scanner(file);
            token = scanner.nextLine();
            mongoURL = scanner.nextLine();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static String clearDirectory(String path) {
        int index = path.lastIndexOf(System.getProperty("file.separator"));
        return path.substring(0, index);
    }

    public static Random getRng() {
        return rng;
    }

    public void loadCommitDB() {
        Document d = new Document();

    }

    //All boring string initializations
    private static void initStatic() {
        commandList.add(new Pair(">>Help", "Shows this list."));
        commandList.add(new Pair(">>InspireMe", "Gives you some inspiration."));
        commandList.add(new Pair(">>DepressMe", "Gives you some depression."));
        commandList.add(new Pair(">>EightBall <Question>", "Consults a Magic Eight-Ball to answer your questions."));
        commandList.add(new Pair(">>Rot13 <String>", "Shows the rot13 of a string."));
        commandList.add(new Pair(">>RotMe (<ID>)", "Sends you the last message rot13'd, unscambled.\n<ID> sends the ID message instead."));
        commandList.add(new Pair(">>SetPin <Emoji> <Count>", "Sets the Autopin Emoji and Count."));
        commandList.add(new Pair(">>SetRule <replacedWord> : <replacingWord>", "Sets a rule to replace the first word with the second."));
        commandList.add(new Pair(">>ClearRule <replacedWord>", "Removes replacment rule for the given word."));
        commandList.add(new Pair(">>SendRules (-e)", "Sends you the replacement rules for the server.\n-e sends you the unmasked list."));

        depressingTruths.add("Eventually, everyone you love will die.");
        depressingTruths.add("You didn't get in to UofT.");
        depressingTruths.add("Anime probably was a mistake.");
        depressingTruths.add("Fifteen major species have gone extinct since you were born.");
        depressingTruths.add("There will never be a Firefly Season 2");
        depressingTruths.add("You don't have a big-tiddy goth gf.");
        depressingTruths.add("No matter how good you are at something, there's always a 12-year old Asian that does it better than you.");

        eightBall.add("It is certain");
        eightBall.add("It is decidedly so");
        eightBall.add("Wihout a doubt");
        eightBall.add("Yes, definitely");
        eightBall.add("You may rely on it");
        eightBall.add("As I see it, yes");
        eightBall.add("Most likely my dude");
        eightBall.add("Outlook good");
        eightBall.add("Yes");
        eightBall.add("Signs point to yes");
        eightBall.add("Reply hazy try again");
        eightBall.add("Ask again later");
        eightBall.add("Better not to tell you now");
        eightBall.add("Cannot predict now");
        eightBall.add("Concentrate and ask again");
        eightBall.add("Don't count on it");
        eightBall.add("My reply is not");
        eightBall.add("My sources say no");
        eightBall.add("Outlook not so good");
        // eightBall.add("Very doubtful");
        eightBall.add("Not likely my dude");
    }
}
