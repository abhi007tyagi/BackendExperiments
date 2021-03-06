package com.tyagiabhinav;
/*      All rights reserved. No part of this project may be reproduced, distributed,copied,transmitted or
        transformed in any form or by any means, without the prior written permission of the developer.
        For permission requests,write to the developer,addressed “Attention:Permissions Coordinator,”
        at the address below.

        Abhinav Tyagi
        DGIII-44Vikas Puri,
        New Delhi-110018
        abhi007tyagi@gmail.com */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IDGenerator {

    public static String getRandomID() {
        String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String intPool = "0123456789";
        return shuffle(generateRandomString(charPool, 3) + generateRandomString(intPool, 6));
    }

    private static String generateRandomString(String characters, int length) {
        Random random = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        String id= new String(text);
        System.out.println("Generated String --> "+id);
        return id;
    }

    private static String shuffle(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        System.out.println("Final String --> "+output.toString());
        return output.toString();
    }

    public static void main(String[] args){
        getRandomID();
    }
}
