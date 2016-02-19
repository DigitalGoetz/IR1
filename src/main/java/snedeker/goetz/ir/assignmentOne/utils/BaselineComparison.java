package snedeker.goetz.ir.assignmentOne.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class BaselineComparison {

	Logger log = Logger.getLogger(getClass());
	SetType type;

	public BaselineComparison(SetType type) {
		this.type = type;
		init();
	}

	private String getBaselinefilename() {
		StringBuilder sb = new StringBuilder();
		if (type == SetType.CRAN) {
			sb.append("med");
		} else {
			sb.append("cran");
		}

		sb.append(".rel");

		return sb.toString();
	}

	private void init() {
		// Load in the baseline details for the particular dataset
		String filename = getBaselinefilename();
		try (InputStream is = BaselineComparison.class.getClassLoader().getResourceAsStream(filename)) {
			List<String> lines = IOUtils.readLines(is);

			for (String string : lines) {
				if (!string.isEmpty()) {
					String[] columns = string.split(" ");
					Integer queryId = Integer.parseInt(columns[0]);
					Integer documentId = Integer.parseInt(columns[1]);

					// TODO add documentID to the list associated with the given
					// query id here
				}
			}
		} catch (IOException e) {
			log.error("Error reading baseline file", e);
		}

		// -Query ID and corresponding list of document IDs
	}

	public enum SetType {
		MED, CRAN;
	}
}
