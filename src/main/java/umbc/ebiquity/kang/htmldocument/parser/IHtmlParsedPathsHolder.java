package umbc.ebiquity.kang.htmldocument.parser;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlPath;

public interface IHtmlParsedPathsHolder {

	List<IHtmlPath> listHtmlPaths(); 
	
	String getUniqueIdentifier();
	
	String getDomainName();

}
