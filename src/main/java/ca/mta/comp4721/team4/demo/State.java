package ca.mta.comp4721.team4.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

// JSON STRING FORMAT
// 
// {
//     "gameMode": "timed",
//     "gameDuration": -1, 
//     "notesInGame": -1, 
//     "timePerNote": -1, 
//     "clef": "treble", 
//     "noteTypes": [
//         "single"
//     ], 
//     "gameStartTime": 1813248023948,
//     "currentTime": 1813248023948,
//     "targetNoteTimePairs": [],
//     "targetNumNotes": 1,
//     "playedNoteTimePairs": []
//      noteAccuracy = { "note": [correct, asked], ....}
// }

// REPORT JSON FORMAT 
//
// {
//     "id": 0,
//     "date": "2024-10-27",
//     "time": "11:05",
//     "type": "Time",
//     "accuracy": "75%",
//     "chronometer": "20:00",
//     "numNotes": 20,
//     "numMistakes": 5
// }

/**
 * Game state.
 * 
 * @author Hayden Walker
 * @version 2024-11-13
 */
public class State {
    /**
     * JSON object representing the state.
     */
    private JSONObject jsonObject;

    /**
     * {@code true} if the game is finished.
     */
    private boolean isFinished;

    /**
     * Create a new State object from a JSON string.
     * 
     * @param JSONstring Game state as a JSON string.
     */
    public State(String JSONstring) {
        jsonObject = new JSONObject(JSONstring);
        isFinished = false;
    }

    /**
     * Update the game state.
     */
    public void update() {
        // mode timed and time exceeded? -> end
        if(jsonObject.getString("gameMode").equals("timed")
            // check if time limit exceeded
            && System.currentTimeMillis() - jsonObject.getLong("gameStartTime") >= jsonObject.getLong("gameDuration") * 60000L ) {
            isFinished = true;
            return;
        }

        // mode notes and x notes played -> end
        if(jsonObject.getString("gameMode").equals("notes")
        && countCorrectKeys() >= jsonObject.getInt("notesInGame")) {
            isFinished = true;
            return;
        }

        JSONArray targetNoteTimePairs = jsonObject.getJSONArray("targetNoteTimePairs");

        // backwards compatibility fix: add noteAccuracy field if absent
        if(!jsonObject.has("noteAccuracy")) {
            jsonObject.put("noteAccuracy", new JSONObject());
        }


        // initial state
        if(targetNoteTimePairs.length() == 0) {
            JSONArray newNote = new JSONArray();
            newNote.put(NoteGenerator.note(jsonObject.getString("clef")));
            newNote.put(System.currentTimeMillis());
            targetNoteTimePairs.put(newNote);
            jsonObject.put("targetNoteTimePairs", targetNoteTimePairs);
            return;
        }

        // pull out target (note, time) pair
        JSONArray targetNote = targetNoteTimePairs.getJSONArray(targetNoteTimePairs.length() - 1);
        String targetNoteName = targetNote.getString(0);
        long targetNoteTime = targetNote.getLong(1);
        
        // flag for if we need to generate a new note or not
        boolean needNewNote = false;
  
        // loop over all keys that have been pressed
        JSONArray keysPressed = jsonObject.getJSONArray("playedNoteTimePairs");
        for(int i = 0; i < keysPressed.length(); i++) {
            // pull out data for this key
            JSONArray thisKey = keysPressed.getJSONArray(i);
            String keyName = thisKey.getString(0);
            long timePressed = thisKey.getLong(1);
            
            // previous key press (ignore)
            if(timePressed < targetNoteTime) {
                continue;
            }

            // incorrect key
            if(!keyName.equals(targetNoteName)) {
                thisKey.put(2, "i");
            } 
            
            // correct key
            else {
                needNewNote = true; // now we need to generate a new note
                thisKey.put(2, "c"); // update status
                updateNotesCorrect(targetNoteName);
                keysPressed.put(i, thisKey); // update key
            }
        }

        // update JSON object
        jsonObject.put("playedNoteTimePairs", keysPressed); 

        // append a new note if needed
        if(needNewNote) {
            JSONArray newNote = new JSONArray();
            String noteName = NoteGenerator.note(jsonObject.getString("clef"));
            updateNotesAsked(noteName);
            newNote.put(noteName);
            newNote.put(System.currentTimeMillis());
            targetNoteTimePairs.put(newNote);
            jsonObject.put("targetNoteTimePairs", targetNoteTimePairs);
        }

    }

    /**
     * Return the state as a JSON object.
     * 
     * @return The state as a JSON object.
     */
    public String toJSON() {
        return jsonObject.toString();
    }

