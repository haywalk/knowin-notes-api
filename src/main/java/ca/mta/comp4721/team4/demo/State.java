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
        if(jsonObject.getString("gameMode").equals("timed")
        && countCorrectKeys() == jsonObject.getInt("notesInGame")) {
            isFinished = true;
            return;
        }

        // otherwise check new notes, update clock, generate new notes
        int targetKeys = jsonObject.getInt("targetNumNotes");
        
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
        long gameDuration = currentTime - startTime / 60000L;
        report.put("chronometer", gameDuration);

        /*
         * calculate mistakes
         */
        int mistakes = numNotesPlayed - countCorrectKeys();
        report.put("numMistakes", mistakes);

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
}
