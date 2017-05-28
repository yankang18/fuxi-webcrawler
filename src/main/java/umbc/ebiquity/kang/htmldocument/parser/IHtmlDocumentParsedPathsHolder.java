package umbc.ebiquity.kang.htmldocument.parser;

import java.util.List;

import umbc.ebiquity.kang.htmldocument.IHtmlPath;

public interface IHtmlDocumentParsedPathsHolder {

	List<IHtmlPath> listHtmlPaths(); 
	
	String getUniqueIdentifier();
	
	String getDomainName();

}
