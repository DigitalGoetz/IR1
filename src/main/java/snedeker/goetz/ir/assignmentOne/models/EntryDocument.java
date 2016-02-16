package snedeker.goetz.ir.assignmentOne.models;

import org.apache.commons.lang3.StringUtils;

public class EntryDocument {
	private Integer docId;
	private String title;
	private String contents;
	private String author;
	private String additional;

	public EntryDocument(String documentString) {
		String[] docs = documentString.split(".I");

		String doc;
		if (docs.length > 0) {
			doc = docs[1];
		} else {
			doc = docs[0];
		}

		// Get ID
		String[] docId = doc.split(".T");
		setDocId(Integer.parseInt(StringUtils.trim(docId[0])));

		// get title
		String[] title = docId[1].split(".A");
		setTitle(StringUtils.trim(title[0]));

		// get author
		String[] author = title[1].split(".B");
		setAuthor(StringUtils.trim(author[0]));

		// get additional
		String[] additional = author[1].split(".W");
		setAdditional(StringUtils.trim(additional[0]));

		// get contents
		setContents(StringUtils.trim(additional[1]));
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
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
