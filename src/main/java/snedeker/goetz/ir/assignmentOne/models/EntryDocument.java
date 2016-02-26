package snedeker.goetz.ir.assignmentOne.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import snedeker.goetz.ir.assignmentOne.utils.DataSource;

public class EntryDocument {

	static Logger log = Logger.getLogger(EntryDocument.class);

	private Integer docId;
	private String title;
	private String contents;
	private String author;
	private String additional;

	public static EntryDocument buildEntry(String documentString, DataSource datasource) {
		EntryDocument document = new EntryDocument();

		if (datasource == DataSource.MED) {
			String[] docs = documentString.split(".I");

			String doc;
			if (docs.length > 1) {
				doc = docs[1];
			} else {
				doc = docs[0];
			}

			// Get ID
			String[] docId = doc.split(".W");
			document.setDocId(Integer.parseInt(StringUtils.trim(docId[0])));
			document.setContents(docId[1]);

		} else {
			try {
				String[] docs = documentString.split(".I");

				String doc;
				if (docs.length > 1) {
					doc = docs[1];
				} else {
					doc = docs[0];
				}

				// Get ID
				String[] docId = doc.split(".T");
				document.setDocId(Integer.parseInt(StringUtils.trim(docId[0])));

				// get title
				String[] title = docId[1].split(".A");
				document.setTitle(StringUtils.trim(title[0]));

				// get author
				String[] author = title[1].split(".B");
				document.setAuthor(StringUtils.trim(author[0]));

				// get additional
				String[] additional = author[1].split(".W");
				document.setAdditional(StringUtils.trim(additional[0]));

				// get contents
				document.setContents(StringUtils.trim(additional[1]));
			} catch (Exception e) {
				log.error("Error building EntryDocument", e);
				document = null;
			}
		}
		return document;
	}

	public String getAdditional() {
		if (additional == null) {
			additional = "";
		}
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public Integer getDocId() {
		if (docId == null) {
			docId = new Integer(0);
		}
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getAuthor() {
		if (author == null) {
			author = "";
		}
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		if (title == null) {
			title = "";
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		if (contents == null) {
			contents = "";
		}
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(".I ");
		sb.append(getDocId());
		sb.append("\n");

		sb.append(".T\n");
		sb.append(getTitle());
		sb.append("\n");

		sb.append(".A\n");
		sb.append(getAuthor());
		sb.append("\n");

		sb.append(".B\n");
		sb.append(getAdditional());
		sb.append("\n");

		sb.append(".W\n");
		sb.append(getContents());
		sb.append("\n");

		return sb.toString();
	}
}
