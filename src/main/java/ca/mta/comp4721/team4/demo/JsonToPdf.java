package ca.mta.comp4721.team4.demo;

import java.io.File;
import java.io.IOException;
//import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONObject;

//import com.fasterxml.jackson.databind.JsonNode;

/**
 * Converts JSON reports into PDFs..
 * 
 * @author Tanner Altenkirk, Hayden Walker
 * @version November 14, 2024
 */
public class JsonToPdf {
    /**
     * Private constructor.
     */
    private JsonToPdf(){}

    /**
     * Given a report, save it to a PDF file.
     * 
     * @param report Report as a JSON string.
     * @param file PDF file.
     */
    public static void savePDF(String report, String file) {
        try {
            PDDocument outputDoc = produceReportPDF(new JSONObject(report));
            outputDoc.save(new File(file));
        } catch(IOException e) {

        }
    }

    /**
     * Convert a report into a PDDocument object.
     * 
     * @param report JSON report.
     * @return Report as PDDocument.
     * @throws IOException If fails to write.
     */
    public static PDDocument produceReportPDF(JSONObject report) throws IOException{
        //create a pdf document to put the results in
        PDDocument outputDoc = new PDDocument();
        PDPage page = new PDPage();
        outputDoc.addPage(page);

        //Exception thrown by PDPageContentStream to be handled elsewhere
        PDPageContentStream contentStream = new PDPageContentStream(outputDoc, page); 
        contentStream.setFont(PDType1Font.TIMES_BOLD, 12); //for some reason, all font names throw unresolved errors... this will probably need to be fixed lol
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 700);

        for(String field : JSONObject.getNames(report)) {
            contentStream.showText(field + ": " + report.get(field).toString());
            contentStream.newLineAtOffset(0, -15);
        }

        contentStream.endText();
        contentStream.close();
        return outputDoc;
    }
}
