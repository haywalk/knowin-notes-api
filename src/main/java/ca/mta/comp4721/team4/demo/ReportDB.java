package ca.mta.comp4721.team4.demo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Read and write reports to/from a file.
 */
public class ReportDB {
    /**
     * File in which to store the database.
     */
    private static final String DB_FILE = "db.json";

    /**
     * Singleton instance.
     */
    private static ReportDB db = null;

    /**
     * File in which to store the database.
     */
    private File file;

    /**
     * JSON array of data.
     */
    private JSONArray jsonArray;

    /**
     * Private constructor.
     */
    private ReportDB() {
        // open database file
        file = new File(DB_FILE);

        // attempt to read from file
        try {
            String json = FileUtils.readFileToString(file, "utf-8");
            jsonArray = new JSONArray(json);
        } 
        // fall back by making a new empty array
        catch(IOException e) {
            jsonArray = new JSONArray();
        }
    }

    /**
     * Return the database.
     * 
     * @return Singleton instance of the database.
     */
    public static ReportDB instance() {
        if(db == null) {
            db = new ReportDB();
        }

        return db;
    }

    /**
     * Return the list of reports as a JSON string.
     * 
     * @return List of reports as a JSON string.
     */
    public String getReports() {
        return jsonArray.toString();
    }

    /**
     * Add a report.
     * 
     * @param report Report as a JSON string.
     */
    public void addReport(String report) {
        JSONObject newReport = new JSONObject(report);
        jsonArray.put(newReport);
        updateFile();
    }

    /**
     * Get a report by its ID.
     * 
     * @param id Report ID.
     * @return Report as a JSON string.
     */
    public String getReportByID(int id) {
        // search database for JSON object with given ID
        for(Object obj : jsonArray) {
            JSONObject json = (JSONObject) obj;
            if(json.getInt("id") == id) {
                return json.toString();
            }
        }

        return "ERROR";
    }

    /**
     * Replace the file with the contents of the JSON array.
     */
    private void updateFile() {
        try {
            file.delete();
            FileUtils.write(file, jsonArray.toString(), "utf-8");
        } catch(IOException e) {

        }
    }

    /**
     * Return the size of the database.
     * 
     * @return Size of the database.
     */
    public int size() {
        return jsonArray.length();
    }
}
