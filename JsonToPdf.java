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
 * @version November 6, 2024
 */
public class JsonToPdf {

    public static void main(String[] args) {

    }

    private void convertJsonToPdf(JsonNode data, String filepath){
        //create a pdf document to put the results in
        PDDocument outputDoc = new PDDocument();
        PDPage page = new PDPage();
        outputDoc.addPage(page);

        //must put next section in try/catch since PDPageContentStream throws IOException
        try { 
            PDPageContentStream contentStream = new PDPageContentStream(outputDoc, page);
            contentStream.setFont(PDType1Font.TIMES_BOLD, 12);
            contentStream.newLineAtOffset(50, 700);

            //field names in order should be: 'Date', 'Time', 'GameMode', 'clef', 'notetypes', 'Gameduration', 'notesInGame', 'timePerNote', 'accuracy'
            
        }


    }
}