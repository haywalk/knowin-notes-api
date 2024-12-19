package ca.mta.comp4721.team4.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

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

        // beginning of document
        buffer.append("\\documentclass{article}\n");
        buffer.append("\\usepackage{txfonts}");
        buffer.append("\\date{}\n");
        buffer.append("\\title{Knowin' Notes: Report}\n");
        buffer.append("\\begin{document}\n");
        buffer.append("\\maketitle\n");

        buffer.append("\\centering");
        
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
    public static void compileTex() throws InterruptedException, IOException {       
        // invoke pdflatex
        Process process = Runtime.getRuntime().exec("pdflatex -output-directory=" + TEX_DIR + " " + TEX_FILE);
        
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

    public static void main(String[] args) throws InterruptedException, IOException {
        compileTex();
    }
}
