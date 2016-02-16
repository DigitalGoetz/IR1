package snedeker.goetz.ir.assignmentOne.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import snedeker.goetz.ir.assignmentOne.models.EntryDocument;
import snedeker.goetz.ir.assignmentOne.models.QueryResults;

public class LuceneUtilities {

	public static QueryResults rawSearch(String documentStorePath, String query) throws IOException {
		QueryResults results = new QueryResults();

		File directory = new File(documentStorePath);

		if (!directory.exists()) {
			results.setHits(0);
			results.setQueryTime(0L);
			results.getResultsMap();
		} else {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					String docString = FileUtils.readFileToString(file);
					EntryDocument doc = EntryDocument.buildEntry(docString);
					// TODO place query logic here
					if (doc != null) {
						results.add(doc);
					}
				}
			}
		}

		return results;
	}

	public static void entryToFile(String documentStorePath, EntryDocument document) throws IOException {
		File docStore = new File(documentStorePath);
		if (!docStore.exists()) {
			docStore.mkdirs();
		}

		File docFile = new File(document.getDocId().toString());
		FileUtils.writeStringToFile(docFile, document.toString());
	}

}
