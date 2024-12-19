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
     * Possible notes for the treble clef.
     */
    private static final String[] TREBLE_NOTES = new String[]{
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
     * Possible notes for the bass clef.
     */
    private static final String[] BASS_NOTES = new String[]{
        "c3", "cs3", 
        "d3", "ds3", 
        "e3", // no e sharp
        "f3", "fs3",
        "g3", "gs3",
        "a4", "as4",
        "b4", // no b sharp
        "c4"
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
    public static String note(String clef) {
        Random random = new Random();

        if(clef.toLowerCase().equals("bass")) {
            int bassIndex = random.nextInt(BASS_NOTES.length);
            return BASS_NOTES[bassIndex];
        }

        int trebleIndex = random.nextInt(TREBLE_NOTES.length);
        return TREBLE_NOTES[trebleIndex];
    }


}
