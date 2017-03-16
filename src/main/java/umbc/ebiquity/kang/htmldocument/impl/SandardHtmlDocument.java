package umbc.ebiquity.kang.htmldocument.impl;

import java.io.File;
import java.io.IOException;

import umbc.ebiquity.kang.htmldocument.AbstractStandardHtmlDocument;

public class SandardHtmlDocument extends AbstractStandardHtmlDocument {

	private File location;

	public SandardHtmlDocument(File location) throws IOException {
		super();
		this.location = location;
	}

	@Override
	protected String getDocLocation() throws IOException {
			return location.getCanonicalPath();
	}

	@Override
	public String getDomainName() {
		return "";
	}

}
