package ru.warmouse.coolbooks;

import java.util.ArrayList;
import java.util.List;

public class NameGenerator {
    private final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

    private final java.util.Random rand = new java.util.Random();

    private final List<String> identifiers = new ArrayList<>();

    private String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    public void prepare() {
        for (int i = 0; i < 1000; i++) {
            identifiers.add(randomIdentifier());
        }
    }

    public String generate() {
        StringBuilder builder = new StringBuilder();
        int count = rand.nextInt(5) + 2;
        for (int i = 0; i < count; i++) {
            builder.append(identifiers.get(rand.nextInt(identifiers.size())));
            if (i < count - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
}
