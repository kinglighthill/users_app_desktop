package com.scholarly.utme.data.util;

import java.util.Random;

public class Generator {
    private int values[] = new int[8];
    private int x;

    public static String Error() {
        Generator generator = new Generator();
        generator.x = generator.randomIntGenerator();
        generator.RandomTextGenerator();
        return generator.stringGenerator();
    }

    public static String Exception() {
        Generator generator = new Generator();
        generator.x = generator.randomIntGenerator();
        generator.RandomTextGenerator();
        return generator.stringGenerator().toUpperCase();
    }

    private int randomIntGenerator() {
        Random rand = new Random();
        return rand.nextInt(9);
    }

    private void RandomTextGenerator() {
        Random rand = new Random();
        for (int i = 0; i< values.length; i++) {
            switch (i) {
                case 0:
                    values[i] = rand.nextInt(122 + 1) + 0;
                    while (values[i] != 75 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 1:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 105 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 2:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 110 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 3:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 103 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 4:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 85 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 5:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 114 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 6:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 99 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;

                case 7:
                    values[i] = rand.nextInt(57 + 1) + 65;
                    while (values[i] != 104 - x) {
                        values[i] = rand.nextInt(57 + 1) + 65;
                    }
                    break;
            }
        }
    }

    private String stringGenerator() {
        String string = "";
        for (int value : values) {
            string += (char) (value + x);
        }
        return string;
    }
}