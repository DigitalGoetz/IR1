package snedeker.goetz.ir.assignmentOne.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class BaselineComparison {

	Logger log = Logger.getLogger(getClass());
	SetType type;
	BaselineData data;

	public BaselineComparison(SetType type) {
		this.type = type;
		data = new BaselineData();
		init();
	}

	public List<Integer> getExpectedDocumentIds(Integer queryId) {
		List<Integer> documentIds = data.getDocumentIds(queryId);
		Collections.sort(documentIds);
		return documentIds;
	}

	private String getBaselinefilename() {
		StringBuilder sb = new StringBuilder();
		if (type == SetType.CRAN) {
			sb.append("cran");
		} else {
			sb.append("med");
		}

		sb.append(".rel");

		return sb.toString();
	}

	private void init() {
		// Load in the baseline details for the particular dataset
		String filename = getBaselinefilename();

		log.debug("Reading baseline information from " + filename);

		try (InputStream is = BaselineComparison.class.getClassLoader().getResourceAsStream(filename)) {
			List<String> lines = IOUtils.readLines(is);

			for (String string : lines) {

				if (!string.isEmpty()) {
					String[] columns = string.split(" ");

					try {
						Integer queryId = Integer.parseInt(columns[0].trim());
						Integer documentId = Integer.parseInt(columns[1].trim());

						data.insert(queryId, documentId);
					} catch (NumberFormatException e) {
						log.error("NFE");
					}

				}
			}
		} catch (IOException e) {
			log.error("Error reading baseline file", e);
		}
	}
}
