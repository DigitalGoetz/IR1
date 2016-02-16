package snedeker.goetz.ir.assignmentOne.utils;

import java.io.File;
import java.util.Map;

import org.apache.log4j.Logger;

public class Metrics {

	static Logger log = Logger.getLogger(Metrics.class);

	public static int getFolderSizeOnDisk(File directory) {
		int size = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				size += file.length();
			} else {
				size += getFolderSizeOnDisk(file);
			}
		}
		return size;
	}

	public static void printIndexTerms(Map<String, Long> postings) {
		int counter = 1;
		for (String key : postings.keySet()) {
			Long count = postings.get(key);

			if (count > 300) {
				log.debug(counter + " Term: " + key + " contains " + count + " occurrences within the index");
				counter++;
			}

		}
	}
}
