package umbc.ebiquity.kang.htmldocument.impl;

import java.io.File;
import java.io.IOException;

import umbc.ebiquity.kang.htmldocument.AbstractSingleHtmlDocument;

public class SingleHtmlDocument extends AbstractSingleHtmlDocument {

	private File location;

	public SingleHtmlDocument(File location) throws IOException {
		super();
		this.location = location;
	}

	@Override
	protected String getDocLocation() throws IOException {
			return location.getCanonicalPath();
	}

}
