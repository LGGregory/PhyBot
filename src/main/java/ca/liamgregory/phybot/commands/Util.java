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

/**
 *
 * @author Liam Gregory
 */
public class Util {

    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public static String rot13(String s) {
        StringBuilder sbuild = new StringBuilder();
        for (Character c : s.toCharArray()) {
            Character t = c.toString().toLowerCase().charAt(0);
            if (alphabet.indexOf(t) >= 0) {
                sbuild.append(rot13(t, Character.isUpperCase(c)));
            } else {
                sbuild.append(c);
            }
        }
        return sbuild.toString();
    }

    public static char rot13(char c, boolean upper) {
        c = ("" + c).toLowerCase().charAt(0);
        int pos = (alphabet.indexOf(c) + 13) % 26;
        char ret = alphabet.charAt(pos);
        return (upper) ? ("" + ret).toUpperCase().charAt(0) : ret;

    }

 /*   public static void main(String... args){
        System.out.println(maskMid("Hello"));
        System.out.println(maskMid("Hellfafaffadfadfadfadao"));
        System.out.println(maskMid("Helra l1r1r13o"));
        System.out.println(maskMid("Helloef111"));
    }*/
    
    public static String maskMid(String s1) {
        StringBuilder s1b = new StringBuilder();
        s1b.append(s1.charAt(0));
        for (int i = 1; i < s1.length() - 1; i++) {
            s1b.append("*");
        }
        s1b.append(s1.charAt(s1.length() - 1));
        return s1b.toString();
    }

}
