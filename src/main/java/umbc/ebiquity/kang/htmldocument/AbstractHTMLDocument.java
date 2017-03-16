package umbc.ebiquity.kang.htmldocument;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class AbstractHTMLDocument implements IHtmlDocument {
	
	private Document htmlDoc;
	private boolean loaded;
	
	@Override
	public void load() throws IOException {
		if (!isLoaded()) {
			String location = getDocLocation();
			this.loaded = true;
			if (isValid(location)) {
				this.htmlDoc = loadDocument(location); 
				postProcess(getDocument());
			} else {
				// TODO: use a more specific exception
				throw new IOException(location + " is not a valid URL");
			}
		}
	}

	@Override
	public Element getBody(){
		return htmlDoc.body();
	}
	
	@Override
	public Document getDocument() {
		return htmlDoc;
	}
	
	@Override
	public boolean isLoaded() { 
		return loaded;
	}
	
	protected abstract String getDocLocation() throws IOException;
	
	protected abstract boolean isValid(String location);
	
	protected abstract Document loadDocument(String location) throws IOException;
	
	protected abstract void postProcess(Document webPageDoc);

}
