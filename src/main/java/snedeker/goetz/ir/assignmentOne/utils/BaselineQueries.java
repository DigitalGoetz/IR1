package snedeker.goetz.ir.assignmentOne.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import snedeker.goetz.ir.assignmentOne.services.IndexerService;

public class BaselineQueries {

	Logger log = Logger.getLogger(getClass());
	SetType type;
	Map<Integer, String> queries;

	public BaselineQueries(SetType type) {
		this.type = type;
		queries = new HashMap<>();
		init();
	}

	public List<Integer> getAvailableQueryIds() {
		List<Integer> ids = new ArrayList<>(queries.keySet());
		Collections.sort(ids);
		return ids;
	}

	public String getQueryString(Integer queryId) {
		String queryString = queries.get(queryId);
		return (queryString == null) ? "" : queryString;
	}

	private String getBaselineQueryfilename() {
		StringBuilder sb = new StringBuilder();
		if (type == SetType.CRAN) {
			sb.append("cran");
		} else {
			sb.append("med");
		}

		sb.append(".query");

		return sb.toString();
	}

	private void init() {
		// Load in the baseline details for the particular dataset
		String filename = getBaselineQueryfilename();
		String text = "";
		try (InputStream stream = IndexerService.class.getClassLoader().getResourceAsStream(filename)) {
			text = IOUtils.toString(stream);
		} catch (IOException e) {
			log.error("Error reading baseline file", e);
		}

		// Split into docs
		String[] splitDocs = text.split(".I");

		for (String doc : splitDocs) {
			String query = doc.trim();

			if (query != null && !query.isEmpty()) {
				// get additional
				String[] queryParts = doc.split(".W");
				Integer queryId = Integer.parseInt(queryParts[0].trim());
				String queryText = queryParts[1].trim();

				queries.put(queryId, queryText);
			}
		}

	}
}