    /**
     * Check if this is a terminal state.
     * 
     * @return {@code true} if this is a terminal game state.
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Convert the (terminal) state to a report and return as a JSON string.
     * 
     * @return Report as a JSON string.
     */
    public String toReport() {
        // create empty report
        JSONObject report = new JSONObject();

        /*
         * calculate ID
         */
        int id = ReportDB.instance().size();
        report.put("id", id);

        /*
         * calculate date and time
         */
        Date date = new Date();
        // format date/time
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        // store in report
        report.put("date", dateFormatter.format(date));
        report.put("time", timeFormatter.format(date));

        /*
         * calculate type
         */
        report.put("type", jsonObject.getString("gameMode"));

        /*
         * calculate accuracy
         */
        int numNotesPlayed = jsonObject.getJSONArray("playedNoteTimePairs").length();

        if(numNotesPlayed > 0) {

            double accuracy = countCorrectKeys() * 1.0 / numNotesPlayed;
            int accuracyPercent = (int) (accuracy * 100.0);
            report.put("accuracy", Integer.toString(accuracyPercent) + "%");    
        } 
        // no notes played (avoid dividing by zero)
        else {
            report.put("accuracy", "0%");
        }


        /*
         * calculate num notes
         */
        report.put("numNotes", numNotesPlayed);

        /*
         * calculate chronometer
         */
        long startTime = jsonObject.getLong("gameStartTime");
        long currentTime = jsonObject.getLong("currentTime");
        long gameDuration = (currentTime - startTime) / 1000L; // in seconds

        // format minutes and seconds
        String minutes = Integer.toString((int) (gameDuration / 60));
        int secondsInt = (int) gameDuration % 60;
        String seconds = "";
        // pad with zero
        if(secondsInt < 10) {
            seconds += "0";
        } 

        seconds += Integer.toString(secondsInt);
        report.put("chronometer", minutes + ":" + seconds);


        /*
         * calculate mistakes
         */
        int mistakes = numNotesPlayed - countCorrectKeys();
        report.put("numMistakes", mistakes);

        /*
         * add clef
         */
        report.put("clef", jsonObject.getString("clef"));

        /*
         * Add accuracy
         */
        report.put("noteAccuracy", jsonObject.getJSONObject("noteAccuracy"));

        /*
         * Add rest of settings
         */
        report.put("gameDuration", jsonObject.getInt("gameDuration"));
        report.put("notesInGame", jsonObject.getInt("notesInGame"));
        report.put("timePerNote", jsonObject.getInt("timePerNote"));
        report.put("noteType", jsonObject.getString("noteType"));
        report.put("targetNumNotes", jsonObject.getInt("targetNumNotes"));



        // return report string
        return report.toString();
    }

    /**
     * Count how many correct keys have been played.
     * 
     * @return How many correct keys have been played.
     */
    private int countCorrectKeys() {
        int count = 0;

        // loop over keys
        JSONArray keys = jsonObject.getJSONArray("playedNoteTimePairs");
        for(int i = 0; i < keys.length(); i++) {
            // pull out key
            JSONArray thisKey = keys.getJSONArray(i);
            // pull out status
            String status = thisKey.getString(2);
            if(status.equals("c")) {
                count++;
            }
        }

        return count;
    }

    /**
     * Increment the number of times a note has been asked for.
     * 
     * @param noteName Name of note.
     */
    private void updateNotesAsked(String noteName) {
        updateAccuracyField(noteName, 1);
    }

    /**
     * Increment the number of times a note has been played correctly.
     * 
     * @param noteName Name of note.
     */
    private void updateNotesCorrect(String noteName) {
        updateAccuracyField(noteName, 0);
    }

    /**
     * Increment the ith index of a note's accuracy array.
     * 
     * @param noteName Name of note.
     * @param index Index to increment.
     */
    private void updateAccuracyField(String noteName, int index) {
        // pull out accuracy list
        JSONObject accuracy = jsonObject.getJSONObject("noteAccuracy");

        // add new entry if needed
        if(!accuracy.has(noteName)) {
            JSONArray acc = new JSONArray();
            acc.put(0);
            acc.put(0);
            accuracy.put(noteName, acc);
        }

        // update times asked
        JSONArray thisNoteAccuracy = accuracy.getJSONArray(noteName);
        int timesAsked = thisNoteAccuracy.getInt(index) + 1;
        thisNoteAccuracy.put(index, timesAsked);
        
        // store
        accuracy.put(noteName, thisNoteAccuracy);
        jsonObject.put("noteAccuracy", accuracy);
    }
}
