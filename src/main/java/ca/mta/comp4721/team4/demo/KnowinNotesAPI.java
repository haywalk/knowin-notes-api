package ca.mta.comp4721.team4.demo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
	@GetMapping("/api/GENERATE_PDF")
	public String generatePDF(@RequestParam(value = "id", defaultValue = "") String id) {
		String report = ReportDB.instance().getReportByID(Integer.parseInt(id));
		JsonToPdf.savePDF(report, "report.pdf");
		return "done";
	}
}
