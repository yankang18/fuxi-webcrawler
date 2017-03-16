package umbc.ebiquity.kang.htmldocument.parser.impl;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlPath;
import umbc.ebiquity.kang.htmldocument.parser.IHtmlParsedPathsHolder;

public class DefaultHtmlParsedPathsHolder implements IHtmlParsedPathsHolder {
 
	private String pageURL;
	private String domainName;
	private List<IHtmlPath> pathList;

	public DefaultHtmlParsedPathsHolder(String pageURL, String domainName,
			List<IHtmlPath> webPagePathList) {
		this.pageURL = pageURL;
		this.domainName = domainName;
		this.pathList = webPagePathList;
	}
	
	@Override
	public List<IHtmlPath> listHtmlPaths() {
		return pathList;
	}

	@Override
	public String getUniqueIdentifier() {
		return pageURL;
	}

	@Override
	public String getDomainName() {
		return domainName;
	}

}
