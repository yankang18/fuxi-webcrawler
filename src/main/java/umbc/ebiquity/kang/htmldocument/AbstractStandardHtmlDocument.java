package umbc.ebiquity.kang.htmldocument;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractStandardHtmlDocument extends AbstractHTMLDocument {

	protected AbstractStandardHtmlDocument() throws IOException {
		super();
	}

	@Override
	protected Document loadDocument(String location) throws IOException {
		File input = new File(location);
		return Jsoup.parse(input, "UTF-8", "http://example.com/");
	}

	@Override
	public String getUniqueIdentifier() {
		return UUID.randomUUID().toString();
	}

	@Override
	protected boolean isValid(String location) {
		return true;
	}

	@Override
	protected void postProcess(Document webPageDoc) {
	}

}
