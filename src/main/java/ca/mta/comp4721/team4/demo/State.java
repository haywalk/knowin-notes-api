package ca.mta.comp4721.team4.demo;

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
            && (System.currentTimeMillis() / 1000L) - jsonObject.getInt("gameStartTime") >= jsonObject.getInt("gameDuration") ) {
            isFinished = true;
            return;
        }

        // mode notes and x notes played -> end

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
        return "{}"; // TODO implement
    }
}
