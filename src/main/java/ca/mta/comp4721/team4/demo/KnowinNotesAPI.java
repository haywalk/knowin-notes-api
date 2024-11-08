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
		return "yolo";
	}
	
	/**
	 * Get a report.
	 * 
	 * @param id Report ID.
	 * @return Report as a JSON string.
	 */
	@GetMapping("/api/GET_REPORT")
	public String listReports(@RequestParam(value = "id", defaultValue = "") String id) {
		return "yolo";
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
		State oldState = new State(JSONstring);
		oldState.update(); // update State

		// return to JSON
		return oldState.toJSON();
	}

	/**
	 * Generate a PDF version of a report.
	 * 
	 * @param id Report ID.
	 * @return PDF file.
	 */
	@GetMapping("/api/GENERATE_PDF")
	public String generatePDF(@RequestParam(value = "id", defaultValue = "") String id) {
		return "asdf";
	}
}
