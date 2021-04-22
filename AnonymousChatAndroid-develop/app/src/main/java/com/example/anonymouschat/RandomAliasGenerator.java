package com.example.anonymouschat;

import java.util.Random;

public class RandomAliasGenerator {

    public static String getRandomName() {
        String[] adjs = {"Autumn", "Hidden", "Bitter", "Misty", "Silent", "Empty", "Dry", "Dark", "Summer", "Icy", "Delicate", "Quiet", "White", "Cool", "Spring", "Winter", "Patient", "Twilight", "Dawn", "Crimson", "Wispy", "Weathered", "Blue", "Billowing", "Broken", "Cold", "Damp", "Falling", "Frosty", "Green", "Long", "Late", "Lingering", "Bold", "Little", "Morning", "Muddy", "Old", "Red", "Rough", "Still", "Small", "Sparkling", "Throbbing", "Shy", "Wandering", "Withered", "Wild", "Black", "Young", "Holy", "Solitary", "Fragrant", "Aged", "Snowy", "Proud", "Floral", "Restless", "Divine", "Polished", "Ancient", "Purple", "Lively", "Nameless"};
        String[] nouns = {"Waterfall", "River", "Breeze", "Moon", "Rain", "Wind", "Sea", "Morning", "Snow", "Lake", "Sunset", "Pine", "Shadow", "Leaf", "Dawn", "Glitter", "Forest", "Hill", "Cloud", "Meadow", "Sun", "Glade", "Bird", "Brook", "Butterfly", "Bush", "Dew", "Dust", "Field", "Fire", "Flower", "Firefly", "Feather", "Grass", "Haze", "Mountain", "Night", "Pond", "Darkness", "Snowflake", "Silence", "Sound", "Sky", "Shape", "Surf", "Thunder", "Violet", "Water", "Wildflower", "Wave", "Water", "Resonance", "Sun", "Wood", "Dream", "Cherry", "Tree", "Fog", "Frost", "Voice", "Paper", "Frog", "Smoke", "Star"};

        return String.format(
                "%s %s",
                adjs[(int) Math.floor(Math.random() * adjs.length)],
                nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    public String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
}
