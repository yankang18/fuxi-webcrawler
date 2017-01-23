package umbc.ebiquity.kang.htmldocument;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class AbstractSingleHtmlDocument extends AbstractHTMLDocument {

	protected AbstractSingleHtmlDocument() throws IOException {
		super();
	}

	@Override
	protected Document loadDocument(String location) throws IOException {
		File input = new File(location);
		return Jsoup.parse(input, "UTF-8", "http://example.com/");
	}

	@Override
	public void extractLinks(Set<String> locOfExcludedDoc) {
	}

	@Override
	public String getUniqueIdentifier() {
		return UUID.randomUUID().toString();
	}

	@Override
	protected boolean validateLocation(String location) {
		return true;
	}

	@Override
	protected void postProcess(Document webPageDoc) {
	}

}
