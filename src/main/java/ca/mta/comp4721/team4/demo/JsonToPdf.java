package ca.mta.comp4721.team4.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Converts JSON reports into PDFs..
 * 
 * @author Tanner Altenkirk, Hayden Walker
 * @version November 14, 2024
 */
public class JsonToPdf {
    /**
     * Save location of the report PDF.
     */
    public static final String PDF_PATH = "./tex/report.pdf";

    /**
     * Location of LaTeX document.
     */
    private static final String TEX_FILE = "./tex/report.tex";

    /**
     * TeX output directory.
     */
    private static final String TEX_DIR = "./tex";

    /**
     * Clef integer.
     */
    private static String clef = "";

    /**
     * Private constructor.
     */
    private JsonToPdf(){}

    /**
     * Given a report, save it to a PDF file.
     * 
     * @param report Report as a JSON string.
     * @return {@code true} if successful.
     */
    public static boolean savePDF(String report) {
        // attempt to generate the PDF
        try {
            String tex = getTex(new JSONObject(report));
            System.out.println(tex);
            writeTex(tex);
            compileTex();
            return true;
        } 
        
        // return false if unsuccessful
        catch(IOException | InterruptedException e) {
            System.err.println("error");
            return false;
        }
    }

    private static String getTex(JSONObject report) {
        StringBuilder buffer = new StringBuilder();

        //get the clef and other necessary info for the visual
        clefFinder(report);

        // beginning of document
        buffer.append("\\documentclass{article}\n");
        buffer.append("\\usepackage{txfonts, musicography, musixtex, xcolor}");
        buffer.append("\\xdefinecolor{red-undar}{RGB}{179,35,79}");
        buffer.append("\\date{}\n");
        buffer.append("\\title{Knowin' Notes: Report}\n");
        buffer.append("\\begin{document}\n");
        buffer.append("\\maketitle\n");
        buffer.append("\\centering");

        // create accuracy visual
        buffer.append("\\begin{music}"); 
        buffer.append("\\instrumentnumber{1}"); 
        buffer.append("\\setstaffs1{1}");
        buffer.append("\\setclef{1}{" + clef + "}");
        buffer.append("\\startextract"); 
        buffer.append(texString(report));
        buffer.append("\\zendextract"); 
        buffer.append("\\end{music}");
        
        // create table
        buffer.append("\\begin{tabular}{|c|c|}\n");
        buffer.append("\\hline\n");
        
        buffer.append("Date & ");
        buffer.append(report.getString("date"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");


        buffer.append("Time & ");
        buffer.append(report.getString("time"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("Session ID & ");
        buffer.append(report.getInt("id"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("Practice Type & ");
        buffer.append(report.getString("type"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("Accuracy & ");
        buffer.append(report.getString("accuracy").replace("%", ""));
        buffer.append("\\%");
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("Timer & ");
        buffer.append(report.getString("chronometer"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("Number of Notes & ");
        buffer.append(report.getInt("numNotes"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("Mistakes & ");
        buffer.append(report.getInt("numMistakes"));
        buffer.append("\\\\\n");
        buffer.append("\\hline\n");

        buffer.append("\\end{tabular}\n");

        buffer.append("\n");
        buffer.append("\\vspace{1.5in}");
        buffer.append("\\centering");
        buffer.append("\\textsc{Knowin' Notes} is a product of the \\textsc{4721 Dream Team}.\n\n");
        buffer.append("\\textcopyright \\ 2024 \\textsc{Tanner Altenkirk}, \\textsc{Anthony Arseneau}, \\textsc{Sawyer Stanley}, and \\textsc{Hayden Walker}.");

        // end of document
        buffer.append("\\end{document}\n");


        return buffer.toString();
    }

    /**
     * Generate a LaTeX document.
     * 
     * @param tex TeX.
     * @throws IOException 
     */
    private static void writeTex(String tex) throws IOException {
        File texFile = new File(TEX_FILE);
        texFile.delete();
        FileWriter fileWriter = new FileWriter(texFile);
        fileWriter.write(tex);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * Compile LaTeX document.
     * 
     * @throws InterruptedException If process is interrupted.
     * @throws IOException If IO fails.
     */
    private static void compileTex() throws InterruptedException, IOException {       
        // invoke pdflatex
        Process process = Runtime.getRuntime().exec("/usr/bin/pdflatex -output-directory=" + TEX_DIR + " " + TEX_FILE);
        
        // capture standard out
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        // // print output if we get an error exit status
        // if(process.waitFor() != 0) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
//        }
    }

    /**
     * Get clef integer
     * @param report the reprot
     */
    private static void clefFinder(JSONObject report){
        String clefString = report.getString("clef");
        if(clefString == "treble"){
            clef = "0";
        }
        else{
            clef = "6";
        }
        System.out.println(clef);
    }

    /**
     * Get notes and clef for pdf visual. Returned Arraylist is of the form:
     *  {"clefNumber (0 for treble, 6 for bass)", "noteColor", "noteID", "noteColor", "noteID"}
     * @param report the report 
     * @throws InterruptedException If process is interrupted.
     * @throws IOException If IO fails.
     */
    private static HashMap<String, String> noteStrings(JSONObject report){
        HashMap<String, String> allNotes = new HashMap<>(); 
        HashMap<String, String> noteConversions = rightDict();
        JSONObject noteAccuracy = report.getJSONObject("noteAccuracy");
        for(String key : noteAccuracy.keySet()){
            allNotes.put(noteConversions.get(key), noteColor(noteAccuracy.getJSONArray(key)));
        }
        return allNotes;
    }

    /**
     * Get the appropriate color for a given note accuracy list.
     * @param JSONArray [correct, asked] (see State JSON formatting)
     * @throws InterruptedException
     * @throws IOException
     */
    private static String noteColor(JSONArray noteStats){
        float ratio = (float)noteStats.getInt(0) / (float)noteStats.getInt(1);
        if(ratio >= 0.9){
            return "green";
        }
        if(ratio >= 0.5){
            return "yellow";
        }
        return "red";
    }

    /**
     * Add notes that were not played but should be shown to the allNotes dict.
     * @param Dictionary the allNotes dict
     * @throws InterruptedException
     * @throws IOException
     */
    private static HashMap<String, String> addMissedNotes(HashMap<String, String> allNotes){
        HashMap<String, String> noteConversions = rightDict();
        for(String key : noteConversions.keySet()){
            if(allNotes.get(noteConversions.get(key)) == null){
                allNotes.put(noteConversions.get(key), "gray");
            }
        }
        return allNotes;
    }

    /**
     * Build the proper note conversion table for the given clef.
     * @param none
     * @throws InterruptedException
     * @throws IOException
     */
    private static HashMap<String, String> rightDict(){
        HashMap<String, String> noteConversions = new HashMap<>();
        if(clef == "0"){
            //build treble conversions here
            noteConversions.put("c4", "c");
            noteConversions.put("d4", "d");
            noteConversions.put("e4", "e");
            noteConversions.put("f4", "f");
            noteConversions.put("g4", "g");
            noteConversions.put("a5", "h");
            noteConversions.put("b5", "i");
            noteConversions.put("c5", "j");
            noteConversions.put("d5", "k");
            noteConversions.put("e5", "l");
            noteConversions.put("f5", "m");
            noteConversions.put("g5", "n");
            noteConversions.put("a6", "o");
        }
        else{
            //build base conversions here
            noteConversions.put("e2", "E");
            noteConversions.put("f2", "F");
            noteConversions.put("g2", "G");
            noteConversions.put("a3", "H");
            noteConversions.put("b3", "I");
            noteConversions.put("c3", "J");
            noteConversions.put("d3", "K");
            noteConversions.put("e3", "L");
            noteConversions.put("f3", "M");
            noteConversions.put("g3", "N");
            noteConversions.put("a4", "O");
            noteConversions.put("b4", "P");
            noteConversions.put("c4", "Q");
        }
        return noteConversions;
    }

    /**
     * Makes the latex line needed to draw the visual in the report.
     * @param report the report
     * @return String the string of latex to make the visual.
     * @throws InterruptedException
     * @throws IOException
     */
    private static String texString(JSONObject report){
        HashMap<String, String> notesDict = addMissedNotes(noteStrings(report));
        if(clef == "0"){
            return "\\Notes {\\color{" + notesDict.get("c") + "} \\qa c} {\\color{" + notesDict.get("d") + "} \\qa d} {\\color{" + notesDict.get("e") + "} \\qa e} {\\color{" + notesDict.get("f") + "} \\qa f} {\\color{" + notesDict.get("g") + "} \\qa g} {\\color{" + notesDict.get("h") + "} \\qa h} {\\color{" + notesDict.get("i") + "} \\qa i} {\\color{" + notesDict.get("j") + "} \\qa j} {\\color{" + notesDict.get("k") + "} \\qa k} {\\color{" + notesDict.get("l") + "} \\qa l} {\\color{" + notesDict.get("m") + "} \\qa m} {\\color{" + notesDict.get("n") + "} \\qa n} {\\color{" + notesDict.get("o") + "} \\qa o} \\en \\endpiece";
        }
        return "\\Notes {\\color{" + notesDict.get("E") + "} \\qa E} {\\color{" + notesDict.get("F") + "} \\qa F} {\\color{" + notesDict.get("G") + "} \\qa G} {\\color{" + notesDict.get("H") + "} \\qa H} {\\color{" + notesDict.get("I") + "} \\qa I} {\\color{" + notesDict.get("J") + "} \\qa J} {\\color{" + notesDict.get("K") + "} \\qa K} {\\color{" + notesDict.get("L") + "} \\qa L} {\\color{" + notesDict.get("M") + "} \\qa M} {\\color{" + notesDict.get("N") + "} \\qa N} {\\color{" + notesDict.get("O") + "} \\qa O} {\\color{" + notesDict.get("P") + "} \\qa P} {\\color{" + notesDict.get("Q") + "} \\qa Q} \\en \\endpiece";
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        compileTex();
    }
}
