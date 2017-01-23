package umbc.ebiquity.kang.htmldocument.parser;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlDocumentPath;

public class DefaultHtmlDocumentParsedPathsHolder implements IHtmlDocumentParsedPathsHolder {
 
	private String pageURL;
	private List<IHtmlDocumentPath> webPagePathList;
	public DefaultHtmlDocumentParsedPathsHolder(String pageURL, List<IHtmlDocumentPath> webPagePathList) {
		this.pageURL = pageURL;
		this.webPagePathList = webPagePathList;
	}
	
	@Override
	public List<IHtmlDocumentPath> listHtmlDocumentPaths() {
		return webPagePathList;
	}

	@Override
	public String getUniqueIdentifier() {
		return pageURL;
	}

}
