package snedeker.goetz.ir.assignmentOne.utils;

import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import snedeker.goetz.ir.assignmentOne.SearchAppliance;

public class Metrics {

	static Logger log = Logger.getLogger(Metrics.class);

	public static int getFolderSizeOnDisk(File directory) {
		int size = 0;

		if (directory.exists() && directory.isDirectory()) {
			for (File file : directory.listFiles()) {
				if (file.isFile()) {
					size += file.length();
				} else {
					size += getFolderSizeOnDisk(file);
				}
			}
		} else {
			size = -1;
		}

		return size;
	}

	public static int getDocumentCount(File directory) {
		int count = 0;

		if (directory.exists() && directory.isDirectory()) {
			count = directory.listFiles().length;
		} else {
			count = -1;
		}

		return count;
	}

	public static void printIndexTerms(SearchAppliance app) throws IOException {

		File indexDirectory = new File(app.getType() + app.getIndexId() + File.separator + "index");
		Directory directory = FSDirectory.open(indexDirectory.toPath());
		IndexReader reader = DirectoryReader.open(directory);

		Terms terms = SlowCompositeReaderWrapper.wrap(reader).terms("content");

		TermsEnum iterator = terms.iterator();
		BytesRef next = iterator.next();

		while (next != null) {
			long freq = reader.totalTermFreq(new Term("content", next));
			if (freq > 200) {
				log.debug(next.utf8ToString() + ": " + freq);
			}
			next = iterator.next();
		}

	}
}
