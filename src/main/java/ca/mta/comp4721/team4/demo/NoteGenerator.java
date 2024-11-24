package ca.mta.comp4721.team4.demo;

import java.util.Random;

/**
 * Generate random notes.
 * 
 * @author Hayden Walker
 * @version 2024-11-24
 */
public class NoteGenerator {
    /**
     * Possible notes.
     */
    private static final String[] notes = new String[]{
        "c4", "cs4", 
        "d4", "ds4", 
        "e4", // no e sharp
        "f4", "fs4",
        "g4", "gs4",
        "a5", "as5",
        "b5", // no b sharp
        "c5"
    };

    /**
     * Private constructor.
     */
    private NoteGenerator() {}

    /**
     * Generate a random note between C4 and C5.
     * 
     * Format: "C sharp 4" -> cs4
     *  "B 5" -> b5
     * 
     * @return Random note between C4 and C5.
     */
    public static String note() {
        Random random = new Random();
        int noteIndex = random.nextInt(notes.length);
        return notes[noteIndex];
    }
}
