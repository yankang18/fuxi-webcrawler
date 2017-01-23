package umbc.ebiquity.kang.htmldocument;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.nodes.Document;

public abstract class AbstractHTMLDocument implements IHtmlDocument {
	
	private Document htmlDoc;
	private boolean loaded;
	private Map<String, String> externalLinks;
	
	protected AbstractHTMLDocument() throws IOException {
		this.externalLinks = new LinkedHashMap<String, String>();
	}
	
	@Override
	public void load() throws IOException {
		if (!isLoaded()) {
			String location = getDocLocation();
			this.loaded = true;
			if (validateLocation(location)) {
				this.htmlDoc = loadDocument(location); 
				postProcess(getDocument());
			} else {
				// TODO: use a more specific exception
				throw new IOException(location + " is not a valid URL");
			}
		}
	}

	@Override
	public Document getDocument() {
		return htmlDoc;
	}
	
	@Override
	public boolean isLoaded() { 
		return loaded;
	}
	
	@Override
	public Map<String, String> getExternalLinks(){
		return this.externalLinks;
	}
	
	protected void addExternalLink(String href, String name){
		externalLinks.put(href, name);
	}
	
	protected abstract String getDocLocation() throws IOException;
	
	protected abstract boolean validateLocation(String location);
	
	protected abstract Document loadDocument(String location) throws IOException;
	
	protected abstract void postProcess(Document webPageDoc);

}
