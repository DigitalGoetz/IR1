package snedeker.goetz.ir.assignmentOne.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import snedeker.goetz.ir.assignmentOne.models.EntryDocument;

public class LuceneUtilities {

	static Logger log = Logger.getLogger(LuceneUtilities.class);

	public static void entryToFile(String documentStorePath, EntryDocument document) throws IOException {
		File docStore = new File(documentStorePath);
		if (!docStore.exists()) {
			docStore.mkdirs();
		}

		File docFile = new File(documentStorePath + File.separator + document.getDocId().toString());
		FileUtils.writeStringToFile(docFile, document.toString());
	}

	public static void printResults(Map<String, Integer> results) {
		int resultIndex = 1;
		for (String title : results.keySet()) {
			Integer docId = results.get(title);
			log.debug(resultIndex++ + ". " + title + " [" + docId + "]");
		}
	}

}
