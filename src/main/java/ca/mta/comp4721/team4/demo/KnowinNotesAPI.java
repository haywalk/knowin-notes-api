package ca.mta.comp4721.team4.demo;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Knowin' Notes REST API.
 * 
 * @author Hayden Walker
 * @version 2024-11-05
 */
@SpringBootApplication
@RestController
public class KnowinNotesAPI {
	/**
	 * Run the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(KnowinNotesAPI.class, args);
	}

	/**
	 * List reports.
	 * 
	 * @return JSON list of report IDs.
	 */
	@GetMapping("/api/LIST_REPORTS")
	public String listReports() {
		return ReportDB.instance().getReports();
	}
	
	/**
	 * Get a report.
	 * 
	 * @param id Report ID.
	 * @return Report as a JSON string.
	 */
	@GetMapping("/api/GET_REPORT")
	public String listReports(@RequestParam(value = "id", defaultValue = "") String id) {
		return ReportDB.instance().getReportByID(Integer.parseInt(id));
	}

	/**
	 * Update state.
	 * 
	 * @param oldStateEncoded Old state as a JSON string encoded in base64.
	 * @return New state as a JSON string.
	 */
	@GetMapping("/api/GET_STATE")
	public String getState(@RequestParam(value = "old", defaultValue = "") String oldStateEncoded) {
		// oldstate is JSON.stringify -> base64
		byte[] decoded = Base64.getDecoder().decode(oldStateEncoded);
		String JSONstring = new String(decoded, StandardCharsets.UTF_8);
		
		// parse into a State object
		State state = new State(JSONstring);
		state.update(); // update State

		if(state.isFinished()) {
			ReportDB.instance().addReport(state.toReport()); // store report in database
			return "REPORT" + state.toReport();
		}

		// return to JSON
		return "STATE" + state.toJSON();
	}

	/**
	 * Generate a PDF version of a report.
	 * 
	 * @param id Report ID.
	 * @return PDF file.
	 */
	// @GetMapping("/api/GENERATE_PDF")
	// public String generatePDF(@RequestParam(value = "id", defaultValue = "") String id) {
	// 	String report = ReportDB.instance().getReportByID(Integer.parseInt(id));
	// 	JsonToPdf.savePDF(report, "report.pdf");
	// 	return "done";
	// }
	@GetMapping("/api/GENERATE_PDF")
	public ResponseEntity<byte[]> generatePDF(@RequestParam(value = "id", defaultValue = "") String id) throws IOException{
		// open file
		File output = new File("report.pdf");
		
		// produce pdf
		String report = ReportDB.instance().getReportByID(Integer.parseInt(id));
		JsonToPdf.savePDF(report, output);

		// get pdf as array of bytes
		byte[] contents = FileUtils.readFileToByteArray(output);

		// lines from here down are based on
		// https://stackoverflow.com/questions/16652760/return-generated-pdf-using-spring-mvc

		// set the HTTP content typu
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);

		// set download file name
		headers.setContentDispositionFormData("report.pdf", "report.pdf");
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		// send response
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	}
}
