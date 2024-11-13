import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This is the class that turns json files into pdf files for the reports after
 * playing the game.
 * NOTE: I didn't get the imports above working, I will leave it to Gradle 
 * magic.
 * 
 * @author Tanner Altenkirk
 * @version November 12, 2024
 */
public class JsonToPdf {

    private JsonToPdf(){}

    public static PDDocument convertJsonToPdf(JsonNode data) throws IOException{
        //create a pdf document to put the results in
        PDDocument outputDoc = new PDDocument();
        PDPage page = new PDPage();
        outputDoc.addPage(page);

        //Exception thrown by PDPageContentStream to be handled elsewhere
        PDPageContentStream contentStream = new PDPageContentStream(outputDoc, page); 
        contentStream.setFont(PDType1Font.TIMES_BOLD, 12); //for some reason, all font names throw unresolved errors... this will probably need to be fixed lol
        contentStream.newLineAtOffset(50, 700);

        //field names in order should be: 'Date', 'Time', 'GameMode', 'clef', 'notetypes', 'Gameduration', 'notesInGame', 'timePerNote', 'accuracy'
        Iterator<String> fieldNames = data.fieldNames();
        while(fieldNames.hasNext()){
            String currentField = fieldNames.next();
            contentStream.showText(currentField + ": " + data.get(currentField).asText());
            contentStream.newLineAtOffset(0, -15);
        }
        contentStream.endText();
        return outputDoc;
    }

    //this method takes in a JsonNode and a file path and saves the data as a pdf to that file location
    public void savePdf(JsonNode data, String filePath) throws IOException{
        PDDocument outputDoc = convertJsonToPdf(data);
        outputDoc.save(new File(filePath));
    }
}
